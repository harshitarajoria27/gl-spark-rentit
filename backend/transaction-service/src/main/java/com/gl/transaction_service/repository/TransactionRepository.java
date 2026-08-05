package com.gl.transaction_service.repository;



import com.gl.transaction_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByBookingId(Long bookingId);

    List<Transaction> findByRenterId(Long renterId);

    List<Transaction> findByOwnerId(Long ownerId);

}
