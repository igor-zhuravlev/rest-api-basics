package com.epam.esm.web.advice;

import com.epam.esm.service.exception.GiftCertificateNotFoundException;
import com.epam.esm.service.exception.TagNotFoundException;
import com.epam.esm.dto.ErrorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private ErrorDto handle(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(code, null, locale);
        return new ErrorDto(message, code);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handlerSurprise(Exception e) {
        return new ErrorDto(e.getMessage(), "000");
    }

    @ExceptionHandler(value = GiftCertificateNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handlerGiftCertificateNotFoundException(GiftCertificateNotFoundException e) {
        return handle(e.getMessage());
    }

    @ExceptionHandler(value = TagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handlerTagNotFoundException(TagNotFoundException e) {
        return handle(e.getMessage());
    }
}
