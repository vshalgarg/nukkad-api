package com.code.monks.nukkad.services;

import com.code.monks.nukkad.exception.ExternalServiceException;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.FIREBASE_UPLOAD_FAILED;
import static com.code.monks.nukkad.enums.ResponseErrorCodes.IMAGE_DOWNLOAD_FAILED;

@Service
@Slf4j
public class FirebaseStorageService {

    private final RestTemplate restTemplate;

    public FirebaseStorageService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .requestFactory(() -> {
                    SimpleClientHttpRequestFactory factory =
                            new SimpleClientHttpRequestFactory();
                    // connect timeout = 5 seconds
                    factory.setConnectTimeout(5000);
                    // read timeout = 15 seconds
                    factory.setReadTimeout(15000);
                    return factory;
                })
                .build();
    }
    public String uploadImageFromUrl(String imageUrl, String folder) {
        Path tempFile = null;

        try {
            log.info("[FIREBASE STORAGE] Downloading image from: {}", imageUrl);

            byte[] imageBytes = restTemplate.getForObject(
                    imageUrl, byte[].class);
            if (imageBytes == null || imageBytes.length == 0) {
                throw new ExternalServiceException(
                        IMAGE_DOWNLOAD_FAILED,
                        new RuntimeException(
                                "Image download failed or empty: " + imageUrl)
                );
            }
            tempFile = Files.createTempFile(
                    "firebase_" + UUID.randomUUID(), ".tmp");
            Files.write(tempFile, imageBytes);

            log.info("[FIREBASE STORAGE] Temp file created: {}", tempFile);

            Bucket bucket = StorageClient.getInstance().bucket();
            String firebasePath = folder + "/" + UUID.randomUUID() + ".jpg";

            String contentType = URLConnection
                    .guessContentTypeFromName(imageUrl);
            if (contentType == null) {
                // fallback to jpeg if detection fails
                contentType = "image/jpeg";
            }

            try (InputStream inputStream =
                         new FileInputStream(tempFile.toFile())) {

                Blob blob = bucket.create(
                        firebasePath, inputStream, contentType);

                String firebaseUrl = String.format(
                        "https://storage.googleapis.com/%s/%s",
                        bucket.getName(),
                        blob.getName()
                );

                log.info("[FIREBASE STORAGE] Upload successful. URL: {}",
                        firebaseUrl);
                return firebaseUrl;
            }

        } catch (ExternalServiceException e) {

            throw e;

        } catch (Exception e) {
            log.error("[FIREBASE STORAGE] Failed to upload from URL: {}",
                    imageUrl, e);

            throw new ExternalServiceException(
                    FIREBASE_UPLOAD_FAILED, e);

        } finally {
            // always delete temp file — success or failure
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                    log.info("[FIREBASE STORAGE] Temp file deleted: {}",
                            tempFile);
                } catch (Exception e) {
                    log.warn("[FIREBASE STORAGE] Failed to delete temp file: {}",
                            tempFile, e);
                }
            }
        }
    }
}