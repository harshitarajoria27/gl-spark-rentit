package com.gl.transaction_service.client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "BOOKING-SERVICE")
public interface BookingClient {

    @GetMapping("/bookings/{bookingId}")
    Object getBooking(@PathVariable Long bookingId);

}
