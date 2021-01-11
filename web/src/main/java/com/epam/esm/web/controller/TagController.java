package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {
    private static final Logger logger = LogManager.getLogger(TagController.class);

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public List<TagDto> findAllTags() {
        try {
            return tagService.findAll();
        } catch (ServiceException e) {
            logger.error(e);
            throw e;
        }
    }

    @GetMapping(value = "/tags/{name}")
    public TagDto findTagByName(@PathVariable String name) {
        try {
            return tagService.findByName(name);
        } catch (ServiceException e) {
            logger.error(e);
            throw e;
        }
    }

    @PostMapping(value = "/tags")
    public TagDto saveTag(@RequestBody TagDto tagDto) {
        try {
            return tagService.save(tagDto);
        } catch (ServiceException e) {
            logger.error(e);
            throw e;
        }
    }

    @DeleteMapping(value = "/tags/{name}")
    public void deleteTag(@PathVariable String name) {
        try {
            tagService.deleteByName(name);
        } catch (ServiceException e) {
            logger.error(e);
            throw e;
        }
    }
}
