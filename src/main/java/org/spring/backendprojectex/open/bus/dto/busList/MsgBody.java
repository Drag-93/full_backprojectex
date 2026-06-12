package org.spring.backendprojectex.open.bus.dto.busList;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MsgBody {
    private List<BusItem> itemList;
}
