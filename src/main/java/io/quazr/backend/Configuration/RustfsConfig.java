package io.quazr.backend.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.crt.S3CrtRetryConfiguration;
import software.amazon.awssdk.services.s3.crt.S3CrtHttpConfiguration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.retries.StandardRetryStrategy;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.regions.Region;

import java.time.Duration;
import java.net.URI;

@Configuration
public class RustfsConfig {
    @Value("${rustfs.url}")
    private String rustfsURL;

    @Value("${rustfs.access.key}")
    private String rustfsAccessKey;

    @Value("${rustfs.secret.key}")
    private String rustfsSecretKey;

    @Bean
    public S3Client rustfsS3Client() {
        return S3Client.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create(rustfsURL))
            .credentialsProvider(staticCredentialsProvider())
            .overrideConfiguration(ClientOverrideConfiguration.builder()
                .apiCallAttemptTimeout(Duration.ofSeconds(15))
                .apiCallTimeout(Duration.ofSeconds(90))
                .retryStrategy(StandardRetryStrategy.builder()
                    .maxAttempts(3)
                    .build())
                .build())
            .forcePathStyle(true) // RustFS requirement
            .build();
    }

    @Bean
    public S3AsyncClient s3AsyncClient() {
        return S3AsyncClient.crtBuilder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create(rustfsURL))
            .credentialsProvider(staticCredentialsProvider())
            .httpConfiguration(S3CrtHttpConfiguration.builder()
                .connectionTimeout(Duration.ofSeconds(90)) 
                .build())
            .retryConfiguration(S3CrtRetryConfiguration.builder()
                .numRetries(3)
                .build())
            .forcePathStyle(true) // RustFS requirement
            .build();
    }

    @Bean
    public S3TransferManager s3TransferManager(S3AsyncClient s3AsyncClient) {
        return S3TransferManager.builder()
            .s3Client(s3AsyncClient)
            .build();
    }

    private StaticCredentialsProvider staticCredentialsProvider() {
        AwsCredentials awsBasicCredentials = AwsBasicCredentials.create(rustfsAccessKey, rustfsSecretKey);
        return StaticCredentialsProvider.create(awsBasicCredentials);
    }
}
