package com.epam.esm.converter.impl;

import com.epam.esm.converter.Converter;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class GiftCertificateConverter implements Converter<GiftCertificate, GiftCertificateDto> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public GiftCertificate dtoToEntity(GiftCertificateDto dto) {
        GiftCertificate giftCertificate = new GiftCertificate();
        modelMapper.map(dto, giftCertificate);
        return giftCertificate;
    }

    @Override
    public GiftCertificateDto entityToDto(GiftCertificate entity) {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        modelMapper.map(entity, giftCertificateDto);
        return giftCertificateDto;
    }

    @Override
    public List<GiftCertificate> dtoToEntityList(List<GiftCertificateDto> dtoList) {
        return dtoList.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificateDto> entityToDtoList(List<GiftCertificate> entityList) {
        return entityList.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }
}
