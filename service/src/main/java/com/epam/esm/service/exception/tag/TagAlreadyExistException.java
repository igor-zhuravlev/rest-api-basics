package com.epam.esm.service.exception.tag;

import com.epam.esm.service.exception.ServiceException;

public class TagAlreadyExistException extends ServiceException {
    private static final long serialVersionUID = 2090395976066506109L;

    public TagAlreadyExistException(String message) {
        super(message);
    }

    public TagAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public TagAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
