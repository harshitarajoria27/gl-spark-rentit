package com.gl.api_gateway.config;



import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.cloud.gateway.filter.GlobalFilter;


@Configuration
@RequiredArgsConstructor
public class GatewayConfig {


    private final JwtAuthenticationFilter jwtFilter;



    @Bean
    public GlobalFilter authenticationFilter(){

        return jwtFilter::filter;

    }

}
