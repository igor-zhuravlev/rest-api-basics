package com.epam.esm.repository.impl.mapper;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagMapper implements RowMapper<Tag> {

    public static final String ID = "t_id";
    public static final String NAME = "t_name";

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getInt(ID));
        tag.setName(rs.getString(NAME));
        return tag;
    }
}
