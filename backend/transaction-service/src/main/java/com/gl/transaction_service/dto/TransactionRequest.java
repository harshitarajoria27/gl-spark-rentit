package com.gl.transaction_service.dto;

import com.gl.transaction_service.entity.PaymentMethod;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {

    private Long bookingId;

    private Long resourceId;

    private Long renterId;

    private Long ownerId;

    private Double securityDeposit;

    private Double rentPerDay;

    private LocalDate bookingDate;

    private LocalDate expectedReturnDate;

    private PaymentMethod paymentMethod;

}