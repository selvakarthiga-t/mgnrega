package com.ourvoice.mgnrega;

// package...
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // Scheduled job-a enable panna
@EnableCaching     // @Cacheable-a enable panna
public class MgnregaApplication {
	public static void main(String[] args) {
		SpringApplication.run(MgnregaApplication.class, args);
	}
}
