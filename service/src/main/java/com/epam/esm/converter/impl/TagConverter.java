package com.epam.esm.converter.impl;

import com.epam.esm.converter.Converter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagConverter implements Converter<Tag, TagDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Tag dtoToEntity(TagDto tagDto) {
        Tag tag = new Tag();
        modelMapper.map(tagDto, tag);
        return tag;
    }

    @Override
    public TagDto entityToDto(Tag tag) {
        TagDto tagDto = new TagDto();
        modelMapper.map(tag, tagDto);
        return tagDto;
    }

    @Override
    public List<Tag> dtoToEntityList(List<TagDto> dtoList) {
        return dtoList.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TagDto> entityToDtoList(List<Tag> entityList) {
        return entityList.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
