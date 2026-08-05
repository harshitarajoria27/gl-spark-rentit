package com.gl.transaction_service.dto;

import com.gl.transaction_service.entity.PaymentMethod;
import com.gl.transaction_service.entity.PaymentStatus;
import com.gl.transaction_service.entity.TransactionStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private Long transactionId;

    private Long bookingId;

    private Long resourceId;

    private Long renterId;

    private Long ownerId;

    private Double rentPerDay;

    private Integer rentalDays;

    private Double totalRent;

    private Double securityDeposit;

    private Double damageCharges;

    private Double refundAmount;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private LocalDate bookingDate;

    private LocalDate expectedReturnDate;

    private LocalDate actualReturnDate;

    private TransactionStatus status;

}