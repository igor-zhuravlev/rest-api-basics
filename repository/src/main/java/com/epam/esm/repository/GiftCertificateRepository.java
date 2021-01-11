package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.exception.RepositoryException;

import java.util.List;

public interface GiftCertificateRepository extends Repository<GiftCertificate> {
    GiftCertificate findByName(String name) throws RepositoryException;
    List<GiftCertificate> findByTagName(String name) throws RepositoryException;
    GiftCertificate updateByName(String name, GiftCertificate giftCertificate) throws RepositoryException;
    void deleteByName(String name) throws RepositoryException;
}
