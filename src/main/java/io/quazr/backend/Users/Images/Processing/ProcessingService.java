package io.quazr.backend.Users.Images.Processing;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.quazr.backend.Users.Images.UserImagesBucket;
import io.quazr.backend.Users.Images.UserImageEntity;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.Message;

import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;
import software.amazon.awssdk.services.s3.model.BucketAlreadyExistsException;
import software.amazon.awssdk.core.exception.ApiCallAttemptTimeoutException;
import software.amazon.awssdk.core.exception.ApiCallTimeoutException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ChecksumAlgorithm;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.transfer.s3.model.CompletedUpload;
import software.amazon.awssdk.transfer.s3.model.UploadRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.nio.charset.StandardCharsets;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProcessingService {
    private final String imagesExchange;
    private final String imagesTasksRoutingKey;
    private final S3TransferManager transferManager;
    private final ExecutorService s3UploadExecutor;
    private final String rustfsImagesBucket;
    private final RabbitTemplate rabbitMQ;
    private final S3Client s3Client;

    private final Map<UUID, ProcessingPipeline> pipelinesMap = new HashMap<UUID, ProcessingPipeline>();

    public ProcessingService(UserImagesBucket imagesBucket, S3Client s3Client, S3TransferManager s3TransferManager, ProcessingBroker rabbitMQConfig, RabbitTemplate rabbitTemplate) {
        this.imagesTasksRoutingKey = rabbitMQConfig.getImagesTasksRoutingKey();
        this.imagesExchange = rabbitMQConfig.getImagesExchange();

        this.s3UploadExecutor = Executors.newFixedThreadPool(4);
        this.rustfsImagesBucket = imagesBucket.getRustfsImagesBucket();

        this.transferManager = s3TransferManager;
        this.rabbitMQ = rabbitTemplate;
        this.s3Client = s3Client;

        try {
            this.s3Client.createBucket(CreateBucketRequest.builder().bucket(this.rustfsImagesBucket).build());
            System.out.println("Bucket created: %s".formatted(this.rustfsImagesBucket));
        } catch (BucketAlreadyExistsException | BucketAlreadyOwnedByYouException e) {
            System.out.println("Bucket '%s' already exists".formatted(this.rustfsImagesBucket));
        }
    }
    
    public void processImage(MultipartFile image, UserImageEntity imageEntity) throws Exception {
        try (InputStream inputStream = new BufferedInputStream(image.getInputStream()) ) {
            String imageExtension = ImageValidator.validateExtension(inputStream);
            String imageUUID = imageEntity.getImageUUID().toString();

            if (imageExtension == null) {
                throw new IllegalArgumentException("Uploaded image is not of valid format");
            }

            String imageFormat = "image/%s".formatted(imageExtension);
            String sourceImage = "unsafeSource.%s".formatted(imageExtension);
            String imageKey = "%s/%s".formatted(imageUUID, sourceImage);

            try {
                PutObjectRequest putRequest = PutObjectRequest.builder()
                    .checksumAlgorithm(ChecksumAlgorithm.CRC32)
                    .bucket(this.rustfsImagesBucket)
                    .contentType(imageFormat)
                    .key(imageKey)
                    .build();

                AsyncRequestBody requestBody = AsyncRequestBody.fromInputStream(
                    inputStream, image.getSize(), this.s3UploadExecutor
                );

                UploadRequest uploadRequest = UploadRequest.builder()
                    .putObjectRequest(putRequest)
                    .requestBody(requestBody)
                    .build();

                CompletedUpload upload = this.transferManager.upload(uploadRequest).completionFuture().join();
                ProcessingPipeline processingPipeline = new ProcessingPipeline(imageEntity);
                Object task = processingPipeline.initiateImageProcessing(sourceImage);
                pipelinesMap.put(imageEntity.getImageUUID(), processingPipeline);

                this.rabbitMQ.convertAndSend(this.imagesExchange, this.imagesTasksRoutingKey, task);
            } catch (CompletionException e) {
                System.err.println("S3 upload failed to complete successfully");
            } catch (ApiCallAttemptTimeoutException e) {
                System.err.println("S3 upload attempt timed out");
            } catch (ApiCallTimeoutException e) {
                System.err.println("S3 upload timed out");
            } catch (SdkClientException e) {
                System.err.println("S3 upload failed");
            } catch (S3Exception e) {
                if ("BadDigest".equals(e.awsErrorDetails().errorCode())) {
                    System.err.println("Checksum mismatch detected. S3 rejected the file.");
                    //TODO: request reupload;
                }
                System.err.println("S3 failed");
            } catch (Exception e) {
                System.err.println("failed");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to process image input stream", e);
        }
    }

    @RabbitListener(queues = "${rabbitmq.images.tasks.successes.queue}", containerFactory = "imagesBrokerPrefetchContainerFactory")
    public void handleSharpSuccesses(ProcessorSuccess taskSuccessResponse) {
        try {
            ProcessingPipeline processingPipeline = pipelinesMap.get(taskSuccessResponse.imageUUID());

            if (processingPipeline == null) {
                throw new IllegalArgumentException();
            }

            List<Object> processingTasks = processingPipeline.advanceImageProcessing(taskSuccessResponse);
           
            if (processingTasks != null) {
                processingTasks.forEach(task -> {
                    this.rabbitMQ.convertAndSend(this.imagesExchange, this.imagesTasksRoutingKey, task);
                });
            }
        } catch (Exception e) {
            System.err.println("Failed to process success payload: " + e.getMessage());
            // Throwing the exception ensures the message is rejected and sent to the DLQ 
            // rather than being silently dropped or infinitely requeued.
            throw new AmqpRejectAndDontRequeueException("Listener failed, sent to DLQ", e);
            //throw new RuntimeException("Database update failed for image " + taskSuccessResponse.imageUUID(), e);
        }
    }

    @RabbitListener(queues = "${rabbitmq.images.tasks.failures.queue}", containerFactory = "imagesBrokerPrefetchContainerFactory")
    public void handleSharpFailures(ProcessorFailure taskFailureResponse) {
        try {
            ProcessingPipeline processingPipeline = pipelinesMap.get(taskFailureResponse.imageUUID());

            if (processingPipeline == null) {
                throw new IllegalArgumentException();
            }

            Object task = processingPipeline.resolveFailure(taskFailureResponse);
            this.rabbitMQ.convertAndSend(this.imagesExchange, this.imagesTasksRoutingKey, task);
        } catch (Exception e) {
            System.err.println("Failed to process failures payload: " + e.getMessage());
            // Throwing the exception ensures the message is rejected and sent to the DLQ 
            // rather than being silently dropped or infinitely requeued.
            throw new AmqpRejectAndDontRequeueException("Listener failed, sent to DLQ", e);
            //throw new RuntimeException("Database update failed for image " + taskFailureResponse.imageUUID(), e);
        }
    }

    @RabbitListener(queues = "${rabbitmq.images.dlq.queue}", containerFactory = "imagesBrokerPrefetchContainerFactory")
    public void handleDeadLetter(Message failedMessage) {
        try {
            // 1. Extract raw payload safely
            String rawPayload = new String(failedMessage.getBody(), StandardCharsets.UTF_8);

            // 2. Extract RabbitMQ's automated death headers (why it was rejected)
            Map<String, Object> headers = failedMessage.getMessageProperties().getHeaders();
            Object deathHeader = headers.get("x-death");

            System.err.println("[CRITICAL] Unhandled task routed to DLQ.");
            System.err.println("Payload: " + rawPayload);

            if (deathHeader != null) {
                System.err.println("Death details: " + deathHeader.toString());
            }
        } catch (Exception e) {
            /* 
            * TODO: Implement persistent storage or alerting here.
            * Do not throw exceptions in this method. Save the rawPayload to a 
            * PostgreSQL table (e.g., 'system_anomalies') or send an alert via Slack/Email 
            * so a developer can manually inspect the payload and debug the Sharp worker.
            */
        }
    }
}
