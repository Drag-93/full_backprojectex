package org.spring.backendprojectex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //공용 시간 설정
public class BackendprojectexApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendprojectexApplication.class, args);
	}

}
