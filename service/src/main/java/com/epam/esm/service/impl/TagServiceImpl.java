package com.epam.esm.service.impl;

import com.epam.esm.converter.Converter;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.service.TagService;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            List<Tag> tagList = tagRepository.findAll();
            return tagConverter.entityToDtoList(tagList);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public TagDto findByName(String name) throws ServiceException {
        try {
            Tag tag = tagRepository.findByName(name);
            return tagConverter.entityToDto(tag);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public TagDto save(TagDto tagDto) throws ServiceException {
        try {
            Tag tag = tagConverter.dtoToEntity(tagDto);
            tag = tagRepository.save(tag);
            return tagConverter.entityToDto(tag);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteByName(String name) throws ServiceException {
        try {
            tagRepository.deleteByName(name);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}
