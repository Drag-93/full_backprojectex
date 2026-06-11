package org.spring.backendprojectex.open.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Rain {
    //지난 1시간 동안의 강우량, mm
    @JsonProperty("1h")
    private float rain1h;
    //지난 3시간 동안의 강우량, mm
    @JsonProperty("3h")
    private float rain3h;
}
