package com.code.monks.nukkad.utils;

import com.code.monks.nukkad.exception.UnhandledException;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import static com.code.monks.nukkad.enums.ResponseErrorCodes.FAILED_TO_DELETE_FILE_FROM_FIREBASE;
import static com.code.monks.nukkad.enums.ResponseErrorCodes.FAILED_TO_UPLOAD_FILE_TO_FIREBASE;

@Component
@Slf4j
public class FirebaseFileUploadHelper {

    public String uploadFile(MultipartFile file, String folder) {
        try {
            String fileName = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());

            // Manually build Firebase-style public download URL
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20");

            return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                    bucket.getName(), encodedFileName);

        } catch (IOException e) {
            throw new UnhandledException(FAILED_TO_UPLOAD_FILE_TO_FIREBASE, e);
        }
    }

    public void deleteFile(String fileUrl) {
        try {
            Bucket bucket = StorageClient.getInstance().bucket();

            // Extract object name from the Firebase URL
            String objectName = fileUrl.substring(fileUrl.indexOf("/o/") + 3, fileUrl.indexOf("?alt="));
            objectName = URLDecoder.decode(objectName, StandardCharsets.UTF_8.name());

            Blob blob = bucket.get(objectName);
            if (blob != null) {
                blob.delete();
            }
        } catch (Exception e) {
            log.error("[QR DELETE] Unexpected error during Firebase deletion for URL :", e);
            log.error("[QR DELETE] Failed to delete QR image from Firebase, aborting delete: ", e);

            throw new UnhandledException(FAILED_TO_DELETE_FILE_FROM_FIREBASE, e);
        }
    }
}