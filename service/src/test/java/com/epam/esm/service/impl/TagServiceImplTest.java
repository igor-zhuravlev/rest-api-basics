package com.epam.esm.service.impl;

import com.epam.esm.converter.Converter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.tag.TagAlreadyExistException;
import com.epam.esm.service.exception.tag.TagNotFoundException;
import com.epam.esm.service.exception.tag.UnableDeleteTagException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;
    @Mock
    private Converter<Tag, TagDto> tagConverter;

    @InjectMocks
    private final TagService tagService = new TagServiceImpl();

    @Test
    void findAll_Success() {
        List<TagDto> tagDtoList = new ArrayList<>();
        tagDtoList.add(new TagDto("tag1"));
        tagDtoList.add(new TagDto("tag2"));

        List<Tag> tagList = new ArrayList<>();

        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("tag1");

        Tag tag2 = new Tag();
        tag1.setId(2L);
        tag1.setName("tag2");

        tagList.add(tag1);
        tagList.add(tag2);

        given(tagRepository.findAll()).willReturn(tagList);
        given(tagConverter.entityToDtoList(tagList)).willReturn(tagDtoList);

        List<TagDto> expectedTagDtoList = tagService.findAll();

        assertEquals(expectedTagDtoList, tagDtoList);

        then(tagRepository)
                .should(times(1))
                .findAll();

        then(tagConverter)
                .should(times(1))
                .entityToDtoList(anyList());
    }

    @Test
    void findByName_Success() {
        final String name = "tag1";
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("tag1");
        TagDto tagDto = new TagDto("tag1");

        given(tagRepository.findByName(name)).willReturn(tag);
        given(tagConverter.entityToDto(tag)).willReturn(tagDto);

        TagDto expectedTagDto = tagService.findByName(name);

        assertEquals(expectedTagDto, tagDto);

        then(tagRepository)
                .should(times(1))
                .findByName(anyString());

        then(tagConverter)
                .should(times(1))
                .entityToDto(any(Tag.class));
    }

    @Test
    void findByName_ThrowTagNotFoundException() {
        given(tagRepository.findByName(anyString())).willReturn(null);

        assertThrows(TagNotFoundException.class, () -> {
            tagService.findByName(anyString());
        });

        then(tagRepository)
                .should(times(1))
                .findByName(anyString());

        then(tagConverter)
                .should(never())
                .entityToDto(any());
    }

    @Test
    void save_Success() {
        TagDto tagDto = new TagDto("tag1");

        Tag tag = new Tag();
        tag.setName("tag1");

        Tag savedTag = new Tag();
        savedTag.setId(1L);
        savedTag.setName("tag1");

        given(tagConverter.dtoToEntity(tagDto)).willReturn(tag);
        given(tagRepository.findByName(tag.getName())).willReturn(null);
        given(tagRepository.save(tag)).willReturn(savedTag);
        given(tagConverter.entityToDto(savedTag)).willReturn(tagDto);

        TagDto expectedTagDto = tagService.save(tagDto);

        assertNotNull(expectedTagDto);
        assertEquals(expectedTagDto, tagDto);

        then(tagConverter)
                .should(times(1))
                .dtoToEntity(any(TagDto.class));

        then(tagRepository)
                .should(times(1))
                .findByName(anyString());

        then(tagRepository)
                .should(times(1))
                .save(any(Tag.class));

        then(tagConverter)
                .should(times(1))
                .entityToDto(any(Tag.class));
    }

    @Test
    void save_ThrowTagAlreadyExistException() {
        TagDto tagDto = new TagDto("tag1");

        Tag tag = new Tag();
        tag.setName("tag1");

        Tag existedTag = new Tag();
        existedTag.setId(1L);
        existedTag.setName("tag1");

        given(tagConverter.dtoToEntity(tagDto)).willReturn(tag);
        given(tagRepository.findByName(tag.getName())).willReturn(existedTag);

        assertThrows(TagAlreadyExistException.class, () -> {
            tagService.save(tagDto);
        });

        then(tagConverter)
                .should(only())
                .dtoToEntity(any(TagDto.class));

        then(tagRepository)
                .should(only())
                .findByName(anyString());

        then(tagRepository)
                .should(never())
                .save(any(Tag.class));

        then(tagConverter)
                .should(never())
                .entityToDto(any());
    }

    @Test
    void deleteByName_Success() {
        final String name = "tag1";
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("tag1");

        given(tagRepository.findByName(name)).willReturn(tag);
        given(tagRepository.deleteByName(name)).willReturn(1L);

        tagService.deleteByName(name);

        then(tagRepository)
                .should(times(1))
                .findByName(anyString());

        then(tagRepository)
                .should(times(1))
                .deleteByName(anyString());
    }

    @Test
    void deleteByName_ThrowTagNotFoundException() {
        final String name = "tag1";

        given(tagRepository.findByName(name)).willReturn(null);

        assertThrows(TagNotFoundException.class, () -> {
            tagService.deleteByName(name);
        });

        then(tagRepository)
                .should(only())
                .findByName(anyString());

        then(tagRepository)
                .should(never())
                .deleteByName(anyString());

        then(tagConverter)
                .should(never())
                .entityToDto(any());
    }

    @Test
    void deleteByName_ThrowUnableDeleteTagException() {
        final String name = "tag1";
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("tag1");

        given(tagRepository.findByName(name)).willReturn(tag);
        given(tagRepository.deleteByName(name)).willReturn(0L);

        assertThrows(UnableDeleteTagException.class, () -> {
            tagService.deleteByName(name);
        });

        then(tagRepository)
                .should(times(1))
                .findByName(anyString());

        then(tagRepository)
                .should(times(1))
                .deleteByName(anyString());

        then(tagConverter)
                .should(never())
                .entityToDto(any());
    }
}