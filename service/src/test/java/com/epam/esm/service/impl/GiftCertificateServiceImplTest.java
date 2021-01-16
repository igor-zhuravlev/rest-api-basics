package com.epam.esm.service.impl;

import com.epam.esm.converter.Converter;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.certificate.GiftCertificateAlreadyExistException;
import com.epam.esm.service.exception.certificate.GiftCertificateNotFoundException;
import com.epam.esm.service.exception.certificate.UnableDeleteGiftCertificateException;
import com.epam.esm.service.exception.certificate.UnableUpdateGiftCertificate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private Converter<GiftCertificate, GiftCertificateDto> giftCertificateConverter;

    @InjectMocks
    private final GiftCertificateService giftCertificateService = new GiftCertificateServiceImpl();

    private List<GiftCertificate> gifts;
    private List<GiftCertificateDto> giftsDto;

    @BeforeEach
    void beforeEach() {
        gifts = new ArrayList<>();
        giftsDto = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            GiftCertificate giftCertificate = new GiftCertificate();
            giftCertificate.setId(i);
            giftCertificate.setName("gift" + i);
            giftCertificate.setDescription("description" + i);
            giftCertificate.setPrice(BigDecimal.valueOf(9.99));
            giftCertificate.setDuration(10);
            gifts.add(giftCertificate);

            GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
            giftCertificateDto.setName("gift" + i);
            giftCertificateDto.setDescription("description" + i);
            giftCertificateDto.setPrice(BigDecimal.valueOf(9.99));
            giftCertificateDto.setDuration(10);
            giftsDto.add(giftCertificateDto);
        }
    }

    @Test
    void findAllWithTags() {
    }

    @Test
    void findAll_ReturnGiftCertificateList() {
        given(giftCertificateRepository.findAll()).willReturn(gifts);
        given(giftCertificateConverter.entityToDtoList(gifts)).willReturn(giftsDto);

        List<GiftCertificateDto> expectedGiftsDto = giftCertificateService.findAll();

        assertEquals(expectedGiftsDto, giftsDto);

        then(giftCertificateRepository)
                .should(only())
                .findAll();

        then(giftCertificateConverter)
                .should(only())
                .entityToDtoList(anyList());
    }

    @Test
    void findByName_ReturnGiftCertificateDto() {
        final String name = "gift1";

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("gift1");

        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setName("gift1");

        given(giftCertificateRepository.findByName(name)).willReturn(giftCertificate);
        given(giftCertificateConverter.entityToDto(giftCertificate)).willReturn(giftCertificateDto);

        GiftCertificateDto expected = giftCertificateService.findByName(name);

        assertEquals(expected, giftCertificateDto);

        then(giftCertificateRepository)
                .should(only())
                .findByName(anyString());

        then(giftCertificateConverter)
                .should(only())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void findByTagName_ReturnGiftCertificateDtoListByTagName() {
        final String tagName = "tag1";

        given(giftCertificateRepository.findByTagName(tagName)).willReturn(gifts);
        given(giftCertificateConverter.entityToDtoList(gifts)).willReturn(giftsDto);

        List<GiftCertificateDto> expected = giftCertificateService.findByTagName(tagName);

        assertEquals(expected, giftsDto);

        then(giftCertificateRepository)
                .should(only())
                .findByTagName(anyString());

        then(giftCertificateConverter)
                .should(only())
                .entityToDtoList(anyList());
    }

    @Test
    void save_ReturnSavedGiftCertificateAsDto() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("gift1");

        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setName("gift1");

        given(giftCertificateConverter.dtoToEntity(giftCertificateDto)).willReturn(giftCertificate);
        given(giftCertificateRepository.findByName(giftCertificate.getName())).willReturn(null);
        given(giftCertificateRepository.save(giftCertificate)).willReturn(giftCertificate);
        given(giftCertificateConverter.entityToDto(giftCertificate)).willReturn(giftCertificateDto);

        GiftCertificateDto expected = giftCertificateService.save(giftCertificateDto);

        assertEquals(expected, giftCertificateDto);

        then(giftCertificateConverter)
                .should(times(1))
                .dtoToEntity(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(times(1))
                .findByName(anyString());

        then(giftCertificateRepository)
                .should(times(1))
                .save(any(GiftCertificate.class));

        then(giftCertificateConverter)
                .should(times(1))
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void save_ThrowGiftCertificateAlreadyExistException() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("gift1");

        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setName("gift1");

        given(giftCertificateConverter.dtoToEntity(giftCertificateDto)).willReturn(giftCertificate);
        given(giftCertificateRepository.findByName(giftCertificate.getName())).willReturn(giftCertificate);

        assertThrows(GiftCertificateAlreadyExistException.class, () -> {
            GiftCertificateDto expected = giftCertificateService.save(giftCertificateDto);
        });

        then(giftCertificateConverter)
                .should(only())
                .dtoToEntity(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(only())
                .findByName(anyString());

        then(giftCertificateRepository)
                .should(never())
                .save(any(GiftCertificate.class));

        then(giftCertificateConverter)
                .should(never())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void updateByName_ReturnUpdatedGiftCertificateAsDto() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("gift1");

        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setName("gift1");

        final String name = "gift1";

        given(giftCertificateRepository.findByName(anyString())).willReturn(giftCertificate);
        given(giftCertificateConverter.dtoToEntity(any(GiftCertificateDto.class))).willReturn(giftCertificate);

        given(giftCertificateRepository.updateById(anyLong(), any(GiftCertificate.class))).willReturn(1L);
        given(giftCertificateRepository.findById(anyLong())).willReturn(giftCertificate);
        given(giftCertificateConverter.entityToDto(any(GiftCertificate.class))).willReturn(giftCertificateDto);

        GiftCertificateDto expected = giftCertificateService.updateByName(name, giftCertificateDto);

        assertEquals(expected, giftCertificateDto);

        then(giftCertificateRepository)
                .should(times(1))
                .findByName(anyString());

        then(giftCertificateConverter)
                .should(times(1))
                .dtoToEntity(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(times(1))
                .updateById(anyLong(), any(GiftCertificate.class));

        then(giftCertificateRepository)
                .should(times(1))
                .findById(anyLong());

        then(giftCertificateConverter)
                .should(times(1))
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void updateByName_ThrowGiftCertificateNotFoundException() {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setName("gift1");

        final String name = "gift1";

        given(giftCertificateRepository.findByName(anyString())).willReturn(null);

        assertThrows(GiftCertificateNotFoundException.class, () -> {
            giftCertificateService.updateByName(name, giftCertificateDto);
        });

        then(giftCertificateRepository)
                .should(only())
                .findByName(anyString());

        then(giftCertificateConverter)
                .should(never())
                .dtoToEntity(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(never())
                .updateById(anyLong(), any(GiftCertificate.class));

        then(giftCertificateRepository)
                .should(never())
                .findById(anyLong());

        then(giftCertificateConverter)
                .should(never())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void updateByName_ThrowUnableUpdateGiftCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("gift1");

        GiftCertificateDto giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setName("gift1");

        final String name = "gift1";

        given(giftCertificateRepository.findByName(anyString())).willReturn(giftCertificate);
        given(giftCertificateConverter.dtoToEntity(any(GiftCertificateDto.class))).willReturn(giftCertificate);

        given(giftCertificateRepository.updateById(anyLong(), any(GiftCertificate.class))).willReturn(0L);

        assertThrows(UnableUpdateGiftCertificate.class, () -> {
            giftCertificateService.updateByName(name, giftCertificateDto);
        });

        then(giftCertificateRepository)
                .should(times(1))
                .findByName(anyString());

        then(giftCertificateConverter)
                .should(times(1))
                .dtoToEntity(any(GiftCertificateDto.class));

        then(giftCertificateRepository)
                .should(times(1))
                .updateById(anyLong(), any(GiftCertificate.class));

        then(giftCertificateRepository)
                .should(never())
                .findById(anyLong());

        then(giftCertificateConverter)
                .should(never())
                .entityToDto(any(GiftCertificate.class));
    }

    @Test
    void deleteByName_Success() {
        final String name = "gift1";

        GiftCertificate giftCertificate = new GiftCertificate();

        given(giftCertificateRepository.findByName(name)).willReturn(giftCertificate);
        given(giftCertificateRepository.deleteByName(name)).willReturn(1L);

        giftCertificateService.deleteByName(name);

        then(giftCertificateRepository)
                .should(times(1))
                .findByName(anyString());

        then(giftCertificateRepository)
                .should(times(1))
                .deleteByName(anyString());
    }

    @Test
    void deleteByName_ThrowGiftCertificateNotFoundException() {
        final String name = "gift1";

        given(giftCertificateRepository.findByName(name)).willReturn(any(GiftCertificate.class));

        assertThrows(GiftCertificateNotFoundException.class, () -> {
            giftCertificateService.deleteByName(name);
        });

        then(giftCertificateRepository)
                .should(only())
                .findByName(anyString());

        then(giftCertificateRepository)
                .should(never())
                .deleteByName(anyString());
    }

    @Test
    void deleteByName_ThrowUnableDeleteGiftCertificateException() {
        final String name = "gift1";

        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("gift1");

        given(giftCertificateRepository.findByName(name)).willReturn(giftCertificate);
        given(giftCertificateRepository.deleteByName(name)).willReturn(0L);

        assertThrows(UnableDeleteGiftCertificateException.class, () -> {
            giftCertificateService.deleteByName(name);
        });

        then(giftCertificateRepository)
                .should(times(1))
                .findByName(anyString());

        then(giftCertificateRepository)
                .should(times(1))
                .deleteByName(anyString());
    }
}