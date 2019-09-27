package com.laraboot.framework.contracts.debug;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface HttpExceptionHandler {
    public ResponseEntity<?> handle(HttpServletRequest request, Throwable throwable);

    public void report(Throwable throwable);
}
