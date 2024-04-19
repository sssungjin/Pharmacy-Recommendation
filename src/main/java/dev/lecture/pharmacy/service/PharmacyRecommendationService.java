package dev.lecture.pharmacy.service;

import dev.lecture.api.dto.DocumentDto;
import dev.lecture.api.dto.KakaoApiResponseDto;
import dev.lecture.api.service.KakaoAddressSearchService;
import dev.lecture.direction.dto.OutputDto;
import dev.lecture.direction.entity.Direction;
import dev.lecture.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;

    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";
    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

    public List<OutputDto> recommendPharmacyList(String address) {

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if(Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[PharmacyRecommendationService recommendPharmacyList] kakaoApiResponseDto is null, Input address: {}", address);
            return Collections.emptyList();
        }

        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        // kakao 카테고리를 이용한 장소 검색 api 이용
        // List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);
        List<Direction> directionList = directionService.buildDirectionList(documentDto);

        return directionService.saveAll(directionList)
                .stream()
                .map(this::converToOutputDto)
                .collect(Collectors.toList());
    }

    private OutputDto converToOutputDto(Direction direction) {

        String params = String.join(",",direction.getTargetPharmacyName(),
                String.valueOf(direction.getTargetLatitude()), String.valueOf(direction.getTargetLongitude()));

        String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params)
                .toUriString();

        log.info("direction params: {}, url: {}", params, result);

        return OutputDto.builder()
                .pharmacyName(direction.getTargetPharmacyName())
                .pharmacyAddress(direction.getTargetAddress())
                .directionUrl(result)
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + "," + direction.getTargetLongitude())
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }
}
