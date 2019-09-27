package com.laraboot.framework.http.controller;

import com.laraboot.framework.http.response.HttpResponseEntity;
import com.laraboot.framework.http.response.Response;
import org.springframework.http.HttpStatus;

abstract public class BaseController {
    protected <T> Response ok(T data) {
        return Response.ok(data);
    }

    protected <T> Response fail(T data) {
        return Response.fail(data);
    }

    protected HttpResponseEntity raw(Object data) {
        return new HttpResponseEntity<>(data, HttpStatus.OK);
    }


}
