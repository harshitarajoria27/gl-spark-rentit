package com.gl.booking_service.client;

import com.gl.booking_service.dto.ResourceResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "RESOURCE-SERVICE")
public interface ResourceClient {


    @GetMapping("/resources/{id}")
    ResourceResponse getResourceById(
            @PathVariable("id") Long resourceId
    );

}