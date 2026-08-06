import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import { createBooking } from "../api/BookingApi";
import { getAllResources } from "../api/resourceApi";

import type { ResourceResponse } from "../types/resource";

import "./BrowseResources.css";


function BrowseResources() {

  /* =========================================
     RESOURCE STATE
  ========================================= */

  const [resources, setResources] =
    useState<ResourceResponse[]>([]);

  const [loading, setLoading] =
    useState(true);

  const [error, setError] =
    useState("");


  /* =========================================
     BOOKING STATE
  ========================================= */

  const [selectedResource, setSelectedResource] =
    useState<ResourceResponse | null>(null);

  const [rentalDays, setRentalDays] =
    useState(1);

  const [sendingRequest, setSendingRequest] =
    useState(false);

  const [bookingError, setBookingError] =
    useState("");

  const [bookingSuccess, setBookingSuccess] =
    useState("");


  /* =========================================
     LOAD RESOURCES
  ========================================= */

  useEffect(() => {

    const loadResources = async () => {

      try {

        setLoading(true);
        setError("");

        const data =
          await getAllResources();

        console.log(
          "ALL RESOURCES:",
          data
        );


        /*
         * Backend is already returning
         * available resources except
         * the logged-in user's resources.
         */

        setResources(data);


      } catch (error) {

        console.error(
          "Failed to load resources:",
          error
        );

        setError(
          "Unable to load resources."
        );

      } finally {

        setLoading(false);
      }
    };


    loadResources();

  }, []);


  /* =========================================
     FORMAT CONDITION
  ========================================= */

  const formatCondition = (
    condition: string | null | undefined
  ) => {

    if (!condition) {
      return "Not specified";
    }

    return condition
      .replace(/_/g, " ")
      .toLowerCase()
      .replace(
        /\b\w/g,
        character =>
          character.toUpperCase()
      );
  };


  /* =========================================
     OPEN BORROW MODAL
  ========================================= */

  const openBorrowModal = (
    resource: ResourceResponse
  ) => {

    setSelectedResource(resource);

    setRentalDays(1);

    setBookingError("");

    setBookingSuccess("");
  };


  /* =========================================
     CLOSE BORROW MODAL
  ========================================= */

  const closeBorrowModal = () => {

    if (sendingRequest) {
      return;
    }

    setSelectedResource(null);

    setRentalDays(1);

    setBookingError("");
  };


  /* =========================================
     SEND BORROW REQUEST
  ========================================= */

  const handleBorrowRequest = async () => {

    if (!selectedResource) {
      return;
    }


    if (
      !Number.isInteger(rentalDays) ||
      rentalDays < 1
    ) {

      setBookingError(
        "Rental days must be at least 1."
      );

      return;
    }


    try {

      setSendingRequest(true);

      setBookingError("");


      const booking =
        await createBooking({

          resourceId:
            selectedResource.resourceId,

          rentalDays: rentalDays

        });


      console.log(
        "BOOKING CREATED:",
        booking
      );


      const resourceTitle =
        selectedResource.title;


      /*
       * Close modal after successful request
       */

      setSelectedResource(null);

      setRentalDays(1);


      /*
       * Show success message
       */

      setBookingSuccess(
        `Borrow request for "${resourceTitle}" sent successfully.`
      );


    } catch (error) {

      console.error(
        "BOOKING ERROR:",
        error
      );

      setBookingError(
        "Unable to send borrow request."
      );

    } finally {

      setSendingRequest(false);
    }
  };


  /* =========================================
     CALCULATE ESTIMATED RENT
  ========================================= */

  const calculateRent = () => {

    if (!selectedResource) {
      return 0;
    }

    return (
      selectedResource.rentPerDay *
      rentalDays
    );
  };


  /* =========================================
     CALCULATE TOTAL
  ========================================= */

  const calculateTotal = () => {

    if (!selectedResource) {
      return 0;
    }

    return (
      calculateRent() +
      selectedResource.securityDeposit
    );
  };


  /* =========================================
     RETURN
  ========================================= */

  return (

    <main className="browse-page">

      <div className="browse-container">


        {/* =====================================
            HEADER
        ===================================== */}

        <div className="browse-header">

          <div>

            <span className="browse-label">
              MARKETPLACE
            </span>

            <h1>
              Browse Resources
            </h1>

            <p>
              Discover resources available
              for rent from other users.
            </p>

          </div>


          <Link
            to="/dashboard"
            className="browse-back-btn"
          >
            ← Dashboard
          </Link>

        </div>


        {/* =====================================
            RESOURCE ERROR
        ===================================== */}

        {error && (

          <div className="browse-error">
            {error}
          </div>

        )}


        {/* =====================================
            BOOKING SUCCESS
        ===================================== */}

        {bookingSuccess && (

          <div className="booking-success">

            {bookingSuccess}

          </div>

        )}


        {/* =====================================
            LOADING
        ===================================== */}

        {loading && (

          <div className="browse-message">

            Loading resources...

          </div>

        )}


        {/* =====================================
            EMPTY STATE
        ===================================== */}

        {!loading &&
          !error &&
          resources.length === 0 && (

            <div className="browse-empty">

              <div className="browse-empty-icon">
                📦
              </div>

              <h2>
                No resources available
              </h2>

              <p>
                There are currently no
                resources available for rent.
              </p>

            </div>

          )}


        {/* =====================================
            RESOURCE GRID
        ===================================== */}

        {!loading &&
          !error &&
          resources.length > 0 && (

            <div className="browse-grid">


              {resources.map(resource => (

                <article
                  key={resource.resourceId}
                  className="browse-card"
                >


                  {/* ===========================
                      IMAGE
                  =========================== */}

                  <div className="browse-card-image">


                    {resource.imageUrl ? (

                      <img
                        src={resource.imageUrl}
                        alt={resource.title}
                      />

                    ) : (

                      <div className="browse-no-image">
                        📦
                      </div>

                    )}


                    <span className="browse-available">
                      Available
                    </span>

                  </div>


                  {/* ===========================
                      CARD CONTENT
                  =========================== */}

                  <div className="browse-card-content">


                    {/* CATEGORY */}

                    <span className="browse-category">

                      {resource.category}

                    </span>


                    {/* TITLE */}

                    <h2>
                      {resource.title}
                    </h2>


                    {/* DESCRIPTION */}

                    <p className="browse-description">

                      {resource.description}

                    </p>


                    {/* LOCATION */}

                    <div className="browse-location">

                      <span>
                        📍
                      </span>

                      <span>

                        {resource.city},{" "}
                        {resource.state}

                      </span>

                    </div>


                    {/* ===========================
                        DETAILS
                    =========================== */}

                    <div className="browse-details">


                      <div>

                        <span>
                          Condition
                        </span>

                        <strong>

                          {formatCondition(
                            resource.condition
                          )}

                        </strong>

                      </div>


                      <div>

                        <span>
                          Quantity
                        </span>

                        <strong>
                          {resource.quantity}
                        </strong>

                      </div>

                    </div>


                    {/* ===========================
                        PRICE
                    =========================== */}

                    <div className="browse-price-row">


                      <div>

                        <span className="browse-price-label">
                          Rent
                        </span>

                        <div className="browse-price">

                          ₹{resource.rentPerDay}

                          <span>
                            {" "}/ day
                          </span>

                        </div>

                      </div>


                      <div className="browse-deposit">

                        <span>
                          Security Deposit
                        </span>

                        <strong>

                          ₹{
                            resource.securityDeposit
                          }

                        </strong>

                      </div>

                    </div>


                    {/* ===========================
                        ACTION BUTTONS
                    =========================== */}

                    <div className="browse-actions">


                      <Link
                        to={
                          `/resources/${resource.resourceId}`
                        }
                        className="browse-view-btn"
                      >
                        View Details
                      </Link>


                      <button
                        type="button"
                        className="borrow-request-btn"
                        onClick={() =>
                          openBorrowModal(resource)
                        }
                      >
                        Send Borrow Request
                      </button>


                    </div>

                  </div>

                </article>

              ))}

            </div>

          )}

      </div>


      {/* =====================================
          BORROW REQUEST MODAL
      ===================================== */}

      {selectedResource && (

        <div
          className="borrow-modal-overlay"
          onClick={closeBorrowModal}
        >

          <div
            className="borrow-modal"
            onClick={(event) =>
              event.stopPropagation()
            }
          >


            {/* ===============================
                MODAL HEADER
            =============================== */}

            <div className="borrow-modal-header">


              <div>

                <span className="borrow-modal-label">
                  BORROW REQUEST
                </span>

                <h2>
                  {selectedResource.title}
                </h2>

              </div>


              <button
                type="button"
                className="borrow-modal-close"
                onClick={closeBorrowModal}
                disabled={sendingRequest}
                aria-label="Close"
              >
                ×
              </button>


            </div>


            {/* ===============================
                RESOURCE SUMMARY
            =============================== */}

            <div className="borrow-resource-summary">


              {selectedResource.imageUrl ? (

                <img
                  src={
                    selectedResource.imageUrl
                  }
                  alt={
                    selectedResource.title
                  }
                />

              ) : (

                <div className="borrow-resource-placeholder">
                  📦
                </div>

              )}


              <div>

                <span>
                  {selectedResource.category}
                </span>

                <strong>
                  {selectedResource.title}
                </strong>

                <p>

                  {selectedResource.city},{" "}
                  {selectedResource.state}

                </p>

              </div>

            </div>


            {/* ===============================
                RENT INFORMATION
            =============================== */}

            <div className="borrow-modal-details">


              <div>

                <span>
                  Rent Per Day
                </span>

                <strong>

                  ₹{
                    selectedResource.rentPerDay
                  }

                </strong>

              </div>


              <div>

                <span>
                  Security Deposit
                </span>

                <strong>

                  ₹{
                    selectedResource
                      .securityDeposit
                  }

                </strong>

              </div>


            </div>


            {/* ===============================
                RENTAL DAYS
            =============================== */}

            <div className="borrow-days-group">

              <label htmlFor="rentalDays">

                Number of Rental Days

              </label>


              <input
                id="rentalDays"
                type="number"
                min="1"
                step="1"
                value={rentalDays}
                onChange={(event) => {

                  const value =
                    Number(
                      event.target.value
                    );

                  setRentalDays(value);

                  setBookingError("");

                }}
              />

            </div>


            {/* ===============================
                PRICE SUMMARY
            =============================== */}

            <div className="borrow-price-summary">


              <div>

                <span>

                  ₹{
                    selectedResource.rentPerDay
                  }

                  {" × "}

                  {rentalDays}

                  {" "}

                  {rentalDays === 1
                    ? "day"
                    : "days"
                  }

                </span>


                <strong>

                  ₹{calculateRent()}

                </strong>

              </div>


              <div>

                <span>
                  Security Deposit
                </span>

                <strong>

                  ₹{
                    selectedResource
                      .securityDeposit
                  }

                </strong>

              </div>


              <div className="borrow-total">

                <span>
                  Estimated Amount
                </span>

                <strong>

                  ₹{calculateTotal()}

                </strong>

              </div>


            </div>


            {/* ===============================
                BOOKING ERROR
            =============================== */}

            {bookingError && (

              <div className="booking-modal-error">

                {bookingError}

              </div>

            )}


            {/* ===============================
                ACTION BUTTONS
            =============================== */}

            <div className="borrow-modal-actions">


              <button
                type="button"
                className="borrow-cancel-btn"
                onClick={closeBorrowModal}
                disabled={sendingRequest}
              >
                Cancel
              </button>


              <button
                type="button"
                className="borrow-confirm-btn"
                onClick={
                  handleBorrowRequest
                }
                disabled={
                  sendingRequest ||
                  rentalDays < 1 ||
                  !Number.isInteger(
                    rentalDays
                  )
                }
              >

                {sendingRequest
                  ? "Sending..."
                  : "Send Request"
                }

              </button>


            </div>

          </div>

        </div>

      )}

    </main>

  );
}


export default BrowseResources;