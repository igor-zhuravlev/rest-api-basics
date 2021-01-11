package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.repository.impl.mapper.GiftCertificateMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private static final Logger logger = LogManager.getLogger(GiftCertificateRepositoryImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String FIND_ALL_QUERY =
            "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate";
    private static final String FIND_BY_NAME_QUERY =
            "SELECT id, name, description, price, duration, create_date, last_update_date FROM gift_certificate WHERE name = ?";
    private static final String FIND_BY_TAG_NAME_QUERY =
            "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gift_certificate gc " +
            "JOIN gift_certificate_tag gct ON gc.id = gct.gift_certificate_id " +
            "JOIN tag t ON gct.tag_id = t.id " +
            "WHERE t.name = ?";
    private static final String SAVE_QUERY =
            "INSERT INTO gift_certificate (name, description, price, duration, create_date) VALUES (?, ?, ?, ?, now())";
    private static final String SAVE_FK_GC_TAG_QUERY =
            "INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, ?)";
    private static final String DELETE_BY_NAME_QUERY =
            "DELETE FROM gift_certificate WHERE name = ?";
    private static final String UPDATE_BY_NAME_QUERY =
            "UPDATE gift_certificate SET name = ?, description = ?, price = ?, duration = ?, last_update_date = now() WHERE name = ?";

    @Override
    public GiftCertificate findByName(String name) throws RepositoryException {
        try {
            // TODO: 10-Jan-21 alter as prepare statement
            return jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, new GiftCertificateMapper(), name);
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public List<GiftCertificate> findByTagName(String name) throws RepositoryException {
        try {
            PreparedStatementCreator preparedStatementCreator = con -> {
                PreparedStatement preparedStatement = con.prepareStatement(FIND_BY_TAG_NAME_QUERY);
                preparedStatement.setString(1, name);
                return preparedStatement;
            };
            return jdbcTemplate.query(preparedStatementCreator, new GiftCertificateMapper());
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public GiftCertificate updateByName(String name, GiftCertificate giftCertificate) throws RepositoryException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator preparedStatementCreator = con -> {
                PreparedStatement preparedStatement = con.prepareStatement(UPDATE_BY_NAME_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, giftCertificate.getName());
                preparedStatement.setString(2, giftCertificate.getDescription());
                preparedStatement.setBigDecimal(3, giftCertificate.getPrice());
                preparedStatement.setInt(4, giftCertificate.getDuration());
                preparedStatement.setString(5, name);
                return preparedStatement;
            };

            jdbcTemplate.update(preparedStatementCreator, keyHolder);

            // TODO: 10-Jan-21 keys as null exception
            giftCertificate.setId((Integer) keyHolder.getKeys().get(GiftCertificateMapper.ID));

            Set<Tag> tags = giftCertificate.getTags();
            if (tags != null) {
                saveGiftCertificateTags(giftCertificate.getId(), tags);
            }

            return giftCertificate;
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    private void saveGiftCertificateTags(Integer giftCertificateId, Set<Tag> tags) {
        tags.stream()
                .mapToInt(Tag::getId)
                .forEach(tagId -> jdbcTemplate.update(SAVE_FK_GC_TAG_QUERY, giftCertificateId, tagId));
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

    @Override
    public List<GiftCertificate> findAll() throws RepositoryException {
        try {
            return jdbcTemplate.query(FIND_ALL_QUERY, new GiftCertificateMapper());
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) throws RepositoryException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator preparedStatementCreator = con -> {
                PreparedStatement preparedStatement = con.prepareStatement(SAVE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, giftCertificate.getName());
                preparedStatement.setString(2, giftCertificate.getDescription());
                preparedStatement.setBigDecimal(3, giftCertificate.getPrice());
                preparedStatement.setInt(4, giftCertificate.getDuration());
                return preparedStatement;
            };

            jdbcTemplate.update(preparedStatementCreator, keyHolder);

            // TODO: 10-Jan-21 keys as null exception
            giftCertificate.setId((Integer) keyHolder.getKeys().get(GiftCertificateMapper.ID));
            giftCertificate.setCreateDate((Date) keyHolder.getKeys().get(GiftCertificateMapper.CREATE_DATE));

            Set<Tag> tags = giftCertificate.getTags();
            if (tags != null) {
                saveGiftCertificateTags(giftCertificate.getId(), tags);
            }

            return giftCertificate;
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }
}
