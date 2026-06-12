package org.spring.backendprojectex.open.scene.dto.movie;

import lombok.Data;

import java.util.List;
@Data
public class MovieItem {
    private List<Company> companys;
    private List<Director> directors;
    private String genreAlt;
    private String movieCd;
    private String movieNm;
    private String movieNmEn;
    private String nationAlt;
    private String openDt;
    private String prdtStatNm;
    private String prdtYear;
    private String repGenreNm;
    private String repNationNm;
    private String typeNm;
}
