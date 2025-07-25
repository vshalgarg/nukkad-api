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
//    private static final String ENV_VARIABLE = "FIREBASE_CREDENTIAL_PATH";

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

//    @PostConstruct
//    public void init() {
//        String firebaseConfigPath = System.getenv(ENV_VARIABLE);
//        log.info("Initializing Firebase using environment variable: {}", ENV_VARIABLE);
//        log.debug("Firebase config file path: {}", firebaseConfigPath);
//
//        if (firebaseConfigPath == null || firebaseConfigPath.isEmpty()) {
//            log.error("Environment variable '{}' not set or empty!", ENV_VARIABLE);
//            return;
//        }
//
//        try (FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath)) {
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//
//            if (FirebaseApp.getApps().isEmpty()) {
//                FirebaseApp.initializeApp(options);
//                log.info(" Firebase initialized successfully with project ID: {}",
//                        options.getProjectId() != null ? options.getProjectId() : "N/A");
//            } else {
//                log.warn("⚠️ Firebase already initialized, skipping reinitialization.");
//            }
//
//        } catch (IOException e) {
//            log.error("Failed to initialize Firebase: {}", e.getMessage(), e);
//        }
//    }


}
