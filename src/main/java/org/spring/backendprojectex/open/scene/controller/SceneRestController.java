package org.spring.backendprojectex.open.scene.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.open.scene.dto.boxoffice.BoxOfficeDto;
import org.spring.backendprojectex.open.scene.dto.movie.MovieDto;
import org.spring.backendprojectex.open.scene.service.BoxOfficeService;
import org.spring.backendprojectex.open.scene.service.MovieService;
import org.spring.backendprojectex.open.util.OpenApiUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/open/scene")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "영화진흥위원회 API", description = "영화진흥위원회 API 연동 및 영화 데이터 처리 API")
public class SceneRestController {
    private final MovieService movieService;
    private final BoxOfficeService boxOfficeService;

    @Value("${open.kobis.servicekey}")
    private String key;

    @Operation(
            summary = "현재일 기준 영화 목록",
            description ="현재일 기준 개봉 및 개봉예정 영화 목록을 조회 후 DB에 저장")
    @GetMapping("/movieList")
    public ResponseEntity<Map<String,Object>> movieList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue ="10") int size
    ){
        String movieSearch="searchMovieList.json"; //가져올 내용
        String itemPerPage="100";
        String openStartDt="2026";
        String apiURL = "https://kobis.or.kr/kobisopenapi/webservice/rest/movie/"+ movieSearch +"?key="+key + "&itemPerPage="+itemPerPage + "&openStartDt="+openStartDt;
        Map<String, String> requestHeaders=new HashMap<>();
        requestHeaders.put("Content-type","application/json");
        String responseBody= OpenApiUtil.get(apiURL,requestHeaders);
        List<MovieDto> movieDtos=movieService.insertResponseBody(responseBody);

        int totalCount = movieDtos.size();
        int totalPages = (int) Math.ceil((double) totalCount / size);

        int start = (page - 1) * size;
        int end = Math.min(start + size, totalCount);

        List<MovieDto> pageList = movieDtos.subList(start, end);

        Map<String, Object> result = new HashMap<>();
        result.put("movie", pageList);
        result.put("currentPage", page);
        result.put("pageSize", size);
        result.put("totalCount", totalCount);
        result.put("totalPages", totalPages);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "선택한 영화의 상세정보 조회",
            description ="선택한 영화의 개봉일, 국적 등 상세정보를 DB에서 조회")
    @GetMapping("/movieDetailJava/{movieCd}")
    public ResponseEntity<Map<String,String>> movieDetail(@PathVariable("movieCd")String movieCd){
        String apiURL= "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key="+key+"&movieCd="+movieCd;
        Map<String,String> requestHeaders=new HashMap<>();
        requestHeaders.put("Content-type","applicataion/json");
        String responseBody=OpenApiUtil.get(apiURL,requestHeaders);
//        System.out.println(requestHeaders);
//        System.out.println(responseBody);
        Map<String,String>movie=new HashMap<>();
        movie.put("movie",responseBody);
        System.out.println(responseBody+"<<<responseBody(Detail)");

        return ResponseEntity.status(HttpStatus.OK).body(movie);

    }

    @Operation(
            summary = "영화 박스오피스 목록 조회",
            description ="선택한 날짜 기준 영화진흥위원회에서 선정한 박스오피스 목록 조회 후 DB에 정보 저장")
    @GetMapping("/boxOffice/{targetDt}/{weekGb}")
    public ResponseEntity< Map<String, List<BoxOfficeDto>>> boxOfficeJava(@PathVariable("targetDt") String targetDt,
                                                                          @PathVariable("weekGb")String weekGb){
        log.info("===========targetDt: "+targetDt+"===========");
        log.info("===========weekGb: "+weekGb+"===========");
        String movieSearch="searchWeeklyBoxOfficeList.json"; // 가져올 내용
        String apiURL= "https://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/"+movieSearch+"?key="+key+"&targetDt="+targetDt+"&weekGb="+weekGb;
        Map<String, String> requestHeaders= new HashMap<>();
        requestHeaders.put("Content-Type", "application/json"); //JSON데이터

        String responseBody=OpenApiUtil.get(apiURL,requestHeaders);

        log.info(responseBody);

        List<BoxOfficeDto> boxOfficeResult=boxOfficeService.insertBoxOfficeBody(responseBody);

        Map<String, List<BoxOfficeDto>>map=new HashMap<>();
        map.put("boxOfficeResult", boxOfficeResult);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
    //DB데이터 get
    @Operation(
            summary = "선택한 박스오피스 영화의 상세정보 조회",
            description ="선택한 박스오피스 영화의 상세정보를 DB에서 조회")
    @GetMapping("/boxOfficeDetailJava/{movieCd}")
    public ResponseEntity<Map<String, BoxOfficeDto>>boxOfficeDetailJava(@PathVariable("movieCd")String movieCd){
        BoxOfficeDto boxOfficeDto=boxOfficeService.boxOfficeMovieInfoJava(movieCd);
        Map<String, BoxOfficeDto> movie = new HashMap<>();
        movie.put("movie", boxOfficeDto);
        return ResponseEntity.status(HttpStatus.OK).body(movie);
    }
}

