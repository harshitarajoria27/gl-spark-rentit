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

    // Booking Details
    private Long bookingId;

    private Long resourceId;

    // Users
    private Long renterId;

    private Long ownerId;

    // Rent Details
    private Double rentPerDay;

    private Integer rentalDays;

    private Double totalRent;

    // Deposit Details
    private Double securityDeposit;

    private Double damageCharges;

    private Double refundAmount;

    // Payment
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    // Dates
    private LocalDate bookingDate;

    private LocalDate expectedReturnDate;

    private LocalDate actualReturnDate;

    // Transaction Status
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}