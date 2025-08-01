    package com.code.monks.nukkad.configuration;

    import com.google.auth.oauth2.GoogleCredentials;
    import com.google.firebase.FirebaseApp;
    import com.google.firebase.FirebaseOptions;
    import jakarta.annotation.PostConstruct;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Configuration;

    import java.io.File;
    import java.io.FileInputStream;


    @Slf4j
    @Configuration
    public class FirebaseConfig {

        @Value("${firebase.credentials.path}")
        private String firebaseConfigPath;

        @PostConstruct
        public void initFirebase() {

            log.info("FirebaseConfig loaded");
            log.info("Inside initFirebase() method");
            try {
                File configFile = new File(firebaseConfigPath);
                if (!configFile.exists()) {
                    log.error(" Firebase config file not found at: {}", firebaseConfigPath);
                    return;
                }

                FileInputStream serviceAccount = new FileInputStream(configFile);

    //            String path = firebaseConfigPath.replace("classpath:", "");
    //            InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream(path);
    //
    //            if (serviceAccount == null) {
    //                log.error(" Firebase config file not found in classpath at: {}", firebaseConfigPath);
    //                return;
    //            }

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