package com.btea.aurorabackendassessmentuploadsite.controller;

import com.btea.aurorabackendassessmentuploadsite.commom.convention.result.Result;
import com.btea.aurorabackendassessmentuploadsite.commom.convention.result.Results;
import com.btea.aurorabackendassessmentuploadsite.commom.util.AliyunOssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: TwentyFiveBTea
 * @Date: 2025/10/17 18:13
 * @Description: 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin
public class uploadController {

    private final AliyunOssUtil aliyunOssUtil;

    /**
     * 上传docx文件
     *
     * @param file   docx文件
     * @param folder 存储文件夹
     * @param name   用户姓名
     * @return 文件访问URL
     */
    @PostMapping("/upload/docx")
    public Result<String> uploadDocx(@RequestParam("file") MultipartFile file,
                                     @RequestParam(value = "folder", defaultValue = "docx") String folder,
                                     @RequestParam(value = "name", required = false) String name) {
        try {
            log.info("开始上传docx文件: {}, 存储文件夹: {}, 用户姓名: {}", file.getOriginalFilename(), folder, name);

            // 检查姓名是否为空
            if (name == null || name.trim().isEmpty()) {
                return Results.failure("请提供用户姓名");
            }

            // 检查文件扩展名
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".docx")) {
                return Results.failure("文件不是docx格式");
            }

            // 构造文件路径，包含用户姓名
            String fileName = originalFilename;
            String filePath = name + "/" + fileName;

            // 上传文件
            String fileUrl = aliyunOssUtil.uploadFile(file, filePath);

            if (fileUrl != null) {
                log.info("docx文件上传成功，文件访问URL: {}", fileUrl);
                return Results.success(fileUrl);
            } else {
                log.error("docx文件上传失败");
                return Results.failure("文件上传失败");
            }
        } catch (Exception e) {
            log.error("上传docx文件时发生异常", e);
            return Results.failure("上传过程中发生错误: " + e.getMessage());
        }
    }

    /**
     * 批量上传java文件
     *
     * @param files  java文件列表
     * @param folder 存储文件夹
     * @param name   用户姓名
     * @return 文件访问URL列表
     */
    @PostMapping("/upload/java")
    public Result<List<String>> batchUploadJava(@RequestParam("files") MultipartFile[] files,
                                                @RequestParam(value = "folder", defaultValue = "java") String folder,
                                                @RequestParam(value = "name", required = false) String name) {
        try {
            log.info("开始批量上传java文件，文件数量: {}, 存储文件夹: {}, 用户姓名: {}", files.length, folder, name);

            // 检查姓名是否为空
            if (name == null || name.trim().isEmpty()) {
                return Results.failure("请提供用户姓名");
            }

            // 检查是否有文件
            if (files.length == 0) {
                return Results.failure("未选择任何文件");
            }

            // 转换数组为列表
            List<MultipartFile> fileList = List.of(files);

            // 检查文件扩展名
            for (MultipartFile file : fileList) {
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".java")) {
                    return Results.failure("存在非java格式文件: " + originalFilename);
                }
            }

            // 上传文件
            List<String> fileUrls = aliyunOssUtil.batchUploadJavaFilesWithName(fileList, folder, name);

            if (fileUrls != null) {
                log.info("批量上传java文件完成，成功上传 {} 个文件", fileUrls.size());
                return Results.success(fileUrls);
            } else {
                log.error("批量上传java文件失败");
                return Results.failure("文件上传失败");
            }
        } catch (Exception e) {
            log.error("批量上传java文件时发生异常", e);
            return Results.failure("上传过程中发生错误: " + e.getMessage());
        }
    }
}