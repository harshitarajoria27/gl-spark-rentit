import api from "./axios";

import type {
  BookingRequest,
  BookingResponse,
  BookingUpdateRequest
} from "../types/booking";


/* CREATE BOOKING */

export const createBooking = async (
  data: BookingRequest
): Promise<BookingResponse> => {

  const response =
    await api.post<BookingResponse>(
      "/api/bookings",
      data
    );

  return response.data;
};


/* GET MY BOOKINGS */

export const getMyBookings = async ():
  Promise<BookingResponse[]> => {

  const response =
    await api.get<BookingResponse[]>(
      "/api/bookings/my-bookings"
    );

  return response.data;
};


/* GET REQUESTS RECEIVED */

export const getReceivedRequests = async ():
  Promise<BookingResponse[]> => {

  const response =
    await api.get<BookingResponse[]>(
      "/api/bookings/requests"
    );

  return response.data;
};


/* CANCEL BOOKING */

export const cancelBooking = async (
  bookingId: number
): Promise<BookingResponse> => {

  const response =
    await api.put<BookingResponse>(
      `/api/bookings/cancel/${bookingId}`
    );

  return response.data;
};


/* UPDATE RENTAL DAYS */

export const updateRentalDays = async (
  bookingId: number,
  data: BookingUpdateRequest
): Promise<BookingResponse> => {

  const response =
    await api.put<BookingResponse>(
      `/api/bookings/${bookingId}`,
      data
    );

  return response.data;
};


/* APPROVE BOOKING */

export const approveBooking = async (
  bookingId: number
): Promise<BookingResponse> => {

  const response =
    await api.put<BookingResponse>(
      `/api/bookings/${bookingId}/approve`
    );

  return response.data;
};


/* REJECT BOOKING */

export const rejectBooking = async (
  bookingId: number
): Promise<BookingResponse> => {

  const response =
    await api.put<BookingResponse>(
      `/api/bookings/${bookingId}/reject`
    );

  return response.data;
};

export const requestExtension = async (
  bookingId: number,
  rentalDays: number
): Promise<BookingResponse> => {

  const response =
    await api.put<BookingResponse>(
      `/api/bookings/${bookingId}/extension`,
      {
        rentalDays
      }
    );

  return response.data;
};

export const approveExtension = async (
  bookingId: number
): Promise<BookingResponse> => {

  const response =
    await api.put<BookingResponse>(
      `/api/bookings/${bookingId}/extension/approve`
    );

  return response.data;
};

export const rejectExtension = async (
  bookingId: number
): Promise<BookingResponse> => {

  const response =
    await api.put<BookingResponse>(
      `/api/bookings/${bookingId}/extension/reject`
    );

  return response.data;
};

