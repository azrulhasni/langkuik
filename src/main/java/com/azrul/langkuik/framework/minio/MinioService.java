/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.internal.http2.ErrorCode;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author azrul
 */
@Component
public class MinioService {

    @Value("${application.minio.url}")
    String minioUrl;

    @Value("${application.minio.bucket}")
    String minioBucket;

    @Value("${application.minio.access-key}")
    String minioAccessKey;

    @Value("${application.minio.secret-key}")
    String minioSecret;

    public MinioService() {
        try {
            if (minioUrl == null) {
                Properties config = new Properties();
                try (InputStream inputStream = MinioService.class
                        .getClassLoader()
                        .getResourceAsStream("application.properties")) {
                    config.load(inputStream);
                    
                    this.minioUrl = config.getProperty("application.minio.url");
                    this.minioBucket = config.getProperty("application.minio.bucket");
                    this.minioAccessKey = config.getProperty("application.minio.access-key");
                    this.minioSecret = config.getProperty("application.minio.secret-key");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(MinioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Boolean exist(String relativePath) {
        MinioClient minioClient
                = MinioClient.builder()
                        .endpoint(minioUrl)
                        .credentials(minioAccessKey, minioSecret)
                        .build();
        boolean found = false;
        try {
            minioClient.statObject(
                    StatObjectArgs
                            .builder()
                            .bucket(minioBucket)
                            .object(relativePath).build());
            found = true;
        } catch (ErrorResponseException e) {
            found = false;

        } catch (InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException ex) {
            Logger.getLogger(MinioService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return found;
    }

    public InputStream get(String relativePath) {
        try {
            //excluding bucket
            MinioClient minioClient
                    = MinioClient.builder()
                            .endpoint(minioUrl)
                            .credentials(minioAccessKey, minioSecret)
                            .build();
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder().bucket(minioBucket).object(relativePath).build()
            );
            File tempFile = File.createTempFile("langkuik", "");
            tempFile.deleteOnExit();
            FileUtils.copyInputStreamToFile(inputStream, tempFile);
            inputStream.close();
            return new FileInputStream(tempFile);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException ex) {
            Logger.getLogger(MinioService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void delete(String relativePath) {
        try {
            MinioClient minioClient
                    = MinioClient.builder()
                            .endpoint(minioUrl)
                            .credentials(minioAccessKey, minioSecret)
                            .build();
            minioClient.removeObject(
                    RemoveObjectArgs
                            .builder()
                            .bucket(minioBucket)
                            .object(relativePath)
                            .build()
            );
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException ex) {
            Logger.getLogger(MinioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Optional<String> save(InputStream is, String fileName, String folder) {
        try {
            //excluding bucket
            MinioClient minioClient
                    = MinioClient.builder()
                            .endpoint(minioUrl)
                            .credentials(minioAccessKey, minioSecret)
                            .build();

            String relativePath = folder + "/" + fileName;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioBucket)
                            .object(relativePath)
                            .stream(is, is.available(), -1)
                            .build());
            return Optional.of(relativePath);

        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException ex) {
            Logger.getLogger(MinioService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Optional.empty();
    }

    public Optional<String> save(File file, String folder) {
        try {
            //excluding bucket
            MinioClient minioClient
                    = MinioClient.builder()
                            .endpoint(minioUrl)
                            .credentials(minioAccessKey, minioSecret)
                            .build();

            String finalName = folder + "/" + file.getName();

            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(minioBucket)
                            .object(file.getAbsolutePath())
                            .filename(finalName)
                            .build());
            return Optional.of(finalName);

        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException ex) {
            Logger.getLogger(MinioService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Optional.empty();
    }

}
