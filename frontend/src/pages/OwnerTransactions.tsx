import { useEffect, useState } from "react";

import type { Transaction } from "../types/transaction";

import {
  getOwnerTransactions,
  markSecurityReturned
} from "../api/transactionApi";

import "./Transactions.css";


const OwnerTransactions = () => {

  const [transactions, setTransactions] =
    useState<Transaction[]>([]);

  const [loading, setLoading] =
    useState(true);

  const [error, setError] =
    useState("");


  // =====================================
  // LOAD OWNER TRANSACTIONS
  // =====================================

  const loadTransactions = async () => {

    try {

      setLoading(true);
      setError("");

      const data =
        await getOwnerTransactions();

      setTransactions(data);

    } catch (err) {

      console.error(
        "Error loading owner transactions:",
        err
      );

      setError(
        "Unable to load owner transactions."
      );

    } finally {

      setLoading(false);
    }
  };


  // =====================================
  // LOAD WHEN PAGE OPENS
  // =====================================

  useEffect(() => {

    loadTransactions();

  }, []);


  // =====================================
  // SECURITY AMOUNT RETURNED
  // =====================================

  const handleSecurityReturned = async (
    transactionId: number
  ) => {

    try {

      await markSecurityReturned(
        transactionId
      );

      await loadTransactions();

    } catch (err) {

      console.error(
        "Error updating transaction:",
        err
      );

      alert(
        "Unable to update transaction."
      );
    }
  };


  // =====================================
  // LOADING
  // =====================================

  if (loading) {

    return (

      <div className="transactions-page">

        <h1>
          Owner Transactions
        </h1>

        <p>
          Loading transactions...
        </p>

      </div>

    );
  }


  // =====================================
  // PAGE
  // =====================================

  return (

    <div className="transactions-page">

      <div className="transactions-header">

        <h1>
          Owner Transactions
        </h1>

        <p>
          Manage transactions for
          your resources.
        </p>

      </div>


      {/* ERROR */}

      {error && (

        <div className="transaction-error">

          {error}

        </div>

      )}


      {/* NO TRANSACTIONS */}

      {transactions.length === 0 ? (

        <div className="empty-transactions">

          <h3>
            No Transactions
          </h3>

          <p>
            No one has borrowed your
            resources yet.
          </p>

        </div>

      ) : (

        // =====================================
        // TRANSACTIONS
        // =====================================

        <div className="transactions-grid">

          {transactions.map(
            (transaction) => (

              <div
                className="transaction-card"
                key={transaction.transactionId}
              >


                {/* HEADER */}

                <div className="transaction-card-header">

                  <div>

                    <span className="transaction-label">
                      Transaction
                    </span>

                    <h2>
                      #{transaction.transactionId}
                    </h2>

                  </div>


                  <span
                    className={
                      transaction.status === "COMPLETED"
                        ? "status completed"
                        : "status active"
                    }
                  >

                    {transaction.status}

                  </span>

                </div>


                {/* DETAILS */}

                <div className="transaction-details">

                  <div>

                    <span>
                      Booking ID
                    </span>

                    <strong>
                      #{transaction.bookingId}
                    </strong>

                  </div>


                  <div>

  <span>
    Resource
  </span>

  <strong>
    {transaction.resourceName}
  </strong>

</div>


                  <div>

  <span>
    Borrower
  </span>

  <strong>
    {transaction.borrowerName}
  </strong>

</div>


                  <div>

                    <span>
                      Rent / Day
                    </span>

                    <strong>
                      ₹{transaction.rentPerDay}
                    </strong>

                  </div>


                  <div>

                    <span>
                      Rental Days
                    </span>

                    <strong>
                      {transaction.rentalDays}
                    </strong>

                  </div>


                  <div>

                    <span>
                      Total Rent
                    </span>

                    <strong>
                      ₹{transaction.totalRent}
                    </strong>

                  </div>


                  <div>

                    <span>
                      Security Amount
                    </span>

                    <strong>
                      ₹{transaction.securityDeposit}
                    </strong>

                  </div>

                </div>


                {/* DATES */}

                <div className="transaction-dates">

                  <p>

                    <strong>
                      From:
                    </strong>{" "}

                    {transaction.bookingDate}

                  </p>


                  <p>

                    <strong>
                      Return:
                    </strong>{" "}

                    {transaction.expectedReturnDate}

                  </p>

                </div>


                {/* PROGRESS */}

                <div className="transaction-progress">

                  <h3>
                    Transaction Progress
                  </h3>


                  {/* PAID */}

                  <label
                    className={
                      transaction.rentPaid
                        ? "transaction-check checked"
                        : "transaction-check disabled-check"
                    }
                  >

                    <input
                      type="checkbox"
                      checked={transaction.rentPaid}
                      disabled
                    />


                    <div>

                      <strong>
                        Paid & Resource Collected
                      </strong>

                      <span>
                        Confirmed by renter.
                      </span>

                    </div>

                  </label>


                  {/* PRODUCT RETURNED */}

                  <label
                    className={
                      transaction.resourceReturned
                        ? "transaction-check checked"
                        : "transaction-check disabled-check"
                    }
                  >

                    <input
                      type="checkbox"
                      checked={
                        transaction.resourceReturned
                      }
                      disabled
                    />


                    <div>

                      <strong>
                        Product Returned
                      </strong>

                      <span>
                        Confirmed by renter.
                      </span>

                    </div>

                  </label>


                  {/* SECURITY RETURNED */}

                  <label
                    className={
                      transaction.securityDepositReturned
                        ? "transaction-check checked"
                        : "transaction-check"
                    }
                  >

                    <input
                      type="checkbox"

                      checked={
                        transaction.securityDepositReturned
                      }

                      disabled={
                        !transaction.resourceReturned ||
                        transaction.securityDepositReturned
                      }

                      onChange={() =>
                        handleSecurityReturned(
                          transaction.transactionId
                        )
                      }
                    />


                    <div>

                      <strong>
                        Security Amount Returned
                      </strong>

                      <span>
                        Confirm that you returned
                        the security amount.
                      </span>

                    </div>

                  </label>

                </div>

              </div>

            )
          )}

        </div>

      )}

    </div>
  );
};

export default OwnerTransactions;