package com.gl.booking_service.client;



import com.gl.booking_service.dto.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "transaction-service",
        url = "http://localhost:8084"
)
public interface TransactionClient {

    @PostMapping("/transactions")
    void createTransaction(
            @RequestBody TransactionRequest request
    );
}
