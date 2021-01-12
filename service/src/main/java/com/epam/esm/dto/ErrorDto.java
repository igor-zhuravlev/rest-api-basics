package com.epam.esm.dto;

import java.io.Serializable;
import java.util.Objects;

public class ErrorDto implements Serializable {
    private static final long serialVersionUID = -7453008975136453861L;

    private String errorMessage;
    private String errorCode;

    public ErrorDto() {}

    public ErrorDto(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorDto error = (ErrorDto) o;
        return Objects.equals(errorMessage, error.errorMessage) &&
                Objects.equals(errorCode, error.errorCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorMessage, errorCode);
    }

    @Override
    public String toString() {
        return "Error{" +
                "errorMessage='" + errorMessage + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
