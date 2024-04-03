package dev.lecture.pharmacy.controller;

import dev.lecture.pharmacy.cache.PharmacyRedisTemplateService;
import dev.lecture.pharmacy.dto.PharmacyDto;
import dev.lecture.pharmacy.entity.Pharmacy;
import dev.lecture.pharmacy.service.PharmacyRepositoryService;
import dev.lecture.util.CsvUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyRepositoryService pharmacyRepositoryService;
    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

    // 데이터 초기 셋팅을 위한 임시 메소드
    @GetMapping("/csv/save")
    public String saveCsv() {
        saveCsvToDatabase();
        saveCsvToRedis();

        return "success save";
    }

    public void saveCsvToDatabase() {

        List<Pharmacy> pharmacyList = loadPharmacyList();
        pharmacyRepositoryService.saveAll(pharmacyList);

    }

    public void saveCsvToRedis() {

        List<PharmacyDto> pharmacyDtoList = pharmacyRepositoryService.findAll()
                .stream().map(pharmacy -> PharmacyDto.builder()
                        .id(pharmacy.getId())
                        .pharmacyName(pharmacy.getPharmacyName())
                        .pharmacyAddress(pharmacy.getPharmacyAddress())
                        .latitude(pharmacy.getLatitude())
                        .longitude(pharmacy.getLongitude())
                        .build())
                .collect(Collectors.toList());

        pharmacyDtoList.forEach(pharmacyRedisTemplateService::save);

    }

    private List<Pharmacy> loadPharmacyList() {
        return CsvUtils.convertToPharmacyDtoList()
                .stream().map(pharmacyDto ->
                        Pharmacy.builder()
                                .id(pharmacyDto.getId())
                                .pharmacyName(pharmacyDto.getPharmacyName())
                                .pharmacyAddress(pharmacyDto.getPharmacyAddress())
                                .latitude(pharmacyDto.getLatitude())
                                .longitude(pharmacyDto.getLongitude())
                                .build())
                .collect(Collectors.toList());
    }
}
