package com.telegrambot.marketplace.service.s3;

import java.util.List;

public interface S3Service {
    String uploadFile(String name, byte[] photo);
    List<String> uploadFiles(List<String> name, List<byte[]> photos);
    List<String> getBucketFilesUrls();
    void deleteFiles(List<String> urls);

    void deleteFile(String url);
    String privatePathToPublicPath(String url);
}
