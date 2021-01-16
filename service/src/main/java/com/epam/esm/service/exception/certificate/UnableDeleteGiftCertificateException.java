package com.epam.esm.service.exception.certificate;

import com.epam.esm.service.exception.ServiceException;

public class UnableDeleteGiftCertificateException extends ServiceException {
    private static final long serialVersionUID = 4829675206020157517L;

    public UnableDeleteGiftCertificateException(String message) {
        super(message);
    }

    public UnableDeleteGiftCertificateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableDeleteGiftCertificateException(Throwable cause) {
        super(cause);
    }
}
