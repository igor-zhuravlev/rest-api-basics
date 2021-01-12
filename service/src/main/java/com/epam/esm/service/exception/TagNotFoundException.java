package com.epam.esm.service.exception;

public class TagNotFoundException extends ServiceException {
    private static final long serialVersionUID = -4005925979810145350L;

    public TagNotFoundException(String message) {
        super(message);
    }

    public TagNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagNotFoundException(Throwable cause) {
        super(cause);
    }
}
