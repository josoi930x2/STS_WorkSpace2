package net.datasa.web5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Web5_45Application {

	public static void main(String[] args) {
		SpringApplication.run(Web5_45Application.class, args);
	}

}
