package com.employee.advatixAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class AdvatixApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(AdvatixApiApplication.class, args);
	}

}
