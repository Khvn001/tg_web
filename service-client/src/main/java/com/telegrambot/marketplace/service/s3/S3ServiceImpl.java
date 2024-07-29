package com.telegrambot.marketplace.service.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    @Value("${aws.s3.publicEndpoint}")
    private String s3PublicEndpoint;

    @Value("${aws.s3.privateEndpoint}")
    private String s3PrivateEndpoint;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.stuffPlaceFolder}")
    private String stuffPlaceFolder;

    @Value("${aws.s3.accessKey}")
    private String accessKey;

    @Value("${aws.s3.secretKey}")
    private String secretKey;


    @Override
    public String uploadFile(final String name, final byte[] photo) {
        AmazonS3 s3Client = initializeS3Client();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(photo.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(photo);
        s3Client.putObject(bucketName, name, inputStream, metadata);
        log.info("Upload Service. Added file: " + name + " to bucket: " + bucketName);

        // Получение ссылки на загруженный файл
        return s3Client.getUrl(bucketName, name).toExternalForm();
    }

    @Override
    public List<String> uploadFiles(final List<String> name, final List<byte[]> photos) {
        List<String> urls = new ArrayList<>();

        AmazonS3 s3Client = initializeS3Client();

        try {
            ExecutorService executorService = Executors.newFixedThreadPool(photos.size());
            List<Future<String>> futures = new ArrayList<>();

            for (int fileNumber = 0; fileNumber < photos.size(); fileNumber++) {
                int threadFileNumber = fileNumber;
                byte[] photo = photos.get(threadFileNumber);
                Future<String> future = executorService.submit(() -> {
                    String objectKey = name.get(threadFileNumber);
                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentLength(photo.length);

                    ByteArrayInputStream inputStream = new ByteArrayInputStream(photo);
                    s3Client.putObject(bucketName, objectKey, inputStream, metadata);
                    log.info("Upload Service. Added file: " + objectKey + " to bucket: " + bucketName);

                    // Получение ссылки на загруженный файл
                    return s3Client.getUrl(bucketName, objectKey).toExternalForm();
                });

                futures.add(future);
            }

            for (Future<String> future : futures) {
                try {
                    String url = future.get();
                    urls.add(url);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("One of the thread ended with exception. Reason: {}", e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            executorService.shutdown();
        } catch (AmazonS3Exception e) {
            log.error("Error uploading photos to Object Storage. Reason: {}", e.getMessage());
            throw new AmazonS3Exception(e.getMessage());
        }
        return urls;
    }

    @Override
    public List<String> getBucketFilesUrls() {
        List<String> urls = new ArrayList<>();
        AmazonS3 s3Client = initializeS3Client();
        ListObjectsV2Result result = s3Client.listObjectsV2(bucketName);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        objects = objects.stream().filter(os -> os.getKey().startsWith("stories")).toList();
        for (S3ObjectSummary os : objects) {
            String url = s3PublicEndpoint + "/" + bucketName + "/" + os.getKey();
            urls.add(url);
        }
        return urls;
    }

    @Override
    public void deleteFiles(final List<String> urls) {
        AmazonS3 s3Client = initializeS3Client();
        for (String url : urls) {
            s3Client
                    .deleteObject(new DeleteObjectRequest(bucketName,
                            url.replace(s3PublicEndpoint, "")
                                    .replace("/", "")
                                    .replace(bucketName, "")
                                    .replace("/", "")));
        }
    }

    @Override
    public void deleteFile(final String url) {
        AmazonS3 s3Client = initializeS3Client();
        s3Client
                .deleteObject(new DeleteObjectRequest(bucketName,
                        url.replace(s3PublicEndpoint, "")
                                .replace("/", "")
                                .replace(bucketName, "")
                                .replace("/", "")));
    }

    @Override
    public String privatePathToPublicPath(final String url) {
        return url.replace(s3PrivateEndpoint, s3PublicEndpoint);
    }

    private AmazonS3 initializeS3Client() {
        try {
            return AmazonS3ClientBuilder.standard()
                    .withEndpointConfiguration(
                            new com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration(
                                    s3PrivateEndpoint,
                                    "us-east-1"
                            )
                    )
                    .withCredentials(new AWSStaticCredentialsProvider(
                            new BasicAWSCredentials(accessKey, secretKey))
                    )
                    .build();
        } catch (SdkClientException e) {
            log.error("Error creating client for Object Storage via AWS SDK. Reason: {}", e.getMessage());
            throw new SdkClientException(e.getMessage());
        }
    }
}
