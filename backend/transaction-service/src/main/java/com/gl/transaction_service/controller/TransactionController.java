package com.gl.transaction_service.controller;


import com.gl.transaction_service.dto.TransactionRequest;
import com.gl.transaction_service.dto.TransactionResponse;

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


    // =================================================
    // CREATE TRANSACTION
    // Called by booking-service after approval
    // =================================================

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @RequestBody TransactionRequest request
    ) {

        return new ResponseEntity<>(

                transactionService
                        .createTransaction(request),

                HttpStatus.CREATED
        );
    }


    // =================================================
    // RENTER CLICKS PAID
    // =================================================

    @PutMapping("/{transactionId}/paid")
    public ResponseEntity<Transaction> markPaid(
            @PathVariable Long transactionId
    ) {

        return ResponseEntity.ok(

                transactionService
                        .markPaid(transactionId)
        );
    }


    // =================================================
    // RENTER CLICKS PRODUCT RETURNED
    // =================================================

    @PutMapping("/{transactionId}/returned")
    public ResponseEntity<Transaction> markReturned(
            @PathVariable Long transactionId
    ) {

        return ResponseEntity.ok(

                transactionService
                        .markReturned(transactionId)
        );
    }


    // =================================================
    // OWNER CLICKS SECURITY AMOUNT RETURNED
    // =================================================

    @PutMapping("/{transactionId}/security-returned")
    public ResponseEntity<Transaction> markSecurityReturned(
            @PathVariable Long transactionId
    ) {

        return ResponseEntity.ok(

                transactionService
                        .markSecurityReturned(
                                transactionId
                        )
        );
    }


    // =================================================
    // GET TRANSACTION BY ID
    // =================================================

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse>
    getTransactionById(
            @PathVariable Long transactionId
    ) {

        return ResponseEntity.ok(

                transactionService
                        .getTransactionById(
                                transactionId
                        )
        );
    }


    // =================================================
    // GET TRANSACTION BY BOOKING ID
    // =================================================

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<TransactionResponse>
    getTransactionByBookingId(
            @PathVariable Long bookingId
    ) {

        return ResponseEntity.ok(

                transactionService
                        .getTransactionByBookingId(
                                bookingId
                        )
        );
    }


    // =================================================
    // MY TRANSACTIONS
    // RENTER SIDE
    // =================================================

    @GetMapping("/my")
    public ResponseEntity<List<TransactionResponse>>
    getMyTransactions(

            @RequestHeader("X-User-Id")
            Long userId

    ) {

        return ResponseEntity.ok(

                transactionService
                        .getTransactionsByUser(
                                userId
                        )
        );
    }


    // =================================================
    // OWNER TRANSACTIONS
    // =================================================

    @GetMapping("/owned")
    public ResponseEntity<List<TransactionResponse>>
    getOwnerTransactions(

            @RequestHeader("X-User-Id")
            Long ownerId

    ) {

        return ResponseEntity.ok(

                transactionService
                        .getTransactionsByOwner(
                                ownerId
                        )
        );
    }


    // =================================================
    // GET ALL TRANSACTIONS
    // =================================================

    @GetMapping
    public ResponseEntity<List<TransactionResponse>>
    getAllTransactions() {

        return ResponseEntity.ok(

                transactionService
                        .getAllTransactions()
        );
    }
}