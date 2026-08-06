import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import {
  approveBooking,
  getReceivedRequests,
  rejectBooking,
  approveExtension,
  rejectExtension
} from "../api/BookingApi";

import type {
  BookingResponse,
  BookingStatus
} from "../types/booking";

import "./Bookings.css";


function BorrowRequests() {

  const [requests, setRequests] =
    useState<BookingResponse[]>([]);

  const [loading, setLoading] =
    useState(true);

  const [error, setError] =
    useState("");

  const [message, setMessage] =
    useState("");

  const [actionBookingId, setActionBookingId] =
    useState<number | null>(null);


  /* LOAD REQUESTS */

  const loadRequests = async () => {

    try {

      setLoading(true);
      setError("");

      const data =
        await getReceivedRequests();

      console.log(
        "RECEIVED REQUESTS:",
        data
      );

      setRequests(data);

    } catch (error) {

      console.error(
        "Failed to load requests:",
        error
      );

      setError(
        "Unable to load borrow requests."
      );

    } finally {

      setLoading(false);
    }
  };


  useEffect(() => {

    loadRequests();

  }, []);


  /* UPDATE LOCAL STATUS */

  const updateStatus = (
    bookingId: number,
    status: BookingStatus
  ) => {

    setRequests(previous =>
      previous.map(request =>
        request.bookingId === bookingId
          ? {
              ...request,
              status
            }
          : request
      )
    );
  };


  /* APPROVE */

  const handleApprove = async (
    bookingId: number
  ) => {

    try {

      setActionBookingId(bookingId);

      setError("");
      setMessage("");

      await approveBooking(
        bookingId
      );

      updateStatus(
        bookingId,
        "APPROVED"
      );

      setMessage(
        "Borrow request approved."
      );

    } catch (error) {

      console.error(
        "Approve error:",
        error
      );

      setError(
        "Unable to approve request."
      );

    } finally {

      setActionBookingId(null);
    }
  };


  /* REJECT */

  const handleReject = async (
    bookingId: number
  ) => {

    const confirmed =
      window.confirm(
        "Are you sure you want to reject this request?"
      );

    if (!confirmed) {
      return;
    }


    try {

      setActionBookingId(bookingId);

      setError("");
      setMessage("");

      await rejectBooking(
        bookingId
      );

      updateStatus(
        bookingId,
        "REJECTED"
      );

      setMessage(
        "Borrow request rejected."
      );

    } catch (error) {

      console.error(
        "Reject error:",
        error
      );

      setError(
        "Unable to reject request."
      );

    } finally {

      setActionBookingId(null);
    }
  };


  const formatStatus = (
    status: BookingStatus
  ) => {

    return status.charAt(0) +
      status.slice(1).toLowerCase();
  };

  const handleApproveExtension = async (
  bookingId: number
) => {

  try {

    setActionBookingId(bookingId);

    await approveExtension(bookingId);

    setMessage(
      "Extension approved successfully."
    );

    loadRequests();

  } catch (error) {

    console.error(error);

    setError(
      "Unable to approve extension."
    );

  } finally {

    setActionBookingId(null);

  }

};
const handleRejectExtension = async (
  bookingId: number
) => {

  try {

    setActionBookingId(bookingId);

    await rejectExtension(bookingId);

    setMessage(
      "Extension rejected."
    );

    loadRequests();

  } catch (error) {

    console.error(error);

    setError(
      "Unable to reject extension."
    );

  } finally {

    setActionBookingId(null);

  }

};


  return (

    <main className="bookings-page">

      <div className="bookings-container">


        {/* HEADER */}

        <div className="bookings-header">

          <div>

            <span className="bookings-label">
              RESOURCE OWNER
            </span>

            <h1>
              Borrow Requests
            </h1>

            <p>
              Review requests received
              for your resources.
            </p>

          </div>


          <Link
            to="/dashboard"
            className="bookings-back-btn"
          >
            ← Dashboard
          </Link>

        </div>


        {message && (

          <div className="bookings-success">
            {message}
          </div>

        )}


        {error && (

          <div className="bookings-error">
            {error}
          </div>

        )}


        {loading && (

          <div className="bookings-state">
            Loading borrow requests...
          </div>

        )}


        {!loading &&
          requests.length === 0 && (

            <div className="bookings-empty">

              <div className="bookings-empty-icon">
                📥
              </div>

              <h2>
                No borrow requests
              </h2>

              <p>
                Requests for your resources
                will appear here.
              </p>

            </div>

          )}


        {!loading &&
          requests.length > 0 && (

            <div className="bookings-grid">

              {requests.map(request => (

                <article
                  key={request.bookingId}
                  className="booking-card"
                >

                  <div className="booking-card-top">

                    <div>

                      <span className="booking-number">
                        REQUEST #{request.bookingId}
                      </span>

                      <h2>
                        Resource #{request.resourceId}
                      </h2>

                    </div>


                    <span
                      className={
                        `booking-status status-${request.status.toLowerCase()}`
                      }
                    >
                      {formatStatus(
                        request.status
                      )}
                    </span>

                  </div>


                  <div className="booking-info">

                    <div>

                      <span>
                        Rental Duration
                      </span>

                      <strong>
                        {request.rentalDays}{" "}
                        {request.rentalDays === 1
                          ? "Day"
                          : "Days"
                        }
                      </strong>
                      {request.extensionStatus && (

  <div style={{ marginTop: "12px" }}>

    <strong>
      Extension Status :
    </strong>{" "}

    {request.extensionStatus}

  </div>

)}
{request.requestedRentalDays && (

  <div>

    <strong>

      Requested Days :

    </strong>{" "}

    {request.requestedRentalDays}

  </div>

)}

                    </div>


                    <div className="booking-requester">

  <span>
    Requested By
  </span>

  <div className="booking-contact-details">

    <p>
      <strong>Name:</strong>{" "}
      {request.renterName}
    </p>

    <p>
      <strong>Phone:</strong>{" "}
      {request.renterPhone}
    </p>

    <p>
      <strong>Email:</strong>{" "}
      {request.renterEmail}
    </p>

  </div>

</div>

                  </div>


                  <div className="booking-card-actions">

  {/* New Booking Request */}

  {request.status === "PENDING" && (

    <>
      <button
        type="button"
        className="reject-request-btn"
        disabled={
          actionBookingId === request.bookingId
        }
        onClick={() =>
          handleReject(request.bookingId)
        }
      >
        Reject
      </button>

      <button
        type="button"
        className="approve-request-btn"
        disabled={
          actionBookingId === request.bookingId
        }
        onClick={() =>
          handleApprove(request.bookingId)
        }
      >
        {actionBookingId === request.bookingId
          ? "Processing..."
          : "Approve"}
      </button>
    </>

  )}

  {/* Extension Request */}

  {request.status === "APPROVED" &&
    request.extensionStatus === "PENDING" && (

      <>
        <button
          type="button"
          className="reject-request-btn"
          disabled={
            actionBookingId === request.bookingId
          }
          onClick={() =>
            handleRejectExtension(
              request.bookingId
            )
          }
        >
          Reject Extension
        </button>

        <button
          type="button"
          className="approve-request-btn"
          disabled={
            actionBookingId === request.bookingId
          }
          onClick={() =>
            handleApproveExtension(
              request.bookingId
            )
          }
        >
          {actionBookingId === request.bookingId
            ? "Processing..."
            : "Approve Extension"}
        </button>
      </>

  )}

  {/* Final Status */}

  {request.status !== "PENDING" &&
    request.extensionStatus !== "PENDING" && (

      <span className="booking-no-action">

        {request.status === "APPROVED"
          ? "Request Approved"
          : request.status === "REJECTED"
          ? "Request Rejected"
          : "Booking Cancelled"}

      </span>

  )}

</div>

                </article>

              ))}

            </div>

          )}

      </div>

    </main>

  );
}


export default BorrowRequests;