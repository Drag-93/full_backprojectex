package org.spring.backendprojectex.open.bus.dto.busStation;

import lombok.Data;

@Data
public class BusStationResultReson {

    private ComMsgHeader comMsgHeader;
    private MsgHeader msgHeader;
    private MsgBody msgBody;
}
