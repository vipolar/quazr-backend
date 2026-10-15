package io.quazr.backend.Users.Images.Processing;

import io.quazr.backend.Users.Images.UserImageEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProcessingPipeline {
    private AtomicBoolean imageDownscalingTasksFired;
    private AtomicBoolean imageFormattingTasksFired;
    private AtomicBoolean imageProcessingTaskFired;
    private AtomicBoolean imageVerificationTaskFired;

    private final Map<String, List<ProcessorFailure>> retryTracker;
    private final UserImageEntity imageEntity;
    private String verifiedSourceImageFile;
    private String unsafeSourceImageFile;
    private String processedImageFile;

    public ProcessingPipeline(UserImageEntity imageEntity) {
        this.imageDownscalingTasksFired = new AtomicBoolean(false);
        this.imageFormattingTasksFired = new AtomicBoolean(false);
        this.imageProcessingTaskFired = new AtomicBoolean(false);
        this.imageVerificationTaskFired = new AtomicBoolean(false);

        this.retryTracker = new HashMap<String, List<ProcessorFailure>>();
        this.imageEntity = imageEntity;
    }

    public Object initiateImageProcessing(String targetFile) throws IllegalArgumentException {
        this.unsafeSourceImageFile = targetFile;
        if (this.imageVerificationTaskFired.compareAndSet(false, true)) {
            return constructImageTask("ImageVerificationTask", "image.png", null);
        } else {
            throw new IllegalCallerException("Image processing pipeline cannot be initiated more than once");
        }
    }

    public List<Object> advanceImageProcessing(ProcessorSuccess successResponse) throws IllegalArgumentException {
        final String taskOutputFile = successResponse.outputFile();
        final String successfulTask = successResponse.task();

        switch (successfulTask) {
            case "ImageVerificationTask":
                this.verifiedSourceImageFile = taskOutputFile;
                if (this.imageProcessingTaskFired.compareAndSet(false, true)) {
                    return List.of(
                        constructImageTask("ImageProcessingTask", "processedImage.png", null)
                    );
                }
            case "ImageProcessingTask":
                this.processedImageFile = taskOutputFile;
                if (this.imageFormattingTasksFired.compareAndSet(false, true)) {
                    return List.of(
                        constructImageTask("ImageFormattingTask", "processedImage_100.webp", null),
                        constructImageTask("ImageFormattingTask", "processedImage_80l.webp", null)
                    );
                }
            case "ImageFormattingTask":
                if (this.imageDownscalingTasksFired.compareAndSet(false, true)) {
                    return List.of(
                        constructImageTask("ImageDownscalingTask", "processedImage_256x256_100.png", null),
                        constructImageTask("ImageDownscalingTask", "processedImage_128x128_100.png", null)
                    );
                }
            case "ImageDownscalingTask":
                return null; //TODO: delete source?
            default:
                return null;
        }
    }

    private Object constructImageTask(String taskClass, String outputTarget, ImageTaskOptions options) throws IllegalArgumentException {
        OutputFormat format;

        switch (taskClass) {
            case "ImageVerificationTask":
                if (outputTarget.equals("image.png")) {
                    return new ImageVerificationTask(this.imageEntity, this.unsafeSourceImageFile, outputTarget, options);
                }
            case "ImageProcessingTask":
                if (outputTarget.equals("processedImage.png")) {
                    return new ImageProcessingTask(this.imageEntity, this.verifiedSourceImageFile, outputTarget, options);
                }
            case "ImageFormattingTask":
                switch (outputTarget) {
                    case "processedImage_100.webp":
                        format = OutputFormat.builder().format("webp").quality(100).lossless(true).build();
                        return new ImageFormattingTask(this.imageEntity, this.processedImageFile, format, outputTarget, options);
                    case "processedImage_80l.webp":
                        format = OutputFormat.builder().format("webp").quality(80).lossless(false).build();
                        return new ImageFormattingTask(this.imageEntity, this.processedImageFile, format, outputTarget, options);
                    default: break;
                }
            case "ImageDownscalingTask":
                switch (outputTarget) {
                    case "processedImage_256x256_100.png":
                        format = OutputFormat.builder().format("png").quality(100).build();
                        return new ImageDownscalingTask(this.imageEntity, this.processedImageFile, 256, 256, format, outputTarget, options);
                    case "processedImage_128x128_100.png":
                        format = OutputFormat.builder().format("png").quality(100).build();
                        return new ImageDownscalingTask(this.imageEntity, this.processedImageFile, 128, 128, format, outputTarget, options);
                    default: break;
                }
            default: break;
        }

        throw new IllegalArgumentException("Image task '%s -> %s' is not supported".formatted(taskClass, outputTarget));
    }

    public Object resolveFailure(ProcessorFailure failureResponse) {
        final String failureMessage = failureResponse.failureMessage();
        final Object failureCause = failureResponse.failureCause();
        final Integer failureCode = failureResponse.failureCode();
        final String outputFile = failureResponse.outputFile();
        final String failedTask = failureResponse.task();

        List<ProcessorFailure> failedAttempts = this.retryTracker.get(outputFile);
        Boolean ignoreCached = false; Boolean doNotCache = false;

        if (failedAttempts == null) {
            failedAttempts = new ArrayList<ProcessorFailure>();
            this.retryTracker.put(outputFile, failedAttempts);
        }

        failedAttempts.add(failureResponse);
        if (failedAttempts.size() > 3) {
            throw new IllegalArgumentException("No more retry attempts");
        }

        if (failureCode < 10000) {
            // task creation error
        } else if (failureCode < 20000) {
            final Integer downloadException = failureCode - 10000;
            switch (downloadException) {
                case 403:
                    // access error???
                    break;
                case 404:
                    // image not found???
                    break;
                case 408:
                    // timeout error (retry)
                    break;
                case 409:
                    // invalid s3 client state error
                    break;
                case 423:
                    // credentials provider error
                    break;
                case 501:
                    // bucket not found
                    break;
                default:
                    break;
            }
        } else if (failureCode < 30000) {
            final Integer sharpException = failureCode - 20000;
            if (sharpException == 0) {
                throw new RuntimeException(failureMessage);
            } else if (sharpException < 2000) {
                // sharp didn't init
            } else if (sharpException < 3000) {
                // processing stuff broke
            } else if (sharpException < 4000) {
                // formatting stuff broke
            } else if (sharpException < 5000) {
                // out broke on sharp output
            } else {
                // whatever else
            }

            if (sharpException % 2 != 0) {
                ignoreCached = true;
            }
        } else if (failureCode < 40000) {
            doNotCache = true;
        } else if (failureCode < 50000) {
            final Integer uploadException = failureCode - 40000;

            switch (uploadException) {
                case 403:
                    // access error???
                    break;
                case 408:
                    // timeout error (retry)
                    break;
                case 409:
                    // invalid s3 client state error
                    break;
                case 413:
                    // entity too large to upload (how?)
                    break;
                case 423:
                    // credentials provider error
                    break;
                case 501:
                    // bucket not found
                    break;
                default:
                    break;
            }
        }

        final ImageTaskOptions options = ImageTaskOptions.builder().ignoreCached(ignoreCached).doNotCache(doNotCache).build();
        return constructImageTask(failedTask, outputFile, options);
    }

    private static record ImageVerificationTask(String task, UUID imageUUID, String sourceFile, OutputFormat format, String outputFile, ImageTaskOptions options) {    
        private ImageVerificationTask(UserImageEntity imageEntity, String sourceFile, String outputFile, ImageTaskOptions options) {
            this("ImageVerificationTask", imageEntity.getImageUUID(), sourceFile, OutputFormat.builder().format("png").quality(100).build(), outputFile, options);
        }
    }

    private static record ImageFormattingTask(String task, UUID imageUUID, String sourceFile, OutputFormat format, String outputFile, ImageTaskOptions options) {    
        private ImageFormattingTask(UserImageEntity imageEntity, String sourceFile, OutputFormat format, String outputFile, ImageTaskOptions options) {
            this("ImageFormattingTask", imageEntity.getImageUUID(), sourceFile, format, outputFile, options);
        }
    }

    private static record ImageDownscalingTask(String task, UUID imageUUID, String sourceFile, Integer scaledWidth, Integer scaledHeight, OutputFormat format, String outputFile, ImageTaskOptions options) {    
        private ImageDownscalingTask(UserImageEntity imageEntity, String sourceFile, Integer scaledWidth, Integer scaledHeight, OutputFormat format, String outputFile, ImageTaskOptions options) {
            this("ImageDownscalingTask", imageEntity.getImageUUID(), sourceFile, scaledWidth, scaledHeight, format, outputFile, options);
        }
    }

    private static record ImageProcessingTask(String task, UUID imageUUID, String sourceFile, Integer scaledWidth, Integer scaledHeight, Double angleInRadians, Integer extractLeft, Integer extractTop, Integer extractWidth, Integer extractHeight, OutputFormat format, String outputFile, ImageTaskOptions options) {
        private ImageProcessingTask(UserImageEntity imageEntity, String sourceFile, String outputFile, ImageTaskOptions options) {
            this("ImageProcessingTask", imageEntity.getImageUUID(), sourceFile, imageEntity.getScaledWidth(), imageEntity.getScaledHeight(), imageEntity.getAngleInRadians(), imageEntity.getExtractLeft(), imageEntity.getExtractTop(), imageEntity.getExtractWidth(), imageEntity.getExtractHeight(), OutputFormat.builder().format("png").quality(100).build(), outputFile, options);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static record OutputFormat(String format, String tune, Integer colors, Integer quality, Integer bitdepth, Boolean lossless, Boolean progressive, Boolean nearLossless, Integer compressionLevel, String chromaSubsampling, Boolean adaptiveFiltering, Boolean smartSubsample, Boolean smartDeblock, Integer alphaQuality, Boolean palette, Double dither, String preset, Boolean exact, Integer effort, Boolean force) {
        private static Builder builder() {
            return new Builder();
        }

        private static final class Builder {
            private Builder() {}

            private String format;
            private Builder format(String format) {
                this.format = format;
                return this;
            }

            private String tune;
            private Builder tune(String tune) {
                this.tune = tune;
                return this;
            }

            private Integer colors;
            private Builder colors(Integer colors) {
                this.colors = colors;
                return this;
            }

            private Integer quality;
            private Builder quality(Integer quality) {
                this.quality = quality;
                return this;
            }

            private Integer bitdepth;
            private Builder bitdepth(Integer bitdepth) {
                this.bitdepth = bitdepth;
                return this;
            }

            private Boolean lossless;
            private Builder lossless(Boolean lossless) {
                this.lossless = lossless;
                return this;
            }

            private Boolean progressive;
            private Builder progressive(Boolean progressive) {
                this.progressive = progressive;
                return this;
            }

            private Boolean nearLossless;
            private Builder nearLossless(Boolean nearLossless) {
                this.nearLossless = nearLossless;
                return this;
            }

            private Integer compressionLevel;
            private Builder compressionLevel(Integer compressionLevel) {
                this.compressionLevel = compressionLevel;
                return this;
            }

            private String chromaSubsampling;
            private Builder chromaSubsampling(String chromaSubsampling) {
                this.chromaSubsampling = chromaSubsampling;
                return this;
            }

            private Boolean adaptiveFiltering;
            private Builder adaptiveFiltering(Boolean adaptiveFiltering) {
                this.adaptiveFiltering = adaptiveFiltering;
                return this;
            }

            private Boolean smartSubsample;
            private Builder smartSubsample(Boolean smartSubsample) {
                this.smartSubsample = smartSubsample;
                return this;
            }

            private Boolean smartDeblock;
            private Builder smartDeblock(Boolean smartDeblock) {
                this.smartDeblock = smartDeblock;
                return this;
            }

            private Integer alphaQuality;
            private Builder alphaQuality(Integer alphaQuality) {
                this.alphaQuality = alphaQuality;
                return this;
            }

            private Boolean palette;
            private Builder palette(Boolean palette) {
                this.palette = palette;
                return this;
            }

            private Integer effort;
            private Builder effort(Integer effort) {
                this.effort = effort;
                return this;
            }

            private Double dither;
            private Builder dither(Double dither) {
                this.dither = dither;
                return this;
            }

            private String preset;
            private Builder preset(String preset) {
                this.preset = preset;
                return this;
            }

            private Boolean exact;
            private Builder exact(Boolean exact) {
                this.exact = exact;
                return this;
            }

            private Boolean force;
            private Builder force(Boolean force) {
                this.force = force;
                return this;
            }

            private OutputFormat build() {
                return new OutputFormat(format, tune, colors, quality, bitdepth, lossless, progressive, nearLossless, compressionLevel, chromaSubsampling, adaptiveFiltering, smartSubsample, smartDeblock, alphaQuality, palette, dither, preset, exact, effort, force);
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static record ImageTaskOptions(Boolean ignoreCached, Boolean doNotCache) {
        private static Builder builder() {
            return new Builder();
        }

        private static final class Builder {
            private Builder() {}

            private Boolean ignoreCached;
            private Builder ignoreCached(Boolean ignoreCached) {
                this.ignoreCached = ignoreCached;
                return this;
            }

            private Boolean doNotCache;
            private Builder doNotCache(Boolean doNotCache) {
                this.doNotCache = doNotCache;
                return this;
            }

            private ImageTaskOptions build() {
                return new ImageTaskOptions(ignoreCached, doNotCache);
            }
        }
    }
}
