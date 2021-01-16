package com.epam.esm.constant;

public enum ServiceError {
    TAG_NOT_FOUND(Constants.CODE_1001),
    TAG_ALREADY_EXISTS(Constants.CODE_1002),
    TAG_UNABLE_DELETE(Constants.CODE_1003),

    GIFT_CERTIFICATE_NOT_FOUNT(Constants.CODE_2001),
    GIFT_CERTIFICATE_ALREADY_EXISTS(Constants.CODE_2002),
    GIFT_CERTIFICATE_UNABLE_DELETE(Constants.CODE_2003),
    UNABLE_UPDATE_GIFT_CERTIFICATE(Constants.CODE_2004);

    private final String code;

    ServiceError(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    private static class Constants {
        private static final String CODE_1001 = "1001";
        private static final String CODE_1002 = "1002";
        private static final String CODE_1003 = "1003";

        private static final String CODE_2001 = "2001";
        private static final String CODE_2002 = "2002";
        private static final String CODE_2003 = "2003";
        private static final String CODE_2004 = "2004";
    }
}
