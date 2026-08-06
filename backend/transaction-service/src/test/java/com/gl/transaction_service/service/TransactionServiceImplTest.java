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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {


    @Mock
    private TransactionRepository transactionRepository;


    @Mock
    private ResourceClient resourceClient;


    @Mock
    private UserClient userClient;


    @InjectMocks
    private TransactionServiceImpl transactionService;


    private Transaction transaction;

    private ResourceResponse resource;

    private UserResponse owner;

    private UserResponse borrower;


    // =================================================
    // SETUP
    // =================================================

    @BeforeEach
    void setUp() {

        transaction = Transaction.builder()

                .transactionId(1L)

                .bookingId(100L)

                .resourceId(200L)

                .renterId(10L)

                .ownerId(20L)

                .rentPerDay(500.0)

                .rentalDays(5)

                .totalRent(2500.0)

                .securityDeposit(2000.0)

                .bookingDate(
                        LocalDate.of(
                                2026,
                                8,
                                1
                        )
                )

                .expectedReturnDate(
                        LocalDate.of(
                                2026,
                                8,
                                6
                        )
                )

                .paymentStatus(
                        PaymentStatus.PENDING
                )

                .status(
                        TransactionStatus.ACTIVE
                )

                .rentPaid(false)

                .securityDepositPaid(false)

                .resourceCollected(false)

                .resourceReturned(false)

                .securityDepositReturned(false)

                .build();


        resource =
                new ResourceResponse();

        resource.setResourceId(200L);

        resource.setTitle("Camera");


        owner =
                new UserResponse();

        owner.setFullName(
                "Resource Owner"
        );


        borrower =
                new UserResponse();

        borrower.setFullName(
                "Borrower User"
        );
    }


    // =================================================
    // TEST 1
    // CREATE TRANSACTION
    // =================================================

    @Test
    void createTransactionSuccessfully() {

        TransactionRequest request =
                new TransactionRequest();

        request.setBookingId(100L);

        request.setResourceId(200L);

        request.setRenterId(10L);

        request.setOwnerId(20L);

        request.setRentPerDay(500.0);

        request.setSecurityDeposit(2000.0);

        request.setBookingDate(
                LocalDate.of(
                        2026,
                        8,
                        1
                )
        );

        request.setExpectedReturnDate(
                LocalDate.of(
                        2026,
                        8,
                        6
                )
        );


        when(
                transactionRepository.save(
                        any(Transaction.class)
                )
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );


        Transaction result =
                transactionService
                        .createTransaction(
                                request
                        );


        assertNotNull(result);


        assertEquals(
                100L,
                result.getBookingId()
        );


        assertEquals(
                200L,
                result.getResourceId()
        );


        assertEquals(
                10L,
                result.getRenterId()
        );


        assertEquals(
                20L,
                result.getOwnerId()
        );


        assertEquals(
                5,
                result.getRentalDays()
        );


        assertEquals(
                2500.0,
                result.getTotalRent()
        );


        assertEquals(
                PaymentStatus.PENDING,
                result.getPaymentStatus()
        );


        assertEquals(
                TransactionStatus.ACTIVE,
                result.getStatus()
        );


        assertFalse(
                result.getRentPaid()
        );

        assertFalse(
                result.getSecurityDepositPaid()
        );

        assertFalse(
                result.getResourceCollected()
        );

        assertFalse(
                result.getResourceReturned()
        );

        assertFalse(
                result.getSecurityDepositReturned()
        );


        verify(
                transactionRepository,
                times(1)
        ).save(
                any(Transaction.class)
        );
    }


    // =================================================
    // TEST 2
    // RENTAL DAYS MINIMUM 1
    // =================================================

    @Test
    void createTransactionShouldUseOneDayWhenDatesAreSame() {

        TransactionRequest request =
                new TransactionRequest();

        request.setBookingId(100L);

        request.setResourceId(200L);

        request.setRenterId(10L);

        request.setOwnerId(20L);

        request.setRentPerDay(500.0);

        request.setSecurityDeposit(2000.0);

        request.setBookingDate(
                LocalDate.of(
                        2026,
                        8,
                        1
                )
        );

        request.setExpectedReturnDate(
                LocalDate.of(
                        2026,
                        8,
                        1
                )
        );


        when(
                transactionRepository.save(
                        any(Transaction.class)
                )
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );


        Transaction result =
                transactionService
                        .createTransaction(
                                request
                        );


        assertEquals(
                1,
                result.getRentalDays()
        );


        assertEquals(
                500.0,
                result.getTotalRent()
        );
    }


    // =================================================
    // TEST 3
    // MARK PAID
    // =================================================

    @Test
    void markPaidSuccessfully() {

        when(
                transactionRepository
                        .findById(1L)
        ).thenReturn(
                Optional.of(transaction)
        );


        when(
                transactionRepository.save(
                        transaction
                )
        ).thenReturn(transaction);


        Transaction result =
                transactionService
                        .markPaid(1L);


        assertTrue(
                result.getRentPaid()
        );


        assertTrue(
                result.getSecurityDepositPaid()
        );


        assertTrue(
                result.getResourceCollected()
        );


        assertEquals(
                PaymentStatus.PAID,
                result.getPaymentStatus()
        );


        verify(
                transactionRepository
        ).save(transaction);
    }


    // =================================================
    // TEST 4
    // MARK PAID - TRANSACTION NOT FOUND
    // =================================================

    @Test
    void markPaidShouldFailWhenTransactionNotFound() {

        when(
                transactionRepository
                        .findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () ->
                                transactionService
                                        .markPaid(99L)
                );


        assertEquals(
                "Transaction not found with id : 99",
                exception.getMessage()
        );


        verify(
                transactionRepository,
                never()
        ).save(
                any(Transaction.class)
        );
    }


    // =================================================
    // TEST 5
    // MARK PRODUCT RETURNED
    // =================================================

    @Test
    void markReturnedSuccessfully() {

        when(
                transactionRepository
                        .findById(1L)
        ).thenReturn(
                Optional.of(transaction)
        );


        when(
                transactionRepository.save(
                        transaction
                )
        ).thenReturn(transaction);


        Transaction result =
                transactionService
                        .markReturned(1L);


        assertTrue(
                result.getResourceReturned()
        );


        verify(
                transactionRepository
        ).save(transaction);
    }


    // =================================================
    // TEST 6
    // MARK RETURNED - NOT FOUND
    // =================================================

    @Test
    void markReturnedShouldFailWhenTransactionNotFound() {

        when(
                transactionRepository
                        .findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () ->
                                transactionService
                                        .markReturned(99L)
                );


        assertEquals(
                "Transaction not found with id : 99",
                exception.getMessage()
        );
    }


    // =================================================
    // TEST 7
    // SECURITY AMOUNT RETURNED
    // =================================================

    @Test
    void markSecurityReturnedSuccessfully() {

        transaction.setResourceReturned(
                true
        );


        when(
                transactionRepository
                        .findById(1L)
        ).thenReturn(
                Optional.of(transaction)
        );


        when(
                transactionRepository.save(
                        transaction
                )
        ).thenReturn(transaction);


        Transaction result =
                transactionService
                        .markSecurityReturned(
                                1L
                        );


        assertTrue(
                result.getSecurityDepositReturned()
        );


        assertEquals(
                PaymentStatus.REFUNDED,
                result.getPaymentStatus()
        );


        assertEquals(
                TransactionStatus.COMPLETED,
                result.getStatus()
        );


        verify(
                transactionRepository
        ).save(transaction);
    }


    // =================================================
    // TEST 8
    // SECURITY RETURN - NOT FOUND
    // =================================================

    @Test
    void markSecurityReturnedShouldFailWhenTransactionNotFound() {

        when(
                transactionRepository
                        .findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () ->
                                transactionService
                                        .markSecurityReturned(
                                                99L
                                        )
                );


        assertEquals(
                "Transaction not found with id : 99",
                exception.getMessage()
        );
    }


    // =================================================
    // TEST 9
    // GET TRANSACTION BY ID
    // =================================================

    @Test
    void getTransactionByIdSuccessfully() {

        when(
                transactionRepository
                        .findById(1L)
        ).thenReturn(
                Optional.of(transaction)
        );


        mockDisplayInformation();


        TransactionResponse response =
                transactionService
                        .getTransactionById(
                                1L
                        );


        assertNotNull(response);


        assertEquals(
                1L,
                response.getTransactionId()
        );


        assertEquals(
                100L,
                response.getBookingId()
        );


        assertEquals(
                "Camera",
                response.getResourceName()
        );


        assertEquals(
                "Resource Owner",
                response.getOwnerName()
        );


        assertEquals(
                "Borrower User",
                response.getBorrowerName()
        );
    }


    // =================================================
    // TEST 10
    // GET TRANSACTION BY ID - NOT FOUND
    // =================================================

    @Test
    void getTransactionByIdShouldFailWhenNotFound() {

        when(
                transactionRepository
                        .findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () ->
                                transactionService
                                        .getTransactionById(
                                                99L
                                        )
                );


        assertEquals(
                "Transaction not found with id : 99",
                exception.getMessage()
        );


        verifyNoInteractions(
                resourceClient
        );


        verifyNoInteractions(
                userClient
        );
    }


    // =================================================
    // TEST 11
    // GET BY BOOKING ID
    // =================================================

    @Test
    void getTransactionByBookingIdSuccessfully() {

        when(
                transactionRepository
                        .findByBookingId(
                                100L
                        )
        ).thenReturn(
                Optional.of(transaction)
        );


        mockDisplayInformation();


        TransactionResponse response =
                transactionService
                        .getTransactionByBookingId(
                                100L
                        );


        assertNotNull(response);


        assertEquals(
                100L,
                response.getBookingId()
        );


        assertEquals(
                "Camera",
                response.getResourceName()
        );


        assertEquals(
                "Resource Owner",
                response.getOwnerName()
        );


        assertEquals(
                "Borrower User",
                response.getBorrowerName()
        );
    }


    // =================================================
    // TEST 12
    // BOOKING ID NOT FOUND
    // =================================================

    @Test
    void getTransactionByBookingIdShouldFailWhenNotFound() {

        when(
                transactionRepository
                        .findByBookingId(
                                999L
                        )
        ).thenReturn(
                Optional.empty()
        );


        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () ->
                                transactionService
                                        .getTransactionByBookingId(
                                                999L
                                        )
                );


        assertEquals(
                "Transaction not found with booking id : 999",
                exception.getMessage()
        );
    }


    // =================================================
    // TEST 13
    // GET MY TRANSACTIONS
    // =================================================

    @Test
    void getTransactionsByUserSuccessfully() {

        when(
                transactionRepository
                        .findByRenterId(
                                10L
                        )
        ).thenReturn(
                List.of(transaction)
        );


        mockDisplayInformation();


        List<TransactionResponse> responses =
                transactionService
                        .getTransactionsByUser(
                                10L
                        );


        assertNotNull(responses);


        assertEquals(
                1,
                responses.size()
        );


        assertEquals(
                "Camera",
                responses.get(0)
                        .getResourceName()
        );


        assertEquals(
                "Resource Owner",
                responses.get(0)
                        .getOwnerName()
        );
    }


    // =================================================
    // TEST 14
    // GET OWNER TRANSACTIONS
    // =================================================

    @Test
    void getTransactionsByOwnerSuccessfully() {

        when(
                transactionRepository
                        .findByOwnerId(
                                20L
                        )
        ).thenReturn(
                List.of(transaction)
        );


        mockDisplayInformation();


        List<TransactionResponse> responses =
                transactionService
                        .getTransactionsByOwner(
                                20L
                        );


        assertNotNull(responses);


        assertEquals(
                1,
                responses.size()
        );


        assertEquals(
                "Camera",
                responses.get(0)
                        .getResourceName()
        );


        assertEquals(
                "Borrower User",
                responses.get(0)
                        .getBorrowerName()
        );
    }


    // =================================================
    // TEST 15
    // GET ALL TRANSACTIONS
    // =================================================

    @Test
    void getAllTransactionsSuccessfully() {

        when(
                transactionRepository
                        .findAll()
        ).thenReturn(
                List.of(transaction)
        );


        mockDisplayInformation();


        List<TransactionResponse> responses =
                transactionService
                        .getAllTransactions();


        assertNotNull(responses);


        assertEquals(
                1,
                responses.size()
        );


        TransactionResponse response =
                responses.get(0);


        assertEquals(
                1L,
                response.getTransactionId()
        );


        assertEquals(
                "Camera",
                response.getResourceName()
        );


        assertEquals(
                "Resource Owner",
                response.getOwnerName()
        );


        assertEquals(
                "Borrower User",
                response.getBorrowerName()
        );
    }


    // =================================================
    // TEST 16
    // EMPTY USER TRANSACTIONS
    // =================================================

    @Test
    void getTransactionsByUserShouldReturnEmptyList() {

        when(
                transactionRepository
                        .findByRenterId(
                                10L
                        )
        ).thenReturn(
                List.of()
        );


        List<TransactionResponse> responses =
                transactionService
                        .getTransactionsByUser(
                                10L
                        );


        assertNotNull(responses);

        assertTrue(
                responses.isEmpty()
        );


        verifyNoInteractions(
                resourceClient
        );


        verifyNoInteractions(
                userClient
        );
    }


    // =================================================
    // TEST 17
    // VERIFY RESPONSE MAPPING
    // =================================================

    @Test
    void transactionResponseShouldContainCorrectData() {

        when(
                transactionRepository
                        .findById(1L)
        ).thenReturn(
                Optional.of(transaction)
        );


        mockDisplayInformation();


        TransactionResponse response =
                transactionService
                        .getTransactionById(
                                1L
                        );


        assertEquals(
                transaction.getTransactionId(),
                response.getTransactionId()
        );


        assertEquals(
                transaction.getBookingId(),
                response.getBookingId()
        );


        assertEquals(
                transaction.getResourceId(),
                response.getResourceId()
        );


        assertEquals(
                transaction.getRenterId(),
                response.getRenterId()
        );


        assertEquals(
                transaction.getOwnerId(),
                response.getOwnerId()
        );


        assertEquals(
                transaction.getRentPerDay(),
                response.getRentPerDay()
        );


        assertEquals(
                transaction.getRentalDays(),
                response.getRentalDays()
        );


        assertEquals(
                transaction.getTotalRent(),
                response.getTotalRent()
        );


        assertEquals(
                transaction.getSecurityDeposit(),
                response.getSecurityDeposit()
        );


        assertEquals(
                transaction.getPaymentStatus(),
                response.getPaymentStatus()
        );


        assertEquals(
                transaction.getBookingDate(),
                response.getBookingDate()
        );


        assertEquals(
                transaction.getExpectedReturnDate(),
                response.getExpectedReturnDate()
        );


        assertEquals(
                transaction.getStatus(),
                response.getStatus()
        );


        assertEquals(
                transaction.getRentPaid(),
                response.getRentPaid()
        );


        assertEquals(
                transaction.getSecurityDepositPaid(),
                response.getSecurityDepositPaid()
        );


        assertEquals(
                transaction.getResourceCollected(),
                response.getResourceCollected()
        );


        assertEquals(
                transaction.getResourceReturned(),
                response.getResourceReturned()
        );


        assertEquals(
                transaction.getSecurityDepositReturned(),
                response.getSecurityDepositReturned()
        );


        assertEquals(
                "Camera",
                response.getResourceName()
        );


        assertEquals(
                "Resource Owner",
                response.getOwnerName()
        );


        assertEquals(
                "Borrower User",
                response.getBorrowerName()
        );
    }


    // =================================================
    // HELPER METHOD
    // =================================================

    private void mockDisplayInformation() {

        when(
                resourceClient.getResourceById(
                        200L
                )
        ).thenReturn(resource);


        when(
                userClient.getUserById(
                        20L
                )
        ).thenReturn(owner);


        when(
                userClient.getUserById(
                        10L
                )
        ).thenReturn(borrower);
    }
}