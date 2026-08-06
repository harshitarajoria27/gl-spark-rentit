package com.gl.transaction_service.client;



import com.gl.transaction_service.dto.ResourceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "resource-service")
public interface ResourceClient {

    @GetMapping("/resources/{id}")
    ResourceResponse getResourceById(
            @PathVariable("id") Long id
    );
}
