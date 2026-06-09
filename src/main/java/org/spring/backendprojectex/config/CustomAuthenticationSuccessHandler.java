package org.spring.backendprojectex.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)throws IOException, ServletException {
        log.info("=========authentication=========");
        log.info(authentication);
        log.info("=========Principal=========");
        log.info(authentication.getPrincipal());
        log.info("=========Authorities=========");
        log.info(authentication.getAuthorities());


        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();

        out.println("<script> alert('"+authentication.getName()+"님 반갑습니다');"+" location.href='/';" + "</script>);");
        out.close();
    }
}

