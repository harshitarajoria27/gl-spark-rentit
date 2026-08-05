package com.gl.transaction_service.service;



import com.gl.transaction_service.dto.ReturnResourceRequest;
import com.gl.transaction_service.dto.TransactionRequest;
import com.gl.transaction_service.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(TransactionRequest request);

    Transaction returnResource(ReturnResourceRequest request);

    Transaction getTransactionById(Long transactionId);

    Transaction getTransactionByBookingId(Long bookingId);

    List<Transaction> getTransactionsByUser(Long userId);

    List<Transaction> getTransactionsByOwner(Long ownerId);

    List<Transaction> getAllTransactions();

}
