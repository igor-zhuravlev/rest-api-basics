package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.util.QueryUtil;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.repository.impl.mapper.GiftCertificateMapper;
import com.epam.esm.repository.impl.mapper.TagMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private static final Logger logger = LogManager.getLogger(GiftCertificateRepositoryImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String FIND_ALL_QUERY =
            "SELECT id gc_id, name gc_name, description, price, duration, create_date, last_update_date FROM gift_certificate";
    private static final String FIND_ALL_WITH_TAGS_QUERY =
            "SELECT gc.id gc_id, gc.name gc_name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, t.id t_id, t.name t_name FROM gift_certificate gc " +
            "JOIN gift_certificate_tag gct ON gc.id = gct.gift_certificate_id " +
            "JOIN tag t ON gct.tag_id = t.id";
    private static final String FIND_ALL_WITH_TAGS_BY_TAG_NAME_QUERY =
            "SELECT gc.id gc_id, gc.name gc_name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, t.id t_id, t.name t_name FROM gift_certificate gc " +
            "JOIN gift_certificate_tag gct ON gc.id = gct.gift_certificate_id " +
            "JOIN tag t ON gct.tag_id = t.id " +
            "WHERE t.name = ?";
    private static final String FIND_ALL_WITH_TAGS_BY_PART_OF_NAME_OR_DESCRIPTION_QUERY =
            "SELECT gc.id gc_id, gc.name gc_name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, t.id t_id, t.name t_name FROM gift_certificate gc " +
            "JOIN gift_certificate_tag gct ON gc.id = gct.gift_certificate_id " +
            "JOIN tag t ON gct.tag_id = t.id " +
            "WHERE gc.name LIKE ? OR gc.description LIKE ?";
    private static final String FIND_ALL_WITH_TAGS_BY_TAG_NAME_AND_PART_OF_NAME_OR_DESCRIPTION_QUERY =
            "SELECT gc.id gc_id, gc.name gc_name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, t.id t_id, t.name t_name FROM gift_certificate gc " +
            "JOIN gift_certificate_tag gct ON gc.id = gct.gift_certificate_id " +
            "JOIN tag t ON gct.tag_id = t.id " +
            "WHERE t.name = ? AND (gc.name LIKE ? OR gc.description LIKE ?)";
    private static final String FIND_BY_ID_QUERY =
            "SELECT id gc_id, name gc_name, description, price, duration, create_date, last_update_date FROM gift_certificate WHERE id = ?";
    private static final String FIND_BY_NAME_QUERY =
            "SELECT id gc_id, name gc_name, description, price, duration, create_date, last_update_date FROM gift_certificate WHERE name = ?";
    private static final String FIND_BY_TAG_NAME_QUERY =
            "SELECT gc.id gc_id, gc.name gc_name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gift_certificate gc " +
            "JOIN gift_certificate_tag gct ON gc.id = gct.gift_certificate_id " +
            "JOIN tag t ON gct.tag_id = t.id " +
            "WHERE t.name = ?";
    private static final String SAVE_QUERY =
            "INSERT INTO gift_certificate (name, description, price, duration, create_date) VALUES (?, ?, ?, ?, now())";
    private static final String SAVE_FK_GC_TAG_QUERY =
            "INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, ?)";
    private static final String DELETE_BY_NAME_QUERY =
            "DELETE FROM gift_certificate WHERE name = ?";
    private static final String UPDATE_BY_ID_QUERY =
            "UPDATE gift_certificate SET " +
            "name = COALESCE(?, name), " +
            "description = COALESCE(?, description), " +
            "price = COALESCE(?, price), " +
            "duration = COALESCE(?, duration), " +
            "last_update_date = now() " +
            "WHERE id = ?";

    @Override
    public GiftCertificate findById(Integer id) throws RepositoryException {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, new GiftCertificateMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public GiftCertificate findByName(String name) throws RepositoryException {
        try {
            return jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, new GiftCertificateMapper(), name);
        } catch (EmptyResultDataAccessException e) {
            return null;
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
    public GiftCertificate updateById(Integer id, GiftCertificate giftCertificate) throws RepositoryException {
        try {
            jdbcTemplate.update(
                    UPDATE_BY_ID_QUERY,
                    giftCertificate.getName(),
                    giftCertificate.getDescription(),
                    giftCertificate.getPrice(),
                    giftCertificate.getDuration(),
                    id
            );

            if (giftCertificate.getTags() != null) {
                saveGiftCertificateTagFK(id, giftCertificate.getTags());
            }

            return giftCertificate;
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    private void saveGiftCertificateTagFK(Integer giftCertificateId, Set<Tag> tags) {
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

    private ResultSetExtractor<Set<GiftCertificate>> eagerResultSetExtractor() {
        return rs -> {
            GiftCertificateMapper giftCertificateMapper = new GiftCertificateMapper();
            TagMapper tagMapper = new TagMapper();
            Map<GiftCertificate, Set<Tag>> map = new LinkedHashMap<>();
            while (rs.next()) {
                GiftCertificate gift = giftCertificateMapper.mapRow(rs, rs.getRow());
                Tag tag = tagMapper.mapRow(rs, rs.getRow());
                if (!map.containsKey(gift)) {
                    map.put(gift, new HashSet<>());
                }
                map.get(gift).add(tag);
            }
            map.forEach(GiftCertificate::setTags);
            return map.keySet();
        };
    }

    @Override
    public Set<GiftCertificate> findAllWithTags() throws RepositoryException {
        try {
            return jdbcTemplate.query(FIND_ALL_WITH_TAGS_QUERY, eagerResultSetExtractor());
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Set<GiftCertificate> findAllWithTagsByOrder(Sort sort) throws RepositoryException {
        try {
            final String query = QueryUtil.queryWithOrder(FIND_ALL_WITH_TAGS_QUERY, sort, QueryUtil.GIFT_CERTIFICATE_TABLE_PREFIX);
            return jdbcTemplate.query(query, eagerResultSetExtractor());
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Set<GiftCertificate> findAllByTagName(String tagName) throws RepositoryException {
        try {
            return jdbcTemplate.query(FIND_ALL_WITH_TAGS_BY_TAG_NAME_QUERY, eagerResultSetExtractor(), tagName);
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Set<GiftCertificate> findAllByPartOfNameOrDescription(String part) throws RepositoryException {
        try {
            final String pattern = QueryUtil.anyMathLikePattern(part);
            return jdbcTemplate.query(FIND_ALL_WITH_TAGS_BY_PART_OF_NAME_OR_DESCRIPTION_QUERY, eagerResultSetExtractor(), pattern, pattern);
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Set<GiftCertificate> findAllByTagNameAndPartOfNameOrDescription(String tagName, String part) throws RepositoryException {
        try {
            final String pattern = QueryUtil.anyMathLikePattern(part);
            return jdbcTemplate.query(FIND_ALL_WITH_TAGS_BY_TAG_NAME_AND_PART_OF_NAME_OR_DESCRIPTION_QUERY, eagerResultSetExtractor(), tagName, pattern, pattern);
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Set<GiftCertificate> findAllByTagNameByOrder(String tagName, Sort sort) throws RepositoryException {
        try {
            final String query = QueryUtil.queryWithOrder(FIND_ALL_WITH_TAGS_BY_TAG_NAME_QUERY, sort, QueryUtil.GIFT_CERTIFICATE_TABLE_PREFIX);
            return jdbcTemplate.query(query, eagerResultSetExtractor(), tagName);
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Set<GiftCertificate> findAllByPartOfNameOrDescriptionByOrder(String part, Sort sort) throws RepositoryException {
        try {
            final String query = QueryUtil.queryWithOrder(FIND_ALL_WITH_TAGS_BY_PART_OF_NAME_OR_DESCRIPTION_QUERY, sort, QueryUtil.GIFT_CERTIFICATE_TABLE_PREFIX);
            final String pattern = QueryUtil.anyMathLikePattern(part);
            return jdbcTemplate.query(query, eagerResultSetExtractor(), pattern, pattern);
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Set<GiftCertificate> findAllByTagNameAndPartOfNameOrDescriptionByOrder(String tagName, String part, Sort sort) throws RepositoryException {
        try {
            final String query = QueryUtil.queryWithOrder(FIND_ALL_WITH_TAGS_BY_TAG_NAME_AND_PART_OF_NAME_OR_DESCRIPTION_QUERY, sort, QueryUtil.GIFT_CERTIFICATE_TABLE_PREFIX);
            final String pattern = QueryUtil.anyMathLikePattern(part);
            return jdbcTemplate.query(query, eagerResultSetExtractor(), tagName, pattern, pattern);
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
            giftCertificate.setId((Integer) keyHolder.getKeys().get(GiftCertificateMapper.SECONDARY_ID));
            giftCertificate.setCreateDate((Date) keyHolder.getKeys().get(GiftCertificateMapper.CREATE_DATE));

            if (giftCertificate.getTags() != null) {
                saveGiftCertificateTagFK(giftCertificate.getId(), giftCertificate.getTags());
            }

            return giftCertificate;
        } catch (DataAccessException e) {
            throw new RepositoryException(e);
        }
    }
}
