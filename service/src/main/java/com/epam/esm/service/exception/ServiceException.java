package com.epam.esm.service.exception;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = -8742650263017937163L;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
