package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.exception.RepositoryException;

public interface TagRepository extends Repository<Tag, Long> {
    Tag findByName(String name) throws RepositoryException;
    Long deleteByName(String name) throws RepositoryException;
}
