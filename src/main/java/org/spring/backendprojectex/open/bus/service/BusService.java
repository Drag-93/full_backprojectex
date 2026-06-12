package org.spring.backendprojectex.open.bus.service;

import org.spring.backendprojectex.open.bus.dto.busList.BusDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BusService {
    @Transactional
    void insertBusList(String busListResult);

    List<BusDto> busListFn(String searchVal);
}
