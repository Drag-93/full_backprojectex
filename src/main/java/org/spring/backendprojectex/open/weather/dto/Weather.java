package org.spring.backendprojectex.open.weather.dto;

import lombok.Data;

@Data
public class Weather {

    private String description;
    private String icon;
    private String id;
    private String main;

}
