package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.exception.RepositoryException;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;

public interface GiftCertificateRepository extends Repository<GiftCertificate, Long> {
    GiftCertificate findByName(String name) throws RepositoryException;
    List<GiftCertificate> findByTagName(String tagName) throws RepositoryException;
    Long updateById(Long id, GiftCertificate giftCertificate) throws RepositoryException;
    Long deleteByName(String name) throws RepositoryException;

    Set<GiftCertificate> findAllWithTags() throws RepositoryException;
    Set<GiftCertificate> findAllWithTagsByOrder(Sort sort) throws RepositoryException;

    Set<GiftCertificate> findAllByTagName(String tagName) throws RepositoryException;
    Set<GiftCertificate> findAllByPartOfNameOrDescription(String part) throws RepositoryException;
    Set<GiftCertificate> findAllByTagNameAndPartOfNameOrDescription(String tagName, String part) throws RepositoryException;

    Set<GiftCertificate> findAllByTagNameByOrder(String tagName, Sort sort) throws RepositoryException;
    Set<GiftCertificate> findAllByPartOfNameOrDescriptionByOrder(String part, Sort sort) throws RepositoryException;
    Set<GiftCertificate> findAllByTagNameAndPartOfNameOrDescriptionByOrder(String tagName, String part, Sort sort) throws RepositoryException;
}
