package com.michael.fu.hsf301assigment1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaseController: Nền tảng dùng chung cho các controller.
 * - Cung cấp logger cho controller con.
 * - Có thể thêm các method dùng chung, khai báo một số model attribute toàn cục.
 */
public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Ghi log thông tin nhanh cho controller con.
     */
    protected void logInfo(String message, Object... args) {
        logger.info(message, args);
    }

    protected void logError(String message, Object... args) {
        logger.error(message, args);
    }
}