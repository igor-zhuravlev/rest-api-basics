package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.config.ContextConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ContextConfig.class)
class GiftCertificateRepositoryImplTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    private List<GiftCertificate> giftList;
    private Set<GiftCertificate> giftSet;

    @BeforeEach
    void findGifts() {
        giftList = giftCertificateRepository.findAll();
        giftSet = giftCertificateRepository.findAllWithTags();
        assertNotNull(giftList);
        assertNotEquals(0, giftList.size());
        assertNotNull(giftSet);
        assertNotEquals(0, giftSet.size());
    }

    @Test
    void findById() {
        final Long id = giftList.get(0).getId();

        GiftCertificate actual = giftCertificateRepository.findById(id);

        assertNotNull(actual);
    }

    @Test
    void findByName() {
        final String name = giftList.get(0).getName();

        GiftCertificate actual = giftCertificateRepository.findByName(name);

        assertNotNull(actual);
    }

    @Test
    void findByTagName() {
        final String tagName = giftSet
                .iterator().next().getTags()
                .iterator().next().getName();

        List<GiftCertificate> actual = giftCertificateRepository.findByTagName(tagName);

        assertNotNull(actual);
    }

    @Test
    void updateById() {
        final String name = giftList.get(0).getName();
        final String newName = "gift11";

        GiftCertificate giftCertificate = giftCertificateRepository.findByName(name);
        giftCertificate.setName(newName);
        giftCertificate.setLastUpdateDate(LocalDateTime.of(2021, 2, 2,2,2));

        Long count = giftCertificateRepository
                .updateById(giftCertificate.getId(), giftCertificate);
        GiftCertificate actual = giftCertificateRepository.findByName(newName);

        assertEquals(1, count);
        assertEquals(giftCertificate, actual);
    }

    @Test
    void deleteByName() {
        final String name = giftList.get(0).getName();

        GiftCertificate existed = giftCertificateRepository.findByName(name);
        Long count = giftCertificateRepository.deleteByName(name);
        GiftCertificate actual = giftCertificateRepository.findByName(name);

        assertNotNull(existed);
        assertEquals(1, count);
        assertNull(actual);
    }

    @Test
    void findAllWithTags() {
        Set<GiftCertificate> actual = giftCertificateRepository
                .findAllWithTags();

        assertNotNull(actual);
        assertEquals(giftSet, actual);
    }

    @Test
    void findAllWithTagsByOrder_NameAsc() {
        Sort sort = Sort.by(Sort.Order.asc("name"));

        Set<GiftCertificate> actual = giftCertificateRepository
                .findAllWithTagsByOrder(sort);

        Set<GiftCertificate> expected = giftSet.stream()
                .sorted(Comparator.comparing(GiftCertificate::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        assertEquals(expected.size(), actual.size());

        Iterator<GiftCertificate> actualIterator = actual.iterator();
        Iterator<GiftCertificate> expectedIterator = expected.iterator();
        while (actualIterator.hasNext() && expectedIterator.hasNext()) {
            assertEquals(expectedIterator.next(), actualIterator.next());
        }
    }

    @Test
    void findAllWithTagsByOrder_NameDesc() {
        Sort sort = Sort.by(Sort.Order.desc("name"));

        Set<GiftCertificate> actual = giftCertificateRepository
                .findAllWithTagsByOrder(sort);

        Set<GiftCertificate> expected = giftSet.stream()
                .sorted(Comparator.comparing(GiftCertificate::getName).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        assertEquals(expected.size(), actual.size());

        Iterator<GiftCertificate> actualIterator = actual.iterator();
        Iterator<GiftCertificate> expectedIterator = expected.iterator();
        while (actualIterator.hasNext() && expectedIterator.hasNext()) {
            assertEquals(expectedIterator.next(), actualIterator.next());
        }
    }

    @Test
    void findAllByTagName() {
        final String tagName = giftList.get(1).getName();

        Set<GiftCertificate> actual = giftCertificateRepository
                .findAllByTagName(tagName);

        Set<GiftCertificate> expected = giftSet.stream()
                .filter(giftCertificate -> {
                    return giftCertificate.getTags().stream()
                            .anyMatch(tag -> tag.getName().equals(tagName));
                }).collect(Collectors.toSet());

        assertEquals(expected, actual);
    }

    @Test
    void findAllByPartOfNameOrDescription() {
        final String part = "ift";

        Set<GiftCertificate> actual = giftCertificateRepository
                .findAllByPartOfNameOrDescription(part);

        Set<GiftCertificate> expected = giftSet.stream()
                .filter(giftCertificate -> {
                    return giftCertificate.getName().contains(part)
                            || giftCertificate.getDescription().contains(part);
                }).collect(Collectors.toSet());

        assertEquals(expected, actual);
    }

    @Test
    void findAllByTagNameAndPartOfNameOrDescription() {
        final String tagName = giftList.get(1).getName();
        final String part = "ift";

        Set<GiftCertificate> actual = giftCertificateRepository
                .findAllByTagNameAndPartOfNameOrDescription(tagName, part);

        Set<GiftCertificate> expected = giftSet.stream()
                .filter(giftCertificate -> {
                    return (giftCertificate.getName().contains(part)
                            || giftCertificate.getDescription().contains(part))
                            && giftCertificate.getTags().stream()
                            .anyMatch(tag -> tag.getName().equals(tagName));
                }).collect(Collectors.toSet());

        assertEquals(expected, actual);
    }

    @Test
    void findAllByTagNameByOrder_NameDesc() {
        final String tagName = giftList.get(0).getName();
        Sort sort = Sort.by(Sort.Order.desc("name"));

        Set<GiftCertificate> actual = giftCertificateRepository
                .findAllByTagNameByOrder(tagName, sort);

        Set<GiftCertificate> expected = giftSet.stream()
                .filter(giftCertificate -> {
                    return giftCertificate.getTags().stream()
                            .anyMatch(tag -> tag.getName().equals(tagName));
                })
                .sorted(Comparator.comparing(GiftCertificate::getName).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        assertEquals(expected.size(), actual.size());

        Iterator<GiftCertificate> actualIterator = actual.iterator();
        Iterator<GiftCertificate> expectedIterator = expected.iterator();
        while (actualIterator.hasNext() && expectedIterator.hasNext()) {
            assertEquals(expectedIterator.next(), actualIterator.next());
        }
    }

    @Test
    void findAllByTagNameByOrder_NameAsc() {
        final String tagName = giftList.get(0).getName();
        Sort sort = Sort.by(Sort.Order.asc("name"));

        Set<GiftCertificate> actual = giftCertificateRepository
                .findAllByTagNameByOrder(tagName, sort);

        Set<GiftCertificate> expected = giftSet.stream()
                .filter(giftCertificate -> {
                    return giftCertificate.getTags().stream()
                            .anyMatch(tag -> tag.getName().equals(tagName));
                })
                .sorted(Comparator.comparing(GiftCertificate::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        assertEquals(expected.size(), actual.size());

        Iterator<GiftCertificate> actualIterator = actual.iterator();
        Iterator<GiftCertificate> expectedIterator = expected.iterator();
        while (actualIterator.hasNext() && expectedIterator.hasNext()) {
            assertEquals(expectedIterator.next(), actualIterator.next());
        }
    }

    @Test
    void findAllByPartOfNameOrDescriptionByOrder_NameDesc() {
        final String part = "ift";
        Sort sort = Sort.by(Sort.Order.desc("name"));

        Set<GiftCertificate> actual = giftCertificateRepository
                .findAllByPartOfNameOrDescriptionByOrder(part, sort);

        Set<GiftCertificate> expected = giftSet.stream()
                .filter(giftCertificate -> giftCertificate.getName().contains(part)
                        || giftCertificate.getDescription().contains(part))
                .sorted(Comparator.comparing(GiftCertificate::getName).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        assertEquals(expected.size(), actual.size());

        Iterator<GiftCertificate> actualIterator = actual.iterator();
        Iterator<GiftCertificate> expectedIterator = expected.iterator();
        while (actualIterator.hasNext() && expectedIterator.hasNext()) {
            assertEquals(expectedIterator.next(), actualIterator.next());
        }
    }

    @Test
    void findAllByTagNameAndPartOfNameOrDescriptionByOrder_NameAsc() {
        final String tagName = giftList.get(0).getName();
        final String part = "ift";
        Sort sort = Sort.by(Sort.Order.asc("name"));

        Set<GiftCertificate> actual = giftCertificateRepository
                .findAllByTagNameAndPartOfNameOrDescriptionByOrder(tagName, part, sort);

        Set<GiftCertificate> expected = giftSet.stream()
                .filter(giftCertificate -> giftCertificate.getName().contains(part)
                        || giftCertificate.getDescription().contains(part))
                .filter(giftCertificate -> {
                    return giftCertificate.getTags().stream()
                            .anyMatch(tag -> tag.getName().equals(tagName));
                })
                .sorted(Comparator.comparing(GiftCertificate::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        assertEquals(expected.size(), actual.size());

        Iterator<GiftCertificate> actualIterator = actual.iterator();
        Iterator<GiftCertificate> expectedIterator = expected.iterator();
        while (actualIterator.hasNext() && expectedIterator.hasNext()) {
            assertEquals(expectedIterator.next(), actualIterator.next());
        }
    }

    @Test
    void findAll() {
        List<GiftCertificate> gifts = giftCertificateRepository.findAll();

        assertNotNull(gifts);
    }

    @Test
    void save() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("gift111");
        giftCertificate.setDescription("description");
        giftCertificate.setPrice(BigDecimal.valueOf(9.99));
        giftCertificate.setCreateDate(
                LocalDateTime.of(2021, 2, 2,2,2));
        giftCertificate.setDuration(10);

        GiftCertificate notExisted = giftCertificateRepository
                .findByName(giftCertificate.getName());
        GiftCertificate saved = giftCertificateRepository
                .save(giftCertificate);
        GiftCertificate actual = giftCertificateRepository
                .findByName(giftCertificate.getName());

        assertNull(notExisted);
        assertNotNull(saved);
        assertEquals(actual, giftCertificate);
    }
}