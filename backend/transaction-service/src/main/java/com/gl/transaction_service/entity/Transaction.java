package com.gl.transaction_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;


    // -------------------------
    // BOOKING / RESOURCE
    // -------------------------

    private Long bookingId;

    private Long resourceId;


    // -------------------------
    // USERS
    // -------------------------

    private Long renterId;

    private Long ownerId;


    // -------------------------
    // RENT DETAILS
    // -------------------------

    private Double rentPerDay;

    private Integer rentalDays;

    private Double totalRent;

    private Double securityDeposit;


    // -------------------------
    // PAYMENT
    // -------------------------



    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


    // -------------------------
    // DATES
    // -------------------------

    private LocalDate bookingDate;

    private LocalDate expectedReturnDate;

    private LocalDateTime createdAt;


    // -------------------------
    // TRANSACTION STATUS
    // -------------------------

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;


    // -------------------------
    // CHECKBOXES
    // -------------------------

    private Boolean rentPaid;

    private Boolean securityDepositPaid;

    private Boolean resourceCollected;

    private Boolean resourceReturned;

    private Boolean securityDepositReturned;


    // -------------------------
    // CREATED TIME
    // -------------------------

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}