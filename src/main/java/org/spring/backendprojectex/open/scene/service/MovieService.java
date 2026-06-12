package org.spring.backendprojectex.open.scene.service;


import org.spring.backendprojectex.open.scene.dto.movie.MovieDto;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface MovieService {
    List<MovieDto> insertResponseBody(String responseBody);

    MovieDto movieInfoJava(String movieCd);

    Page<MovieDto> getMovieList(Pageable pageable, String subject, String search);
}
