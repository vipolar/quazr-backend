package io.quazr.backend.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class ImageBucketConfig {
    @Value("${rustfs.bucket.name}")
    private String rustfsImagesBucket;
    
    public String getRustfsImagesBucket() {
        return this.rustfsImagesBucket;
    }
}
