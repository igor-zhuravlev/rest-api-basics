package com.epam.esm.repository.exception;

public class RepositoryException extends RuntimeException {
    private static final long serialVersionUID = -3950237039619962604L;

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryException(Throwable cause) {
        super(cause);
    }
}
