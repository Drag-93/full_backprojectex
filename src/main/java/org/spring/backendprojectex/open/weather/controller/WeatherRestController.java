package org.spring.backendprojectex.open.weather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.open.util.OpenApiUtil;
import org.spring.backendprojectex.open.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/open/weather")
@RequiredArgsConstructor
@Tag(name = "Weather API", description = "OpenWeatherMap API 연동 및 날씨 데이터 처리 API")
public class WeatherRestController {
    private final WeatherService weatherService;

    @Value("${open.openWeatherMap.serviceKey}")
    private String key;

    @Operation(
            summary = "도시별 현재 날씨 및 저장",
            description = "도시명을 입력하면 Openweathermap API에서 도시의 현재 날씨정보를 조회 후 DB에 저장")
    @GetMapping("/search/{q}")
    public ResponseEntity<Map<String, String>> search(@PathVariable("q") String q) {
        //URL
        String apiURL = "https://api.openweathermap.org/data/2.5/weather?q=" + q + "&appid=" + key;
        //Header
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Content-type", "application/json");//Json

        String responseBody = OpenApiUtil.get(apiURL, requestHeaders);
        System.out.println(responseBody + "<< responseBody");

        //JSON -> JAVA변경 -> DTO에 추가 -> Entity -> DB저장
        weatherService.insertWeather(responseBody);

        Map<String, String> weather = new HashMap<>();
        weather.put("weather", responseBody);  //"weather"이름으로

        return ResponseEntity.status(HttpStatus.OK).body(weather);
    }
}
