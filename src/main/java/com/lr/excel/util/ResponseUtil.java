/*
 * Copyright (c) 2025 LR-Software. All rights reserved.
 */
package com.lr.excel.util;

import com.lr.commons.Assert;
import com.lr.commons.file.FileExtensionPool;
import com.lr.commons.lang.text.StrPool;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;

/**
 * ResponseUtil
 *
 * @author 一可爱小白兔
 * @date 2025-09-16 10:17
 */
public abstract class ResponseUtil {

    /**
     * 私有构造
     */
    private ResponseUtil() {
    }

    /**
     * 设置 Excel 导出文件头
     *
     * @param fileName 文件名 (xx/xx.xlsx)
     * @param response {@link HttpServletResponse}
     */
    public static void setupExcelExportFileHeader(@NonNull String fileName, @NonNull HttpServletResponse response) {
        // 验证参数
        Assert.notNull(response, "HttpServletResponse must not be null");
        Assert.hasText(fileName, "File name must not be blank");
        // 确保 fileName 以 '.xsls' 结尾
        if (!fileName.toLowerCase().endsWith(FileExtensionPool.EXCEL_XSSF.toLowerCase())) {
            fileName = fileName + StrPool.DOT + FileExtensionPool.EXCEL_XSSF;
        }
        // 重置响应
        response.resetBuffer();
        // 安全：防止文件名 XSS 或路径遍历
        String safeFileName = fileName.replaceAll("[\\\\/:*?\"<>|]", StrPool.DASH)
                .replace("\\.\\.", StrPool.DASH);
        String encodedFileName = URLEncoder.encode(safeFileName, StandardCharsets.UTF_8);
        // 设置基本头
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + encodedFileName + "; filename*=UTF-8''" + encodedFileName);
        response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(0));
        // 缓存控制
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0);
    }

}
