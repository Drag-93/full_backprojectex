package org.spring.backendprojectex.open.weather.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="weather_tb06")
public class WeatherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="weather_id")
    private Long id;

    private String name;

    private String lat; //위도
    private String lon; //경도

    private String country; //국가

    private String temp_max; //최고온도
    private String temp_min; //최저온도

    private String icon; //이미지 아이콘

}
