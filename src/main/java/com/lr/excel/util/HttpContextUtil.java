/*
 * Copyright (c) 2025 LR-Software. All rights reserved.
 */
package com.lr.excel.util;

import java.util.Optional;

import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * HttpContextUtil
 *
 * @author 一可爱小白兔
 * @date 2025-01-03 10:43
 */
public abstract class HttpContextUtil {

    /**
     * 私有构造
     */
    private HttpContextUtil() {
    }

    /**
     * getHttpServletResponse
     *
     * @return {@link HttpServletResponse}
     */
    @Nullable
    public static HttpServletResponse getHttpServletResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(requestAttributes)
                .map(req -> ((ServletRequestAttributes) req).getResponse())
                .orElse(null);
    }

}
