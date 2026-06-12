package org.spring.backendprojectex.open.scene.dto.movie;

import lombok.Data;

import java.util.List;

@Data
public class MovieListResult {

    private String source; //영화 소스 제공

    private String totCnt; //전체 영화 수

    private List<MovieItem> movieList; //실제 영화 리스트
}
