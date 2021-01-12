package com.epam.esm.constant;

public enum ServiceError {
    TAG_NOT_FOUND(Constants.CODE_001),
    GIFT_CERTIFICATE_NOT_FOUNT(Constants.CODE_002);

    private final String code;

    ServiceError(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    private static class Constants {
        private static final String CODE_001 = "001";
        private static final String CODE_002 = "002";
    }
}
