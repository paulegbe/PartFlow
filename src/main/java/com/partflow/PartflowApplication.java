package com.partflow;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.partflow"})
@EnableJpaRepositories(basePackages = "com.partflow.repository")
@EntityScan(basePackages = "com.partflow.model")
public class PartflowApplication {

	public static void main(String[] args) {
		Application.launch(PartflowFXApp.class, args);
	}

}
