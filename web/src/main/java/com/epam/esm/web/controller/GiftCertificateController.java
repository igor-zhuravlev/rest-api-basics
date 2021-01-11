package com.epam.esm.web.controller;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GiftCertificateController {
    private static final Logger logger = LogManager.getLogger(GiftCertificateController.class);

    @Autowired
    private GiftCertificateService giftCertificateService;

    @GetMapping("/gifts")
    public List<GiftCertificateDto> findAllGiftCertificates() {
        try {
            return giftCertificateService.findAll();
        } catch (ServiceException e) {
            logger.error(e);
            throw e;
        }
    }

    @GetMapping("/gifts/tag/{name}")
    public List<GiftCertificateDto> findGiftCertificatesByTag(@PathVariable String name) {
        try {
            return giftCertificateService.findByTagName(name);
        } catch (ServiceException e) {
            logger.error(e);
            throw e;
        }
    }

    @GetMapping("/gifts/{name}")
    public GiftCertificateDto findGiftCertificateByName(@PathVariable String name) {
        try {
            return giftCertificateService.findByName(name);
        } catch (ServiceException e) {
            logger.error(e);
            throw e;
        }
    }

    @PostMapping("/gifts")
    public GiftCertificateDto saveGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
        try {
            return giftCertificateService.save(giftCertificateDto);
        } catch (ServiceException e) {
            logger.error(e);
            throw e;
        }
    }

    @PutMapping("/gifts/{name}")
    public GiftCertificateDto updateGiftCertificate(@PathVariable String name, @RequestBody GiftCertificateDto giftCertificateDto) {
        try {
            return giftCertificateService.updateByName(name, giftCertificateDto);
        } catch (ServiceException e) {
            logger.error(e);
            throw e;
        }
    }

    @DeleteMapping("/gifts/{name}")
    public void deleteGiftCertificateByName(@PathVariable String name) {
        try {
            giftCertificateService.deleteByName(name);
        } catch (ServiceException e) {
            logger.error(e);
            throw e;
        }
    }
}
