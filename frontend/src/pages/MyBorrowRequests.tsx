import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import type {
  BookingResponse
} from "../types/booking";
import {
  getMyBookings,
  cancelBooking,
  updateRentalDays,
  requestExtension
} from "../api/BookingApi";

import "./Bookings.css";

function MyBorrowRequests() {

  const [bookings, setBookings] =
  useState<BookingResponse[]>([]);

  const [loading, setLoading] = useState(true);

  const [error, setError] = useState("");

  const [editingBookingId, setEditingBookingId] =
    useState<number | null>(null);

  const [editedDays, setEditedDays] =
    useState(1);
    
  const [extensionBooking, setExtensionBooking] =
  useState<BookingResponse | null>(null);

const [extensionDays, setExtensionDays] =
  useState(1);

const [sendingExtension, setSendingExtension] =
  useState(false);


  useEffect(() => {

    loadBookings();

  }, []);


  const loadBookings = async () => {

    try {

      setLoading(true);

      const data =
        await getMyBookings();

      console.log(data);

      setBookings(data);

    } catch {

      setError(
        "Unable to load bookings."
      );

    } finally {

      setLoading(false);
    }

  };


  const handleCancel = async (
    bookingId: number
  ) => {

    if (
      !window.confirm(
        "Cancel this request?"
      )
    ) {
      return;
    }

    await cancelBooking(
      bookingId
    );

    loadBookings();

  };


  const handleEdit = (
    bookingId: number,
    currentDays: number
  ) => {

    setEditingBookingId(
      bookingId
    );

    setEditedDays(
      currentDays
    );

  };


  const saveDays = async (
    bookingId: number
  ) => {

    await updateRentalDays(
      bookingId,
      {
        rentalDays: editedDays
      }
    );

    setEditingBookingId(
      null
    );

    loadBookings();

  };
  const openExtensionModal = (
  booking: BookingResponse
) => {

  setExtensionBooking(booking);

  setExtensionDays(
    booking.rentalDays
  );

};
  
const closeExtensionModal = () => {

  if (sendingExtension) {
    return;
  }

  setExtensionBooking(null);

};
const handleExtensionRequest =
async () => {

  if (!extensionBooking) {
    return;
  }

  try {

    setSendingExtension(true);

    await requestExtension(

      extensionBooking.bookingId,

      extensionDays

    );

    loadBookings();

    setExtensionBooking(null);

  }

  catch(error){

    console.error(error);

  }

  finally{

    setSendingExtension(false);

  }

};


  return (

    <main className="bookings-page">

      <div className="bookings-container">

        <div className="bookings-header">

          <div>

            <span className="bookings-label">
              BOOKINGS
            </span>

            <h1>
              My Borrow Requests
            </h1>

            <p>
              View all the resources you
              have requested.
            </p>

          </div>

          <Link
            to="/dashboard"
            className="bookings-back-btn"
          >
            ← Dashboard
          </Link>

        </div>


        {loading &&

          <div className="bookings-state">

            Loading...

          </div>

        }


        {error &&

          <div className="bookings-error">

            {error}

          </div>

        }


        {!loading &&
          bookings.length === 0 && (

          <div className="bookings-empty">

            No borrow requests found.

          </div>

        )}


        <div className="bookings-grid">

          {bookings.map(booking => (

            <div
              className="booking-card"
              key={booking.bookingId}
            >

              <h2>

                Resource #{booking.resourceId}

              </h2>


              <p>

                <strong>Status :</strong>

                {" "}

                {booking.status}

              </p>
              {/* OWNER DETAILS - ONLY AFTER APPROVAL */}

{booking.status === "APPROVED" &&
  booking.ownerName && (

    <div className="booking-contact-details">

      <h3>Owner Contact Details</h3>

      <p>
        <strong>Name:</strong>{" "}
        {booking.ownerName}
      </p>

      <p>
        <strong>Phone:</strong>{" "}
        {booking.ownerPhone}
      </p>

      <p>
        <strong>Email:</strong>{" "}
        {booking.ownerEmail}
      </p>

    </div>
)}


              <p>

                <strong>

                  Rental Days :

                </strong>

                {" "}

                {editingBookingId ===
                booking.bookingId ? (

                  <input
                    type="number"
                    min={1}
                    value={editedDays}
                    onChange={(event) =>
                      setEditedDays(
                        Number(
                          event.target.value
                        )
                      )
                    }
                  />

                ) : (

                  booking.rentalDays

                )}
                

              </p>
                 {booking.extensionStatus && (

<p>

<strong>

Extension Status :

</strong>

{" "}

{booking.extensionStatus}

</p>

)}


              <div
                className="booking-card-actions"
              >

                {booking.status ===
                "PENDING" && (

                  <>

                    {editingBookingId ===
                    booking.bookingId ? (

                      <button
                        className="approve-request-btn"
                        onClick={() =>
                          saveDays(
                            booking.bookingId
                          )
                        }
                      >

                        Save

                      </button>

                    ) : (

                      <button
                        className="approve-request-btn"
                        onClick={() =>
                          handleEdit(
                            booking.bookingId,
                            booking.rentalDays
                          )
                        }
                      >

                        Edit Days

                      </button>

                    )}


                    <button
                      className="reject-request-btn"
                      onClick={() =>
                        handleCancel(
                          booking.bookingId
                        )
                      }
                    >

                      Cancel Request

                    </button>

                  </>

                )}


                {booking.status === "APPROVED" && (

<>

<span>

Approved by Owner

</span>

{booking.extensionStatus !==
"PENDING" && (

<button

className="extension-btn"

onClick={() =>
openExtensionModal(
booking
)
}

>

Request Extension

</button>

)}

</>

)}


                {booking.status ===
                  "REJECTED" && (

                  <span>

                    Request Rejected

                  </span>

                )}


                {booking.status ===
                  "CANCELLED" && (

                  <span>

                    Request Cancelled

                  </span>

                )}

              </div>

            </div>

          ))}

        </div>

      </div>
      {extensionBooking && (

<div className="extension-modal-overlay">

  <div className="extension-modal">

    <h2>Request Extension</h2>

    <p>
      Current Rental Days :
      <strong>{extensionBooking.rentalDays}</strong>
    </p>

    <label>New Rental Days</label>

    <input
      type="number"
      min={extensionBooking.rentalDays + 1}
      value={extensionDays}
      onChange={(e) =>
        setExtensionDays(Number(e.target.value))
      }
    />

    <div className="extension-actions">

      <button onClick={closeExtensionModal}>
        Cancel
      </button>

      <button onClick={handleExtensionRequest}>
        Send Request
      </button>

    </div>

  </div>

</div>

)}

    </main>

  );

}

export default MyBorrowRequests;