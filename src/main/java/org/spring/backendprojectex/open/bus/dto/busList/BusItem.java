package org.spring.backendprojectex.open.bus.dto.busList;

import lombok.Data;

@Data
public class BusItem {
    private String busRouteId;
    private String busRouteNm;
    private String busRouteAbrv;
    private String length;
    private String routeType;
    private String stStationNm;
    private String edStationNm;
    private String term;
    private String lastBusYn;
    private String firstBusTm;
    private String lastBusTm;
    private String firstLowTm;
    private String lastLowTm;
    private String corpNm;
}
