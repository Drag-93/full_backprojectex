package org.spring.backendprojectex.open.scene.dto.boxoffice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoxOfficeDto {
    private Long id;

    private String movieCd;
    private String movieNm;
    private String rank;
    private String openDt;
    private String audiAcc;
    private String salesAcc;
    private String boxofficeType;
    private String showRange;
    private String yearWeekTime;
}
