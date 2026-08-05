package com.gl.transaction_service.service;

import com.gl.transaction_service.dto.ReturnResourceRequest;
import com.gl.transaction_service.dto.TransactionRequest;
import com.gl.transaction_service.entity.PaymentStatus;
import com.gl.transaction_service.entity.Transaction;
import com.gl.transaction_service.entity.TransactionStatus;
import com.gl.transaction_service.exception.ResourceNotFoundException;
import com.gl.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(TransactionRequest request) {

        int rentalDays = (int) ChronoUnit.DAYS.between(
                request.getBookingDate(),
                request.getExpectedReturnDate());

        if (rentalDays <= 0) {
            rentalDays = 1;
        }

        double totalRent = rentalDays * request.getRentPerDay();

        Transaction transaction = Transaction.builder()
                .bookingId(request.getBookingId())
                .resourceId(request.getResourceId())
                .renterId(request.getRenterId())
                .ownerId(request.getOwnerId())
                .securityDeposit(request.getSecurityDeposit())
                .rentPerDay(request.getRentPerDay())
                .rentalDays(rentalDays)
                .totalRent(totalRent)
                .damageCharges(0.0)
                .refundAmount(0.0)
                .bookingDate(request.getBookingDate())
                .expectedReturnDate(request.getExpectedReturnDate())
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(PaymentStatus.PAID)
                .status(TransactionStatus.ACTIVE)
                .build();

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction returnResource(ReturnResourceRequest request) {

        Transaction transaction = transactionRepository
                .findByBookingId(request.getBookingId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transaction not found with Booking ID : "
                                        + request.getBookingId()));

        transaction.setActualReturnDate(request.getActualReturnDate());

        long actualRentalDays = ChronoUnit.DAYS.between(
                transaction.getBookingDate(),
                request.getActualReturnDate());

        if (actualRentalDays <= 0) {
            actualRentalDays = 1;
        }

        double totalRent =
                actualRentalDays * transaction.getRentPerDay();

        transaction.setRentalDays((int) actualRentalDays);
        transaction.setTotalRent(totalRent);

        // Damage charges sent by owner
        double damageCharges = request.getDamageCharges();

        transaction.setDamageCharges(damageCharges);

        double refund =
                transaction.getSecurityDeposit() - damageCharges;

        if (refund < 0) {
            refund = 0;
        }

        transaction.setRefundAmount(refund);

        transaction.setPaymentStatus(PaymentStatus.REFUNDED);

        transaction.setStatus(TransactionStatus.COMPLETED);

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(Long transactionId) {

        return transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transaction not found with id : "
                                        + transactionId));
    }

    @Override
    public Transaction getTransactionByBookingId(Long bookingId) {

        return transactionRepository.findByBookingId(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transaction not found with booking id : "
                                        + bookingId));
    }

    @Override
    public List<Transaction> getTransactionsByUser(Long userId) {

        return transactionRepository.findByRenterId(userId);
    }

    @Override
    public List<Transaction> getTransactionsByOwner(Long ownerId) {

        return transactionRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Transaction> getAllTransactions() {

        return transactionRepository.findAll();
    }
}