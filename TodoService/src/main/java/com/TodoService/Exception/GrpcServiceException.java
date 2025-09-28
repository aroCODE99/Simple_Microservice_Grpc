package com.TodoService.Exception;

public class GrpcServiceException extends RuntimeException {
    public GrpcServiceException(String message) {
        super(message);
    }
}
