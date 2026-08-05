package com.gl.api_gateway.config;




import lombok.RequiredArgsConstructor;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter {


    private final JwtService jwtService;



    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ){


        String path =
                exchange.getRequest()
                        .getURI()
                        .getPath();



        // Allow login and register

        if(path.equals("/users/login")
                ||
                path.equals("/users/register")){


            return chain.filter(exchange);

        }



        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(
                                HttpHeaders.AUTHORIZATION
                        );



        if(authHeader == null ||
                !authHeader.startsWith("Bearer ")){

            exchange.getResponse()
                    .setStatusCode(
                            HttpStatus.UNAUTHORIZED
                    );


            return exchange.getResponse()
                    .setComplete();

        }



        String token =
                authHeader.substring(7);



        if(!jwtService.isValid(token)){


            exchange.getResponse()
                    .setStatusCode(
                            HttpStatus.UNAUTHORIZED
                    );


            return exchange.getResponse()
                    .setComplete();

        }



        Long userId = jwtService.extractUserId(token);

        ServerWebExchange modifiedExchange =
                exchange.mutate()
                        .request(builder ->
                                builder.header("X-User-Id", userId.toString()))
                        .build();

        return chain.filter(modifiedExchange);

    }

}
