package com.shu.tony.PlayTogether;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PlayTogetherApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlayTogetherApplication.class, args);
	}
}
