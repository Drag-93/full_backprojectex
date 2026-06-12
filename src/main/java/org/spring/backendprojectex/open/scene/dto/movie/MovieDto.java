package org.spring.backendprojectex.open.scene.dto.movie;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "영화진흥위원회 API OpenAPI 연동을 위한 데이터 전용 객체(DTO)")
public class MovieDto {
    @Schema(description = "DB저장 아이디", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;
    private String movieCd;
    private String genreAlt;
    private String movieNm;
    private String movieNmEn;
    private String openDt;
    private String prdtStatNm;
    private String prdtYear;
    private String repGenreNm;
    private String repNationNm;
    private String typeNm;
    private String getCompanyCd;
    private String getCompanyNm;
    private String source;
    private String totCnt;
}
