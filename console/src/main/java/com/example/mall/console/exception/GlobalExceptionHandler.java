package com.example.mall.console.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerExceptionResolver;

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

    @Autowired
    @Lazy
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    /** Controller 捕获异常后调用，仍通过全局异常处理写入 HTTP 错误响应。 */
    public void resolveException(Exception e) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        exceptionResolver.resolveException(attributes.getRequest(), attributes.getResponse(), null, e);
    }

    //全局异常处理
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("请求处理发生未捕获异常", e);
        return ResponseEntity.status(500).body("网络繁忙");
    }
}
