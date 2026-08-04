package com.gl.booking_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="resource-service")
public interface ResourceClient {


    @GetMapping("/api/resources/{id}")
    Object getResource(
            @PathVariable Long id
    );

}
