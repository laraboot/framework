package com.laraboot.framework.auth;

import com.laraboot.framework.debug.RenderableException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends RenderableException {

    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
