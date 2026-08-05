package com.gl.transaction_service.controller;



import com.gl.transaction_service.dto.ReturnResourceRequest;
import com.gl.transaction_service.dto.TransactionRequest;
import com.gl.transaction_service.entity.Transaction;
import com.gl.transaction_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @RequestBody TransactionRequest request) {

        return new ResponseEntity<>(
                transactionService.createTransaction(request),
                HttpStatus.CREATED
        );
    }


    @PutMapping("/return")
    public ResponseEntity<Transaction> returnResource(
            @RequestBody ReturnResourceRequest request) {

        return ResponseEntity.ok(
                transactionService.returnResource(request)
        );
    }


    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransactionById(
            @PathVariable Long transactionId) {

        return ResponseEntity.ok(
                transactionService.getTransactionById(transactionId)
        );
    }


    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<Transaction> getTransactionByBookingId(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                transactionService.getTransactionByBookingId(bookingId)
        );
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                transactionService.getTransactionsByUser(userId)
        );
    }


    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Transaction>> getTransactionsByOwner(
            @PathVariable Long ownerId) {

        return ResponseEntity.ok(
                transactionService.getTransactionsByOwner(ownerId)
        );
    }


    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {

        return ResponseEntity.ok(
                transactionService.getAllTransactions()
        );
    }

}
