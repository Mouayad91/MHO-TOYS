package com.mho_toys.backend.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    /**
     * Bean configuration for ModelMapper.
     * This allows for automatic mapping between DTOs and entities.
     *
     * @return a new instance of ModelMapper
     */

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
