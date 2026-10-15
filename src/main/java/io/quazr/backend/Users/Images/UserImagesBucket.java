package io.quazr.backend.Users.Images;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class UserImagesBucket {
    @Value("${rustfs.bucket.name}")
    private String rustfsImagesBucket;
    
    public String getRustfsImagesBucket() {
        return this.rustfsImagesBucket;
    }
}
