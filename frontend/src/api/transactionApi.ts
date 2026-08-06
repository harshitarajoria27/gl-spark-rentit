import api from "./axios";

import type { Transaction } from "../types/transaction";


// ================================
// MY TRANSACTIONS
// ================================

export const getMyTransactions =
  async (): Promise<Transaction[]> => {

    const response = await api.get(
      "/transactions/my"
    );

    return response.data;
  };


// ================================
// OWNER TRANSACTIONS
// ================================

export const getOwnerTransactions =
  async (): Promise<Transaction[]> => {

    const response = await api.get(
      "/transactions/owned"
    );

    return response.data;
  };


// ================================
// RENTER MARKS PAID
// ================================

export const markTransactionPaid = async (
  transactionId: number
): Promise<Transaction> => {

  const response = await api.put(
    `/transactions/${transactionId}/paid`
  );

  return response.data;
};


// ================================
// RENTER MARKS PRODUCT RETURNED
// ================================

export const markProductReturned = async (
  transactionId: number
): Promise<Transaction> => {

  const response = await api.put(
    `/transactions/${transactionId}/returned`
  );

  return response.data;
};


// ================================
// OWNER RETURNS SECURITY AMOUNT
// ================================

export const markSecurityReturned = async (
  transactionId: number
): Promise<Transaction> => {

  const response = await api.put(
    `/transactions/${transactionId}/security-returned`
  );

  return response.data;
};