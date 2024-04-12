package dev.lecture.pharmacy.service;

import dev.lecture.pharmacy.dto.PharmacyDto;
import dev.lecture.pharmacy.entity.Pharmacy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacySearchService {

    private final PharmacyRepositoryService pharmacyRepositoryService;

    // 전체 약국 데이터 조회
    public List<PharmacyDto> searchPharmacyDtoList() {

        //redis

        //db
        return pharmacyRepositoryService.findAll()
                .stream()
                .map(this::converToPharmacyDto)
                .collect(Collectors.toList());
    }

    private PharmacyDto converToPharmacyDto(Pharmacy pharmacy) {

        return PharmacyDto.builder()
                .id(pharmacy.getId())
                .pharmacyAddress(pharmacy.getPharmacyAddress())
                .pharmacyName(pharmacy.getPharmacyName())
                .latitude(pharmacy.getLatitude())
                .longitude(pharmacy.getLongitude())
                .build();
    }
}
