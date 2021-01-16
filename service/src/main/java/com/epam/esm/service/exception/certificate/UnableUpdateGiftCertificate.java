package com.epam.esm.service.exception.certificate;

import com.epam.esm.service.exception.ServiceException;

public class UnableUpdateGiftCertificate extends ServiceException {
    private static final long serialVersionUID = -1035738899521016567L;

    public UnableUpdateGiftCertificate(String message) {
        super(message);
    }

    public UnableUpdateGiftCertificate(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableUpdateGiftCertificate(Throwable cause) {
        super(cause);
    }
}
