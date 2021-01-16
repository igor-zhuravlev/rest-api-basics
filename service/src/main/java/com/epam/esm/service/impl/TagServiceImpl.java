package com.epam.esm.service.impl;

import com.epam.esm.constant.ServiceError;
import com.epam.esm.converter.Converter;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.service.TagService;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.exception.tag.TagAlreadyExistException;
import com.epam.esm.service.exception.tag.TagNotFoundException;
import com.epam.esm.service.exception.tag.UnableDeleteTagException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private static final Logger logger = LogManager.getLogger(TagServiceImpl.class);

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private Converter<Tag, TagDto> tagConverter;

    @Override
    public List<TagDto> findAll() throws ServiceException {
        try {
            List<Tag> tags = tagRepository.findAll();
            return tagConverter.entityToDtoList(tags);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public TagDto findByName(String name) throws ServiceException {
        try {
            // TODO: 15-Jan-21 validate name
            Tag tag = tagRepository.findByName(name);
            if (tag == null) {
                throw new TagNotFoundException(ServiceError.TAG_NOT_FOUND.getCode());
            }
            return tagConverter.entityToDto(tag);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public TagDto save(TagDto tagDto) throws ServiceException {
        try {
            // TODO: 15-Jan-21 validate tagDto
            Tag tag = tagConverter.dtoToEntity(tagDto);
            if (tagRepository.findByName(tag.getName()) != null) {
                throw new TagAlreadyExistException(ServiceError.TAG_ALREADY_EXISTS.getCode());
            }
            return tagConverter.entityToDto(tagRepository.save(tag));
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    @Override
    public void deleteByName(String name) throws ServiceException {
        try {
            // TODO: 16-Jan-21 validate name
            Tag tag = tagRepository.findByName(name);

            if (tag == null) {
                throw new TagNotFoundException(ServiceError.TAG_NOT_FOUND.getCode());
            }

            long count = tagRepository.deleteByName(name);

            if (count == 0) {
                throw new UnableDeleteTagException(ServiceError.TAG_UNABLE_DELETE.getCode());
            }
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}
