package com.code.monks.nukkad.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;


@Slf4j
@Configuration
public class FirebaseConfig {

    @Value("${firebase.credentials.path}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initFirebase() {
        try {
            InputStream serviceAccount = getClass()
                    .getClassLoader()
                    .getResourceAsStream(firebaseConfigPath.replace("classpath:", ""));

            if (serviceAccount == null) {
                log.error(" firebase-service-account.json not found at: {}", firebaseConfigPath);
                return;
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info(" Firebase initialized successfully!");
            }
        } catch (Exception e) {
            log.error(" Failed to initialize Firebase: {}", e.getMessage(), e);
        }
    }
}
