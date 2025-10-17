package com.btea.aurorabackendassessmentuploadsite.commom.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.btea.aurorabackendassessmentuploadsite.commom.config.AliyunConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: TwentyFiveBTea
 * @Date: 2025/10/17 18:03
 * @Description: 阿里云 OSS 工具类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunOssUtil {

    private final AliyunConfig aliyunConfig;

    /**
     * 创建阿里云客户端
     *
     * @return 阿里云客户端
     */
    public OSS createClient() {
        return new OSSClientBuilder().build(aliyunConfig.getEndpoint(), aliyunConfig.getAccessKeyId(), aliyunConfig.getAccessKeySecret());
    }

    /**
     * 上传文件到阿里云OSS
     *
     * @param file     要上传的文件
     * @param filePath 文件在OSS中的存储路径
     * @return 文件访问URL，如果上传失败则返回null
     */
    public String uploadFile(MultipartFile file, String filePath) {
        OSS ossClient = createClient();
        try {
            log.info("开始上传文件到OSS，Bucket: {}, 文件路径: {}", aliyunConfig.getBucketName(), filePath);

            // 获取文件输入流
            InputStream inputStream = file.getInputStream();
            log.info("文件名: {}, 文件大小: {} bytes", file.getOriginalFilename(), file.getSize());

            // 创建上传请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(aliyunConfig.getBucketName(), filePath, inputStream);
            log.info("创建上传请求完成");

            // 上传文件
            ossClient.putObject(putObjectRequest);
            log.info("文件上传到OSS完成");

            // 构造文件访问URL
            String fileUrl = "https://" + aliyunConfig.getBucketName() + "." + aliyunConfig.getEndpoint() + "/" + filePath;

            log.info("文件上传成功，文件访问URL: {}", fileUrl);
            return fileUrl;
        } catch (Exception e) {
            log.error("文件上传失败，Bucket: {}, 文件路径: {}", aliyunConfig.getBucketName(), filePath, e);
            return null;
        } finally {
            // 关闭客户端
            if (ossClient != null) {
                ossClient.shutdown();
                log.info("OSS客户端已关闭");
            }
        }
    }

    /**
     * 批量上传java文件到阿里云OSS（带用户姓名）
     *
     * @param files  要上传的java文件列表
     * @param folder 文件在OSS中的存储文件夹
     * @param name   用户姓名
     * @return 文件访问URL列表，如果上传失败则返回null
     */
    public List<String> batchUploadJavaFilesWithName(List<MultipartFile> files, String folder, String name) {
        List<String> fileUrls = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                // 检查文件扩展名
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".java")) {
                    log.warn("文件不是java格式: {}", originalFilename);
                    continue; // 跳过非java文件
                }

                // 构造带用户姓名的文件路径
                String fileName = originalFilename;
                String filePath = name + "/" + fileName;

                // 上传文件
                String fileUrl = uploadFile(file, filePath);

                if (fileUrl != null) {
                    fileUrls.add(fileUrl);
                    log.info("java文件上传成功: {}", fileName);
                } else {
                    log.error("java文件上传失败: {}", fileName);
                }
            }

            log.info("批量上传java文件完成，成功上传 {} 个文件", fileUrls.size());
            return fileUrls;
        } catch (Exception e) {
            log.error("批量上传java文件时发生异常", e);
            return null;
        }
    }
}
