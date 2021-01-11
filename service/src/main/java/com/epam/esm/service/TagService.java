package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

public interface TagService {
    List<TagDto> findAll() throws ServiceException;
    TagDto findByName(String name) throws ServiceException;
    TagDto save(TagDto tagDto) throws ServiceException;
    void deleteByName(String name) throws ServiceException;
}
