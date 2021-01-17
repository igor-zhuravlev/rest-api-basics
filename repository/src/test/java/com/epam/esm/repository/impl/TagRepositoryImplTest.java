package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.config.ContextConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ContextConfig.class)
class TagRepositoryImplTest {

    @Autowired
    private TagRepository tagRepository;

    private List<Tag> tags;

    @BeforeEach
    void findTags() {
        tags = tagRepository.findAll();
        assertNotNull(tags);
        assertNotEquals(0, tags.size());
    }


    @Test
    void findAll() {
        List<Tag> tags = tagRepository.findAll();
        assertNotNull(tags);
    }

    @Test
    void findById() {
        final String tagName = tags.get(0).getName();

        Tag tag = tagRepository.findByName(tagName);

        assertNotNull(tag);
    }

    @Test
    void findByName() {
        final String name = tags.get(0).getName();

        Tag actual = tagRepository.findByName(name);

        assertNotNull(actual);
    }

    @Test
    void save() {
        Tag tag = new Tag("tag11");

        Tag notExisted = tagRepository.findByName(tag.getName());
        Tag saved = tagRepository.save(tag);
        Tag actual = tagRepository.findByName(tag.getName());

        assertNull(notExisted);
        assertNotNull(saved);
        assertEquals(saved, actual);
    }

    @Test
    void deleteByName() {
        final String name = tags.get(0).getName();

        Tag existed = tagRepository.findByName(name);
        Long count = tagRepository.deleteByName(name);
        Tag notExisted = tagRepository.findByName(name);

        assertNotNull(existed);
        assertEquals(1, count);
        assertNull(notExisted);
    }
}