package com.gl.transaction_service.service;

import com.gl.transaction_service.client.ResourceClient;
import com.gl.transaction_service.client.UserClient;

import com.gl.transaction_service.dto.ResourceResponse;
import com.gl.transaction_service.dto.TransactionRequest;
import com.gl.transaction_service.dto.TransactionResponse;
import com.gl.transaction_service.dto.UserResponse;

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

    private final ResourceClient resourceClient;

    private final UserClient userClient;


    // =================================================
    // CREATE TRANSACTION
    // Called when booking is approved
    // =================================================

    @Override
    public Transaction createTransaction(
            TransactionRequest request
    ) {

        int rentalDays = (int) ChronoUnit.DAYS.between(
                request.getBookingDate(),
                request.getExpectedReturnDate()
        );

        if (rentalDays <= 0) {
            rentalDays = 1;
        }


        double totalRent =
                rentalDays * request.getRentPerDay();


        Transaction transaction =
                Transaction.builder()

                        .bookingId(
                                request.getBookingId()
                        )

                        .resourceId(
                                request.getResourceId()
                        )

                        .renterId(
                                request.getRenterId()
                        )

                        .ownerId(
                                request.getOwnerId()
                        )

                        .securityDeposit(
                                request.getSecurityDeposit()
                        )

                        .rentPerDay(
                                request.getRentPerDay()
                        )

                        .rentalDays(
                                rentalDays
                        )

                        .totalRent(
                                totalRent
                        )

                        .bookingDate(
                                request.getBookingDate()
                        )

                        .expectedReturnDate(
                                request.getExpectedReturnDate()
                        )

                        .paymentStatus(
                                PaymentStatus.PENDING
                        )

                        .status(
                                TransactionStatus.ACTIVE
                        )

                        // Initial checkbox values

                        .rentPaid(false)

                        .securityDepositPaid(false)

                        .resourceCollected(false)

                        .resourceReturned(false)

                        .securityDepositReturned(false)

                        .build();


        return transactionRepository.save(
                transaction
        );
    }


    // =================================================
    // RENTER CLICKS PAID
    // =================================================

    @Override
    public Transaction markPaid(
            Long transactionId
    ) {

        Transaction transaction =
                transactionRepository
                        .findById(transactionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found with id : "
                                                + transactionId
                                )
                        );


        transaction.setRentPaid(true);

        transaction.setSecurityDepositPaid(true);

        transaction.setResourceCollected(true);

        transaction.setPaymentStatus(
                PaymentStatus.PAID
        );


        return transactionRepository.save(
                transaction
        );
    }


    // =================================================
    // RENTER CLICKS PRODUCT RETURNED
    // =================================================

    @Override
    public Transaction markReturned(
            Long transactionId
    ) {

        Transaction transaction =
                transactionRepository
                        .findById(transactionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found with id : "
                                                + transactionId
                                )
                        );


        transaction.setResourceReturned(
                true
        );


        return transactionRepository.save(
                transaction
        );
    }


    // =================================================
    // OWNER CLICKS SECURITY AMOUNT RETURNED
    // =================================================

    @Override
    public Transaction markSecurityReturned(
            Long transactionId
    ) {

        Transaction transaction =
                transactionRepository
                        .findById(transactionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found with id : "
                                                + transactionId
                                )
                        );


        transaction.setSecurityDepositReturned(
                true
        );


        transaction.setPaymentStatus(
                PaymentStatus.REFUNDED
        );


        transaction.setStatus(
                TransactionStatus.COMPLETED
        );


        return transactionRepository.save(
                transaction
        );
    }


    // =================================================
    // GET TRANSACTION BY ID
    // =================================================

    @Override
    public TransactionResponse getTransactionById(
            Long transactionId
    ) {

        Transaction transaction =
                transactionRepository
                        .findById(transactionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found with id : "
                                                + transactionId
                                )
                        );


        return convertToResponse(
                transaction
        );
    }


    // =================================================
    // GET TRANSACTION BY BOOKING ID
    // =================================================

    @Override
    public TransactionResponse getTransactionByBookingId(
            Long bookingId
    ) {

        Transaction transaction =
                transactionRepository
                        .findByBookingId(bookingId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found with booking id : "
                                                + bookingId
                                )
                        );


        return convertToResponse(
                transaction
        );
    }


    // =================================================
    // MY TRANSACTIONS
    // RENTER SIDE
    // =================================================

    @Override
    public List<TransactionResponse>
    getTransactionsByUser(
            Long userId
    ) {

        return transactionRepository
                .findByRenterId(userId)

                .stream()

                .map(this::convertToResponse)

                .toList();
    }


    // =================================================
    // OWNER TRANSACTIONS
    // =================================================

    @Override
    public List<TransactionResponse>
    getTransactionsByOwner(
            Long ownerId
    ) {

        return transactionRepository
                .findByOwnerId(ownerId)

                .stream()

                .map(this::convertToResponse)

                .toList();
    }


    // =================================================
    // GET ALL TRANSACTIONS
    // =================================================

    @Override
    public List<TransactionResponse>
    getAllTransactions() {

        return transactionRepository
                .findAll()

                .stream()

                .map(this::convertToResponse)

                .toList();
    }


    // =================================================
    // ENTITY -> RESPONSE DTO
    // =================================================

    private TransactionResponse convertToResponse(
            Transaction transaction
    ) {


        // ---------------------------------------------
        // GET RESOURCE
        // ---------------------------------------------

        ResourceResponse resource =
                resourceClient.getResourceById(
                        transaction.getResourceId()
                );


        // ---------------------------------------------
        // GET OWNER
        // ---------------------------------------------

        UserResponse owner =
                userClient.getUserById(
                        transaction.getOwnerId()
                );


        // ---------------------------------------------
        // GET BORROWER / RENTER
        // ---------------------------------------------

        UserResponse borrower =
                userClient.getUserById(
                        transaction.getRenterId()
                );


        // ---------------------------------------------
        // BUILD RESPONSE
        // ---------------------------------------------

        return TransactionResponse.builder()

                // Transaction IDs

                .transactionId(
                        transaction.getTransactionId()
                )

                .bookingId(
                        transaction.getBookingId()
                )

                .resourceId(
                        transaction.getResourceId()
                )

                .renterId(
                        transaction.getRenterId()
                )

                .ownerId(
                        transaction.getOwnerId()
                )


                // Rent

                .rentPerDay(
                        transaction.getRentPerDay()
                )

                .rentalDays(
                        transaction.getRentalDays()
                )

                .totalRent(
                        transaction.getTotalRent()
                )

                .securityDeposit(
                        transaction.getSecurityDeposit()
                )


                // Payment

                .paymentStatus(
                        transaction.getPaymentStatus()
                )


                // Dates

                .bookingDate(
                        transaction.getBookingDate()
                )

                .expectedReturnDate(
                        transaction.getExpectedReturnDate()
                )


                // Status

                .status(
                        transaction.getStatus()
                )


                // Checkboxes

                .rentPaid(
                        transaction.getRentPaid()
                )

                .securityDepositPaid(
                        transaction.getSecurityDepositPaid()
                )

                .resourceCollected(
                        transaction.getResourceCollected()
                )

                .resourceReturned(
                        transaction.getResourceReturned()
                )

                .securityDepositReturned(
                        transaction.getSecurityDepositReturned()
                )


                // =====================================
                // DISPLAY INFORMATION
                // =====================================

                .resourceName(
                        resource.getTitle()
                )

                .ownerName(
                        owner.getFullName()
                )

                .borrowerName(
                        borrower.getFullName()
                )


                .build();
    }
}