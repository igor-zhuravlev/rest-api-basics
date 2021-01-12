package com.epam.esm.service.impl;

import com.epam.esm.constant.ServiceError;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.converter.Converter;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.GiftCertificateNotFoundException;
import com.epam.esm.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private static final Logger logger = LogManager.getLogger(GiftCertificateServiceImpl.class);

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private Converter<GiftCertificate, GiftCertificateDto> giftCertificateConverter;

    @Override
    public List<GiftCertificateDto> findAll() throws ServiceException {
        try {
            List<GiftCertificate> giftCertificateList = giftCertificateRepository.findAll();
            return giftCertificateConverter.entityToDtoList(giftCertificateList);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public GiftCertificateDto findByName(String name) throws ServiceException {
        try {
            GiftCertificate giftCertificate = giftCertificateRepository.findByName(name);

            if (giftCertificate == null) {
                throw new GiftCertificateNotFoundException(ServiceError.GIFT_CERTIFICATE_NOT_FOUNT.getCode());
            }

            return giftCertificateConverter.entityToDto(giftCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<GiftCertificateDto> findByTagName(String name) throws ServiceException {
        try {
            List<GiftCertificate> giftCertificateList = giftCertificateRepository.findByTagName(name);
            return giftCertificateConverter.entityToDtoList(giftCertificateList);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public GiftCertificateDto save(GiftCertificateDto giftCertificateDto) throws ServiceException {
        try {
            GiftCertificate giftCertificate = giftCertificateConverter.dtoToEntity(giftCertificateDto);

            Set<Tag> tags = giftCertificate.getTags();
            if (tags != null) {
                tags = tags.stream()
                        .map(tag -> {
                            Tag t = tagRepository.findByName(tag.getName());
                            return t == null ? tagRepository.save(tag) : t;
                        }).collect(Collectors.toSet());
                giftCertificate.setTags(tags);
            }

            giftCertificate = giftCertificateRepository.save(giftCertificate);

            return giftCertificateConverter.entityToDto(giftCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public GiftCertificateDto updateByName(String name, GiftCertificateDto giftCertificateDto) throws ServiceException {
        try {
            GiftCertificate giftCertificate = giftCertificateRepository.findByName(name);

            if (giftCertificate == null) {
                throw new GiftCertificateNotFoundException("Gift Certificate not found");
            }

            giftCertificate = giftCertificateConverter.dtoToEntity(giftCertificateDto);

            Set<Tag> tags = new HashSet<>();
            Set<Tag> newTags = new HashSet<>();
            if (giftCertificate.getTags() != null) {
                for (Tag tag : giftCertificate.getTags()) {
                    Tag t = tagRepository.findByName(tag.getName());
                    if (t == null) {
                        t = tagRepository.save(tag);
                        newTags.add(t);
                    }
                    tags.add(t);
                }
                giftCertificate.setTags(newTags);
            }

            giftCertificate = giftCertificateRepository.updateByName(name, giftCertificate);
            giftCertificate.setTags(tags);

            return giftCertificateConverter.entityToDto(giftCertificate);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteByName(String name) throws ServiceException {
        try {
            giftCertificateRepository.deleteByName(name);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}
