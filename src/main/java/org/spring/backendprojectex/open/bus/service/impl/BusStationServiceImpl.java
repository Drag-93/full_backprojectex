package org.spring.backendprojectex.open.bus.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.open.bus.dto.busStation.BusItemStation;
import org.spring.backendprojectex.open.bus.dto.busStation.BusStationDto;
import org.spring.backendprojectex.open.bus.dto.busStation.BusStationResultReson;
import org.spring.backendprojectex.open.bus.entity.BusStationEntity;
import org.spring.backendprojectex.open.bus.repository.BusStationRepository;
import org.spring.backendprojectex.open.bus.service.BusStationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusStationServiceImpl implements BusStationService {
    private final BusStationRepository busStationRepository;
    private final ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public void insertBusStations(String busStationList) {
        BusStationResultReson busStationResultReson=null;
        try{
            busStationResultReson=objectMapper.readValue(busStationList, BusStationResultReson.class);
            System.out.println(busStationResultReson);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("busStationResultReson = "
                + busStationResultReson);
        List<BusItemStation> busItemStations= busStationResultReson.getMsgBody().getItemList();
        System.out.println(busItemStations);
        for (BusItemStation item: busItemStations){
            BusStationEntity busDetailEntity = BusStationEntity.builder()
                    .busRouteId(item.getBusRouteId())
                    .beginTm(item.getBeginTm())
                    .lastTm(item.getLastTm())
                    .busRouteNm(item.getBusRouteNm())
                    .routeType(item.getRouteType())
                    .gpsX(item.getGpsX())
                    .gpsY(item.getGpsY())
                    .posX(item.getPosX())
                    .posY(item.getPosY())
                    .stationNm(item.getStationNm())
                    .stationNo(item.getStationNo())
                    .build();
            Optional<BusStationEntity> optionalBusDetailEntity=
                    busStationRepository.findByBusRouteIdAndStationNo(item.getBusRouteId(), item.getStationNo());
            if(optionalBusDetailEntity.isEmpty()){
                busStationRepository.save(busDetailEntity);
            }
        }
    }

    @Override
    public List<BusStationDto> busStationListFn(String busRouteId) {
        List<BusStationEntity> busStationEntities= busStationRepository.findByBusRouteId(busRouteId);
        return busStationEntities.stream().map(bus->BusStationDto.builder()
                .id(bus.getId())
                .busRouteId(bus.getBusRouteId())
                .busRouteNm(bus.getBusRouteNm())
                .gpsX(bus.getGpsX())
                .gpsY(bus.getGpsY())
                .stationNm(bus.getStationNm())
                .routeType(bus.getRouteType())
                .build()).collect(Collectors.toList());
    }
}
