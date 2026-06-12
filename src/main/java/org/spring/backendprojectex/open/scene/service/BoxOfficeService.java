package org.spring.backendprojectex.open.scene.service;

import org.spring.backendprojectex.open.scene.dto.boxoffice.BoxOfficeDto;

import java.util.List;

public interface BoxOfficeService {
    List<BoxOfficeDto> insertBoxOfficeBody(String boxOfficeBody);


    BoxOfficeDto boxOfficeMovieInfoJava(String movieCd);
}
