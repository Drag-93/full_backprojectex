package org.spring.backendprojectex.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration //스프링 설정     //외부에서 로컬의 특정 폴더에 접근
public class WebMvcConfigClass implements WebMvcConfigurer {
    @Value("${img.path.item}")
    private String itemImgPath;

    @Value("${img.path.community}")
    private String communityImgPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        //file: //경로에서 "file://"부분을 제거
        String itemPath=itemImgPath.replace("file://","");
        String communityPath=communityImgPath.replace("file://","");

        //리소스 핸들러 설정
        registry.addResourceHandler("/backend/item/**")
                .addResourceLocations("file:"+itemPath); //실제 이미지 경로 설정
        registry.addResourceHandler("/backend/community/**")
                .addResourceLocations("file:"+communityPath); //실제 이미지 경로 설정
    }

}
