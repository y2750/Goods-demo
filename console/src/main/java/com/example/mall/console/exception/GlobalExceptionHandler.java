package com.example.mall.console.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ClassName: GlobalExceptionHandler
 * Package: com.example.mall.app.exception
 * Description:
 *
 * @Author
 * @Create 2026/10/2 11:13
 * @Version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //全局异常处理
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("请求处理发生未捕获异常", e);
        return ResponseEntity.status(500).body("⽹络繁忙");
    }
}
