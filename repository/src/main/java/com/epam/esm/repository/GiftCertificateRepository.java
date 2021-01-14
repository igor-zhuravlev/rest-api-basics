package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.exception.RepositoryException;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;

public interface GiftCertificateRepository extends Repository<GiftCertificate, Integer> {
    GiftCertificate findByName(String name) throws RepositoryException;
    List<GiftCertificate> findByTagName(String name) throws RepositoryException;
    GiftCertificate updateById(Integer id, GiftCertificate giftCertificate) throws RepositoryException;
    void deleteByName(String name) throws RepositoryException;

    Set<GiftCertificate> findAllWithTags() throws RepositoryException;
    Set<GiftCertificate> findAllWithTagsByOrder(Sort sort) throws RepositoryException;

    Set<GiftCertificate> findAllByTagName(String tagName) throws RepositoryException;
    Set<GiftCertificate> findAllByPartOfNameOrDescription(String part) throws RepositoryException;
    Set<GiftCertificate> findAllByTagNameAndPartOfNameOrDescription(String tagName, String part) throws RepositoryException;

    Set<GiftCertificate> findAllByTagNameByOrder(String tagName, Sort sort) throws RepositoryException;
    Set<GiftCertificate> findAllByPartOfNameOrDescriptionByOrder(String part, Sort sort) throws RepositoryException;
    Set<GiftCertificate> findAllByTagNameAndPartOfNameOrDescriptionByOrder(String tagName, String part, Sort sort) throws RepositoryException;
}
