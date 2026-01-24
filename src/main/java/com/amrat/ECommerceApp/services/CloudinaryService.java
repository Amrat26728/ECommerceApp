package com.amrat.ECommerceApp.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Async
    public CompletableFuture<List<String>> uploadFilesAsync(List<byte[]> files, List<byte[]> primaryImage, String folder) {
        System.out.println("Inside uploadFilesAsync");
        List<String> urls = uploadFiles(files, folder);
        return CompletableFuture.completedFuture(urls);
    }

    public List<String> uploadFiles(List<byte[]> files, String folder) {
        System.out.println("Inside uploadFiles start "+files.size());
        return files.stream()
                .map(file -> uploadFile(file, folder))
                .toList();
    }

    public String uploadFile(byte[] file, String folder) {
        try {
            System.out.println("Inside uploadFile start");
            Map<String, Object> options = Map.of(
                    "folder", folder,
                    "public_id", UUID.randomUUID().toString(),
                    "resource_type", "image"
            );

            Map<?, ?> uploadResult = cloudinary.uploader()
                    .upload(file, options);
            System.out.println("Inside uploadFile end");
            return uploadResult.get("secure_url").toString();

        } catch (Exception ex) {
            throw new RuntimeException("Failed to upload image to Cloudinary", ex);
        }
    }


}
