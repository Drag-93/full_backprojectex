package org.spring.backendprojectex.open.bus.dto.busStation;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MsgBody {
    private List<BusItemStation> itemList;
}
