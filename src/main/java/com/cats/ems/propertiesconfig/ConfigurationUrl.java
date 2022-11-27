package com.cats.ems.propertiesconfig;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import lombok.Data;

@Configuration
@PropertySource("classpath:authConfig.properties")
@PropertySource("classpath:swagger.properties")
@ConfigurationProperties(prefix = "jwt")
@Data
@Service
public class ConfigurationUrl {
    private String key;
    private String initVector;
    private String algorithm;
    private String[] authUrl;
    private List<String> tokenUrl;
    
   
}