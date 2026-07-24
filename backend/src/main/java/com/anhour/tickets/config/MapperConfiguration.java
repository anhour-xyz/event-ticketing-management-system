package com.anhour.tickets.config;

import com.anhour.tickets.mappers.EventMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public EventMapper eventMapper() {
        return Mappers.getMapper(EventMapper.class);
    }
}
