export type PaymentStatus =
  | "PENDING"
  | "PAID"
  | "REFUNDED";

export type TransactionStatus =
  | "ACTIVE"
  | "COMPLETED";

export interface Transaction {

  transactionId: number;

  bookingId: number;

  resourceId: number;

  renterId: number;

  ownerId: number;

  rentPerDay: number;

  rentalDays: number;

  totalRent: number;

  securityDeposit: number;

  bookingDate: string;

  expectedReturnDate: string;

  paymentStatus: PaymentStatus;

  status: TransactionStatus;

  rentPaid: boolean;

  securityDepositPaid: boolean;

  resourceCollected: boolean;

  resourceReturned: boolean;

  securityDepositReturned: boolean;

  createdAt: string;

  resourceName: string;
  ownerName: string;
  borrowerName: string;
}