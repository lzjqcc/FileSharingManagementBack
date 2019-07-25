package com.Loser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SharefileApplication {

	public static void main(String[] args) {
		SpringApplication.run(SharefileApplication.class, args);
	}

}
