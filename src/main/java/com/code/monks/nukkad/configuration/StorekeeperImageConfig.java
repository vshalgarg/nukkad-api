package com.code.monks.nukkad.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "storekeeper.image")
public class StorekeeperImageConfig {
    private String uploadPath;
}
