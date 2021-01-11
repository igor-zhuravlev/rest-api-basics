package com.epam.esm.repository.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.repository.impl.mapper.GiftCertificateMapper;
import com.epam.esm.repository.impl.mapper.TagMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {
    private static final Logger logger = LogManager.getLogger(TagRepositoryImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String FIND_ALL_QUERY = "SELECT id, name FROM tag";
    private static final String FIND_BY_NAME_QUERY = "SELECT id, name FROM tag WHERE name = ?";
    private static final String SAVE_QUERY = "INSERT INTO tag (name) VALUES (?)";
    private static final String DELETE_BY_NAME_QUERY = "DELETE FROM tag WHERE name = ?";

    @Override
    public List<Tag> findAll() throws RepositoryException {
        try {
            return jdbcTemplate.query(FIND_ALL_QUERY, new TagMapper());
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Tag findByName(String name) throws RepositoryException {
        try {
            // TODO: 10-Jan-21 alter as prepare statement
            return jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, new TagMapper(), name);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Tag save(Tag tag) throws RepositoryException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator preparedStatementCreator = con -> {
                PreparedStatement preparedStatement = con.prepareStatement(SAVE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, tag.getName());
                return preparedStatement;
            };

            jdbcTemplate.update(preparedStatementCreator, keyHolder);

            // TODO: 11-Jan-21 keys as null exception
            tag.setId((Integer) keyHolder.getKeys().get(TagMapper.ID));

            return tag;
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public void deleteByName(String name) throws RepositoryException {
        try {
            PreparedStatementCreator preparedStatementCreator = con -> {
                PreparedStatement preparedStatement = con.prepareStatement(DELETE_BY_NAME_QUERY);
                preparedStatement.setString(1, name);
                return preparedStatement;
            };
            jdbcTemplate.update(preparedStatementCreator);
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }
}
