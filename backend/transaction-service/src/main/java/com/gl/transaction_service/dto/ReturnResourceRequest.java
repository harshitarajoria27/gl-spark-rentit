package com.gl.transaction_service.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnResourceRequest {

    private Long bookingId;

    private LocalDate actualReturnDate;

    private Double damageCharges;

}