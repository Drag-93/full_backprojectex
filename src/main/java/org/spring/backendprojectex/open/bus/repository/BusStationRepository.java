package org.spring.backendprojectex.open.bus.repository;

import org.spring.backendprojectex.open.bus.entity.BusStationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusStationRepository extends JpaRepository<BusStationEntity,Long> {
    Optional<BusStationEntity> findByBusRouteIdAndStationNo(String busRouteId, String stationNo);

    List<BusStationEntity> findByBusRouteId(String busRouteId);
}
