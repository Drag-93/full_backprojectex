package org.spring.backendprojectex.open.weather.dto;

import lombok.Data;

@Data
public class Main {
    private String feels_like;
    private String grnd_level;
    private String humidity;
    private String pressure;
    private String sea_level;
    private String temp;
    private String temp_max;
    private String temp_min;
}
