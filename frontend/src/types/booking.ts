export type BookingStatus =
  | "PENDING"
  | "APPROVED"
  | "REJECTED"
  | "CANCELLED";


export type ExtensionStatus =
  | "PENDING"
  | "APPROVED"
  | "REJECTED";


export interface BookingRequest {

  resourceId: number;

  rentalDays: number;
}


export interface BookingResponse {

  bookingId: number;

  userId: number;

  ownerId: number;

  resourceId: number;

  rentalDays: number;

  status: BookingStatus;

  requestedRentalDays?: number;

  extensionStatus?: ExtensionStatus;


  // =========================
  // RENTER DETAILS
  // Visible to owner
  // =========================

  renterName?: string;

  renterEmail?: string;

  renterPhone?: string;


  // =========================
  // OWNER DETAILS
  // Visible to renter after approval
  // =========================

  ownerName?: string;

  ownerEmail?: string;

  ownerPhone?: string;
}


export interface BookingUpdateRequest {

  rentalDays: number;
}