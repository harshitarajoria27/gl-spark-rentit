package com.gl.transaction_service.client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "RESOURCE-SERVICE")
public interface ResourceClient {

    @GetMapping("/resources/{resourceId}")
    Object getResource(@PathVariable Long resourceId);

}
