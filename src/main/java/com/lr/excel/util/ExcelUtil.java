/*
 * Copyright (c) 2025 LR-Software. All rights reserved.
 */
package com.lr.excel.util;

import com.lr.commons.Assert;
import com.lr.commons.lang.exception.UtilException;
import com.lr.commons.util.DataUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import cn.idev.excel.FastExcelFactory;
import cn.idev.excel.converters.longconverter.LongStringConverter;
import cn.idev.excel.support.ExcelTypeEnum;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.NonNull;

/**
 * ExcelUtil
 *
 * @author 一可爱小白兔
 * @date 2025-09-16 10:14
 */
public abstract class ExcelUtil {

    /**
     * Log
     */
    private static final Logger log = LogManager.getLogger(ExcelUtil.class);

    /**
     * 私有构造
     */
    private ExcelUtil() {
    }

    /**
     * 最大导出数据量: 10000
     */
    private static final int EXPORT_EXCEL_MAX_NUM_10000 = 10000;

    /**
     * 最大导出数据量: 50000
     */
    private static final int EXPORT_EXCEL_MAX_NUM_50000 = 50000;

    /**
     * exportExcel
     *
     * @param fileName  文件名
     * @param sheetName sheet 名称
     * @param clazz     映射实体类
     * @param dataList  数据列表
     */
    public static void exportExcel(@NonNull String fileName, @NonNull String sheetName,
                                   @NonNull Class<?> clazz, Collection<?> dataList) {
        // 获取响应
        HttpServletResponse response = HttpContextUtil.getHttpServletResponse();
        Assert.notNull(response, "HttpServletResponse must not be null");
        // 设置响应头
        ResponseUtil.setupExcelExportFileHeader(fileName, response);
        dataList = DataUtil.limitData(dataList, EXPORT_EXCEL_MAX_NUM_10000);
        try {
            ServletOutputStream out = response.getOutputStream();
            FastExcelFactory.write(out, clazz)
                    .autoCloseStream(true)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet(sheetName)
                    .registerConverter(new LongStringConverter())
                    .doWrite(dataList);
            out.flush();
            log.info("Excel export successful, data count: {}", dataList::size);
        } catch (IOException e) {
            log.error("Excel export failed: IO exception, client may have aborted the download", e);
            throw new UtilException("File export failed, please check your network connection");
        } catch (Exception e) {
            log.error("Excel export failed: unexpected error", e);
            throw new UtilException("An error occurred while exporting data");
        }
    }

    public static void exportExcel(@NonNull String fileName, @NonNull String sheetName,
                                   @NonNull List<List<String>> headerList, Collection<Collection<?>> dataList) {
        // 获取响应
        HttpServletResponse response = HttpContextUtil.getHttpServletResponse();
        Assert.notNull(response, "HttpServletResponse must not be null");
        // 设置响应头
        ResponseUtil.setupExcelExportFileHeader(fileName, response);
        try {
            ServletOutputStream out = response.getOutputStream();
            FastExcelFactory.write(out)
                    .excelType(ExcelTypeEnum.XLSX)
                    .autoCloseStream(true)
                    .sheet(sheetName)
                    .head(headerList)
                    .doWrite(dataList);
            out.flush();
        } catch (IOException e) {
            log.error("Excel export failed: IO exception, client may have aborted the download", e);
            throw new UtilException("File export failed, please check your network connection");
        } catch (Exception e) {
            log.error("Excel export failed: unexpected error", e);
            throw new UtilException("An error occurred while exporting data");
        }
    }

    /**
     * exportExcel
     *
     * @param file      {@link File}
     * @param sheetName sheetName
     * @param clazz     clazz
     * @param dataList  数据列表
     */
    public static void exportExcel(@NonNull File file, @NonNull String sheetName,
                                   @NonNull Class<?> clazz, Collection<?> dataList) {
        dataList = DataUtil.limitData(dataList, EXPORT_EXCEL_MAX_NUM_50000);
        try {
            FastExcelFactory.write(file, clazz)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet(sheetName)
                    .registerConverter(new LongStringConverter())
                    .doWrite(dataList);
        } catch (Exception e) {
            log.error("Excel export failed: unexpected error", e);
            throw new UtilException("An error occurred while exporting data");
        }
    }

}
