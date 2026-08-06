package com.gl.transaction_service.service;

import com.gl.transaction_service.dto.TransactionRequest;
import com.gl.transaction_service.dto.TransactionResponse;
import com.gl.transaction_service.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(
            TransactionRequest request
    );

    Transaction markPaid(
            Long transactionId
    );

    Transaction markReturned(
            Long transactionId
    );

    Transaction markSecurityReturned(
            Long transactionId
    );


    // GET METHODS

    TransactionResponse getTransactionById(
            Long transactionId
    );

    TransactionResponse getTransactionByBookingId(
            Long bookingId
    );

    List<TransactionResponse> getTransactionsByUser(
            Long userId
    );

    List<TransactionResponse> getTransactionsByOwner(
            Long ownerId
    );

    List<TransactionResponse> getAllTransactions();
}