package com.epam.esm.repository;

import com.epam.esm.repository.exception.RepositoryException;

import java.util.List;

public interface Repository<E> {
    List<E> findAll() throws RepositoryException;
    E save(E entity) throws RepositoryException;
}
