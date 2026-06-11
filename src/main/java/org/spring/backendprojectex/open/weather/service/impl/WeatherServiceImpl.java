package org.spring.backendprojectex.open.weather.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.open.weather.dto.WeatherApiDto;
import org.spring.backendprojectex.open.weather.entity.WeatherEntity;
import org.spring.backendprojectex.open.weather.repository.WeatherRepository;
import org.spring.backendprojectex.open.weather.service.WeatherService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class WeatherServiceImpl implements WeatherService {
    private final WeatherRepository weatherRepository;
    //JSON -> JAVA
    private final ObjectMapper objectMapper;

    @Override
    public void insertWeather(String responseBody) {

        try{
        WeatherApiDto weatherApiDto=objectMapper.readValue(responseBody,WeatherApiDto.class);

        log.info("API 응답 완료:{}",weatherApiDto);

            Optional<WeatherEntity> weatherEntity=weatherRepository.findByName(weatherApiDto.getName());
            if(!weatherEntity.isPresent()){
                WeatherEntity newEntity= WeatherEntity.builder()
                        .name(weatherApiDto.getName())
                        .lat(weatherApiDto.getCoord().getLat())
                        .lon(weatherApiDto.getCoord().getLon())
                        .icon(weatherApiDto.getWeather().get(0).getIcon())
                        .temp_max(weatherApiDto.getMain().getTemp_max())
                        .temp_min(weatherApiDto.getMain().getTemp_min())
                        .country(weatherApiDto.getSys().getCountry())
                        .build();
                weatherRepository.save(newEntity);
                log.info("새로운 날씨 정보 저장 완료: {}",newEntity);
            }else{
                log.info("이미 저장된 날씨 정보 존재 - 업데이트 또는 무시 가능");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("API 응답 에러");
        }


    }
}
