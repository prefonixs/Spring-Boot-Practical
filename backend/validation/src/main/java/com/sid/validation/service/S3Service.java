package com.sid.validation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URL;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.bucket-name}")
    private String bucketName;

    public S3Service(@Value("${aws.access-key}") String accessKey,
                     @Value("${aws.secret-key}") String secretKey,
                     @Value("${aws.region}") String region) {

        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .build(),
                RequestBody.fromBytes(file.getBytes()));

        return getFileUrl(fileName).toString();
    }

    public URL getFileUrl(String fileName) {
        return s3Client.utilities().getUrl(GetUrlRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build());
    }
    
//    public String generatePresignedUrl(String fileName, Duration expirationTime) {
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket(bucketName)
//                .key(fileName)
//                .build();
//
//        return s3Client.utilities().getPresignedUrl(GetPresignedUrlRequest.builder()
//                .signatureDuration(expirationTime)
//                .getObjectRequest(getObjectRequest)
//                .build()).toString();
//    }

    public void deleteFile(String fileUrl) {
    	System.out.println(fileUrl);
    	String fileName = extractFilename(fileUrl);
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build());
    }
    
    public static String extractFilename(String fileUrl) {
        try {
            @SuppressWarnings("deprecation")
			URL url = new URL(fileUrl);
            String path = url.getPath();
            return path.substring(path.lastIndexOf('/') + 1); // Extracts the last part of the path
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
