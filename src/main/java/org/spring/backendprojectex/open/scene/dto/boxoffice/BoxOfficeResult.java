package org.spring.backendprojectex.open.scene.dto.boxoffice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoxOfficeResult {
    private BoxOfficeType boxofficeType;
    private ShowRange showRange;
    private List<WeeklyBoxOfficeList> weeklyBoxOfficeList;
    private YearWeekTime yearWeekTime;


}
