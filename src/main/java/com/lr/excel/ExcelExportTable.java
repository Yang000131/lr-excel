/*
 * Copyright (c) 2025 LR-Software. All rights reserved.
 */
package com.lr.excel;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 动态导出表头
 *
 * @param key      列表名，应与前端页面字段信息一致
 * @param value    导出字段，应与VO字段保持一致
 * @param orderNum 字段导出顺序，用户依次勾选值
 * @author 一可爱小白兔
 * @date 2025-09-16 10:14
 */
@Schema(description = "动态导出表头")
public record ExcelExportTable(

        @Schema(description = "关键字") String key,

        @Schema(description = "属性值") Object value,

        @Schema(description = "顺序") Integer orderNum
) {
}
