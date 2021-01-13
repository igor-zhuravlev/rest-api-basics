package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;
import java.util.Map;

public interface GiftCertificateService {
    List<GiftCertificateDto> findAll() throws ServiceException;
    List<GiftCertificateDto> findAllWithTags(Map<String, String[]> params) throws ServiceException;
    GiftCertificateDto findByName(String name) throws ServiceException;
    List<GiftCertificateDto> findByTagName(String name) throws ServiceException;
    GiftCertificateDto save(GiftCertificateDto giftCertificateDto) throws ServiceException;
    GiftCertificateDto updateByName(String name, GiftCertificateDto giftCertificateDto) throws ServiceException;
    void deleteByName(String name) throws ServiceException;
}
