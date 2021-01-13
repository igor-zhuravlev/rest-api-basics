package com.epam.esm.repository.impl.mapper;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    public static final String ID = "gc_id";
    public static final String NAME = "gc_name";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String DURATION = "duration";
    public static final String CREATE_DATE = "create_date";
    public static final String LAST_UPDATE_DATE = "last_update_date";

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(rs.getInt(ID));
        giftCertificate.setName(rs.getString(NAME));
        giftCertificate.setDescription(rs.getString(DESCRIPTION));
        giftCertificate.setPrice(rs.getBigDecimal(PRICE));
        giftCertificate.setDuration(rs.getInt(DURATION));
        giftCertificate.setCreateDate(rs.getTimestamp(CREATE_DATE));
        giftCertificate.setLastUpdateDate(rs.getTimestamp(LAST_UPDATE_DATE));
        return giftCertificate;
    }
}
