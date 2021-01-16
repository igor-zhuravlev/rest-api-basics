package com.epam.esm.service.exception.certificate;

import com.epam.esm.service.exception.ServiceException;

public class GiftCertificateAlreadyExistException extends ServiceException {
    private static final long serialVersionUID = -8892115154572538648L;

    public GiftCertificateAlreadyExistException(String message) {
        super(message);
    }

    public GiftCertificateAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public GiftCertificateAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
