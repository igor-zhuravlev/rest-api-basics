package com.epam.esm.service.exception.certificate;

import com.epam.esm.service.exception.ServiceException;

public class GiftCertificateNotFoundException extends ServiceException {
    private static final long serialVersionUID = 5480895478668711729L;

    public GiftCertificateNotFoundException(String message) {
        super(message);
    }

    public GiftCertificateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GiftCertificateNotFoundException(Throwable cause) {
        super(cause);
    }
}
