package org.spring.backendprojectex.open.bus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.open.bus.dto.busList.BusDto;
import org.spring.backendprojectex.open.bus.dto.busStation.BusStationDto;
import org.spring.backendprojectex.open.bus.service.BusService;
import org.spring.backendprojectex.open.bus.service.BusStationService;
import org.spring.backendprojectex.open.util.OpenApiUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open/bus")
@Log4j2
public class BusRestController {
    private final BusService busService;
    private final BusStationService busStationService;

    @Value("${open.data.bus.servicekey}")
    private String key;

    @GetMapping("/busList")
    public ResponseEntity<?> busList(@RequestParam(required = false) String searchVal) throws IOException{

        String busListResult= OpenApiUtil.getBusList(key,searchVal);
        log.info("==busListResult"+busListResult+"==");
        //DB저장
        busService.insertBusList(busListResult);
        //DB저장 정보 get
        List<BusDto> busDtoList=busService.busListFn(searchVal);
        //반환
        Map<String,Object> busMap=new HashMap<>();
        busMap.put("busList",busDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(busMap);
    }
    @GetMapping("/busStationList")
    public ResponseEntity<?>stationList(@RequestParam(required = false) String busRouteId) throws IOException {
        //공공데이터 API호출
        String stationListResult = OpenApiUtil.getStationList(key, busRouteId);
        log.info("==" + stationListResult + "==");
        //DB저장
        busStationService.insertBusStations(stationListResult);
        //DB저장 정보 get
        List<BusStationDto> busStationList = busStationService.busStationListFn(busRouteId);
        //반환
        Map<String, Object> busMap = new HashMap<>();
        //busMap.put("busList",busListResult);
        busMap.put("busStationList", busStationList);
        return ResponseEntity.status(HttpStatus.OK).body(busMap);
    }

}
