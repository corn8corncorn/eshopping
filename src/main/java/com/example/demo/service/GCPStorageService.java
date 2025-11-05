package com.example.demo.service;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

/**
 * GCP Cloud Storage 服務
 * 負責處理檔案上傳到 Google Cloud Storage
 */
@Service
public class GCPStorageService {

    private static final Logger logger = LoggerFactory.getLogger(GCPStorageService.class);

    @Value("${gcp.storage.bucket-name:eshopping-images}")
    private String bucketName;

    @Value("${gcp.storage.project-id:}")
    private String projectId;

    @Value("${gcp.storage.credentials-path:}")
    private String credentialsPath;

    /**
     * 上傳圖片到 GCP Cloud Storage
     * 
     * @param file 上傳的檔案
     * @param folder 儲存資料夾（例如：products）
     * @return 上傳後的公開 URL
     * @throws IOException 當檔案讀取或上傳失敗時
     */
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            logger.warn("上傳的檔案為空");
            return null;
        }

        // 驗證檔案類型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("只能上傳圖片檔案");
        }

        // 生成檔名
        String originalFilename = file.getOriginalFilename();
        String fileName;
        
        if (originalFilename != null && !originalFilename.isEmpty()) {
            // 保留原始檔名，但確保安全（移除路徑分隔符）
            fileName = originalFilename.replaceAll("[/\\\\]", "_");
        } else {
            // 如果沒有原始檔名，使用 UUID 生成
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            fileName = UUID.randomUUID().toString() + extension;
        }
        
        // 如果指定了 folder，則加上資料夾前綴；否則直接放在根目錄
        String uniqueFileName = (folder != null && !folder.isEmpty()) ? folder + "/" + fileName : fileName;

        logger.info("開始上傳圖片到 GCP - bucket: {}, fileName: {}, contentType: {}", 
                    bucketName, uniqueFileName, contentType);

        try {
            Storage storage = createStorageClient();

            BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, uniqueFileName))
                .setContentType(contentType)
                .setCacheControl("public, max-age=3600")
                .build();

            storage.create(blobInfo, file.getBytes());

            String gcsPublicUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, uniqueFileName);
            logger.info("圖片上傳成功 - fileName: {}, publicUrl: {}", uniqueFileName, gcsPublicUrl);
            
            return gcsPublicUrl;

        } catch (Exception e) {
            logger.error("上傳圖片到 GCP 失敗 - fileName: {}", uniqueFileName, e);
            throw new IOException("上傳圖片失敗：" + e.getMessage(), e);
        }
    }

    /**
     * 刪除 GCP Cloud Storage 中的圖片
     * 
     * @param imageUrl 圖片 URL
     * @return 是否刪除成功
     */
    public boolean deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return false;
        }

        try {
            // 從 URL 中提取檔案名稱
            String fileName = imageUrl.replace("https://storage.googleapis.com/" + bucketName + "/", "");
            
            logger.info("開始刪除圖片 - fileName: {}", fileName);

            Storage storage = createStorageClient();
            boolean deleted = storage.delete(BlobId.of(bucketName, fileName));
            
            if (deleted) {
                logger.info("圖片刪除成功 - fileName: {}", fileName);
            } else {
                logger.warn("圖片不存在或刪除失敗 - fileName: {}", fileName);
            }
            
            return deleted;

        } catch (Exception e) {
            logger.error("刪除圖片失敗 - imageUrl: {}", imageUrl, e);
            return false;
        }
    }

    /**
     * 建立 Storage 客戶端
     */
    private Storage createStorageClient() throws IOException {
        StorageOptions.Builder builder = StorageOptions.newBuilder();

        if (projectId != null && !projectId.isEmpty()) {
            builder.setProjectId(projectId);
        }

        if (credentialsPath != null && !credentialsPath.isEmpty()) {
            try {
                GoogleCredentials credentials;
                
                if (credentialsPath.startsWith("classpath:")) {
                    String resourcePath = credentialsPath.replace("classpath:", "");
                    Resource resource = new ClassPathResource(resourcePath);
                    credentials = GoogleCredentials.fromStream(resource.getInputStream());
                    logger.info("從 classpath 載入 GCP 認證檔案: {}", resourcePath);
                } else {
                    java.io.File credentialsFile = new java.io.File(credentialsPath);
                    if (!credentialsFile.exists()) {
                        throw new IOException("GCP 認證檔案不存在: " + credentialsPath);
                    }
                    credentials = GoogleCredentials.fromStream(
                        new java.io.FileInputStream(credentialsFile));
                    logger.info("從檔案系統載入 GCP 認證檔案: {}", credentialsPath);
                }
                
                builder.setCredentials(credentials);
            } catch (Exception e) {
                logger.warn("無法從指定路徑載入 GCP 認證檔案: {}, 將使用預設認證方式", credentialsPath, e);
            }
        }

        return builder.build().getService();
    }
}

