package org.spring.backendprojectex.open.scene.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.open.scene.dto.movie.MovieDto;
import org.spring.backendprojectex.open.scene.dto.movie.MovieItem;
import org.spring.backendprojectex.open.scene.dto.movie.MovieListResponse;
import org.spring.backendprojectex.open.scene.entity.movie.MovieEntity;
import org.spring.backendprojectex.open.scene.repository.MovieRepository;
import org.spring.backendprojectex.open.scene.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    @Override
    public List<MovieDto> insertResponseBody(String responseBody) {
        ObjectMapper objectMapper=new ObjectMapper();
        MovieListResponse movieListResponse=null;
        try{
            //JSON 문자열을 Java객체로 변환
            movieListResponse=objectMapper.readValue(responseBody, MovieListResponse.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        if(movieListResponse==null||movieListResponse.getMovieListResult()==null){
            return null;
        }
        List<MovieItem>movieItems=movieListResponse.getMovieListResult().getMovieList();
        AtomicReference<String> companyCd=new AtomicReference<>("");
        AtomicReference<String> companyNm=new AtomicReference<>("");
        for(MovieItem movieItem:movieItems){
            //회사 정보가 여러 개일 경우 마지막 값을 사용
            movieItem.getCompanys().forEach(company -> {
                companyCd.set(company.getCompanyCd());
                companyNm.set(company.getCompanyNm());
            });
            Optional<MovieEntity> movieEntity1=movieRepository.findByMovieCd(movieItem.getMovieCd());
            if(!movieEntity1.isPresent()){
                MovieEntity movieEntity=MovieEntity.builder()
                        .movieCd(movieItem.getMovieCd())
                        .movieNm(movieItem.getMovieNm())
                        .movieNmEn(movieItem.getMovieNmEn())
                        .prdtYear(movieItem.getPrdtYear())
                        .repNationNm(movieItem.getRepNationNm())
                        .typeNm(movieItem.getTypeNm())
                        .openDt(movieItem.getOpenDt())
                        .genreAlt(movieItem.getGenreAlt())
                        .prdtStatNm(movieItem.getPrdtStatNm())
                        .getCompanyCd(companyCd.get())
                        .getCompanyNm(companyNm.get())
                        .build();
                movieRepository.save(movieEntity);
            }
        }
        List<MovieEntity> movieEntities=movieRepository.findAll();
        if(movieEntities.isEmpty()){
            throw new NullPointerException("목록이 없습니다.");
        }
        String totCnt=movieListResponse.getMovieListResult().getTotCnt();
        String source=movieListResponse.getMovieListResult().getSource();
        return movieEntities.stream().map(el->MovieDto.builder()
                .id(el.getId())
                .movieCd(el.getMovieCd())
                .movieNm(el.getMovieNm())
                .movieNmEn(el.getMovieNmEn())
                .prdtYear(el.getPrdtYear())
                .repGenreNm(el.getRepGenreNm())
                .repNationNm(el.getRepNationNm())
                .typeNm(el.getTypeNm())
                .openDt(el.getOpenDt())
                .genreAlt(el.getGenreAlt())
                .prdtStatNm(el.getPrdtStatNm())
                .getCompanyCd(el.getGetCompanyCd())
                .getCompanyNm(el.getGetCompanyNm())
                .source(source)
                .totCnt(totCnt)
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public MovieDto movieInfoJava(String movieCd) {
        return null;
    }

    @Override
    public Page<MovieDto> getMovieList(Pageable pageable, String subject, String search) {
        return null;
    }
}
