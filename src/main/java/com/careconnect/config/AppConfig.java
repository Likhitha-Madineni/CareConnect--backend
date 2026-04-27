package com.careconnect.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper m = new ModelMapper();
		m.getConfiguration().setSkipNullEnabled(true);
		return m;
	}
}
