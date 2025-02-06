package com.multipledb.multipledb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MultipledbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultipledbApplication.class, args);
	}

}
