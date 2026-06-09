package org.spring.backendprojectex.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigClass {
    private final MyOAuth2UserService myOAuth2UserService;
    private final CustomLogOutSuccessHandler customLogOutSuccessHandler;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;


    //시큐리티설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //1. csrf 설정
        //http.csrf(csrf->csrf.disable());
        http.csrf(AbstractHttpConfigurer::disable);

        //2. 페이지 설정
        http.authorizeHttpRequests(authorize ->
                authorize.requestMatchers("/member/join","/member/login").permitAll() //모든 접근
                        .requestMatchers("/member/logout","/member/detail").authenticated() //로그인 후
                        .requestMatchers("/admin/**").hasRole("ADMIN") // ADMIN권한
                        .anyRequest().permitAll()// 나머지 모두 허용
        );
        //3. 로그인 설정
        http.formLogin(login->
                login.loginPage("/member/login") //로그인 페이지 이동
                        .usernameParameter("userEmail") //로그인 아이디
                        .passwordParameter("userPw")    //로그인 비밀번호
                        .loginProcessingUrl("/member/login") //로그인 POST
//                        .defaultSuccessUrl("/") //로그인 성공시(나중에 핸들러 추가)
//                        .failureUrl("/") //로그인 실패시(나중에 핸들러 추가)
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)
                        .permitAll()//
                );
        //3-2 OAuth2 방식 로그인(SNS)
        http.oauth2Login(oauth2->oauth2
                .loginPage("/member/login") //로그인 페이지 이동
                .userInfoEndpoint(userInfo->
//                        userInfo.userService(myOAuth2UserService()))); //myOAuth2UserService로 이동해서 처리
                        userInfo.userService(myOAuth2UserService)));
        //4. 로그아웃
        http.logout(logout->
                logout.logoutUrl("/member/logout")
                        .logoutSuccessHandler(customLogOutSuccessHandler)
                        .permitAll()
        );
        return http.build();
    }
}
