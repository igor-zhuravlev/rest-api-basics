package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.exception.RepositoryException;

public interface TagRepository extends Repository<Tag> {
    Tag findByName(String name) throws RepositoryException;
    void deleteByName(String name) throws RepositoryException;
}
