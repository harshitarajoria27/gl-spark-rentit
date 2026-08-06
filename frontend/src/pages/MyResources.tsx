import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import {
  getMyResources,
  deleteResource
} from "../api/resourceApi";
import type {
  ResourceResponse
} from "../types/resource";

import "./MyResources.css";


function MyResources() {

  const [resources, setResources] =
    useState<ResourceResponse[]>([]);

  const [loading, setLoading] =
    useState(true);

  const [deletingId, setDeletingId] =
  useState<number | null>(null);

  const [error, setError] =
    useState("");


  /* =========================================
     LOAD MY RESOURCES
  ========================================= */

  useEffect(() => {

    const loadResources = async () => {

      try {

        setLoading(true);
        setError("");

        const data =
          await getMyResources();

        console.log(
          "MY RESOURCES:",
          data
        );

        setResources(data);

      } catch (error) {

        console.error(
          "Failed to load resources:",
          error
        );

        setError(
          "Unable to load your resources."
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
    condition: string
  ) => {

    return condition
      .replace("_", " ")
      .toLowerCase()
      .replace(/\b\w/g, char =>
        char.toUpperCase()
      );
  };
  const handleDelete = async (
  id: number
) => {

  const confirmed = window.confirm(
    "Are you sure you want to delete this resource?"
  );

  if (!confirmed) {
    return;
  }

  try {

    setDeletingId(id);
    setError("");

    await deleteResource(id);

    // Remove deleted resource from UI
    setResources(previous =>
      previous.filter(
        resource => resource.resourceId !== id
      )
    );

  } catch (error) {

    console.error(
      "Failed to delete resource:",
      error
    );

    setError(
      "Unable to delete resource."
    );

  } finally {

    setDeletingId(null);
  }
};


  /* =========================================
     RETURN
  ========================================= */

  return (

    <main className="my-resources-page">

      <div className="my-resources-container">


        {/* HEADER */}

        <div className="my-resources-header">

          <div>

            <span className="resource-page-label">
              RESOURCES
            </span>

            <h1>
              My Resources
            </h1>

            <p>
              View and manage all the resources
              you've listed for rent.
            </p>

          </div>


          <Link
            to="/resources/add"
            className="add-new-resource-btn"
          >
            + Add Resource
          </Link>

        </div>


        {/* ERROR */}

        {error && (

          <div className="resources-error">
            {error}
          </div>

        )}


        {/* LOADING */}

        {loading && (

          <div className="resources-message">
            Loading your resources...
          </div>

        )}


        {/* EMPTY STATE */}

        {!loading &&
          !error &&
          resources.length === 0 && (

            <div className="empty-resources">

              <div className="empty-resource-icon">
                📦
              </div>

              <h2>
                No resources yet
              </h2>

              <p>
                You haven't listed any resources
                for rent yet.
              </p>

              <Link
                to="/resources/add"
                className="empty-add-resource-btn"
              >
                + Add Your First Resource
              </Link>

            </div>

          )}


        {/* RESOURCE GRID */}

        {!loading &&
          resources.length > 0 && (

            <div className="resources-grid">

              {resources.map(resource => (

                <article
                  className="resource-card"
                  key={resource.resourceId}
                >


                  {/* IMAGE */}

                  <div className="resource-card-image">

                    {resource.imageUrl ? (

                      <img
                        src={resource.imageUrl}
                        alt={resource.title}
                      />

                    ) : (

                      <div className="resource-no-image">
                        📦
                      </div>

                    )}


                    <span
                      className={
                        resource.available
                          ? "availability-badge available"
                          : "availability-badge unavailable"
                      }
                    >

                      {resource.available
                        ? "Available"
                        : "Unavailable"
                      }

                    </span>

                  </div>


                  {/* CONTENT */}

                  <div className="resource-card-content">


                    {/* CATEGORY */}

                    <span className="resource-category">
                      {resource.category}
                    </span>


                    {/* TITLE */}

                    <h2>
                      {resource.title}
                    </h2>


                    {/* DESCRIPTION */}

                    <p className="resource-description">
                      {resource.description}
                    </p>


                    {/* LOCATION */}

                    <div className="resource-location">

                      <span>
                        📍
                      </span>

                      <span>
                        {resource.city},
                        {" "}
                        {resource.state}
                      </span>

                    </div>


                    {/* DETAILS */}

                    <div className="resource-card-details">


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


                    {/* PRICE */}

                    <div className="resource-price-section">

                      <div>

                        <span className="price-label">
                          Rent
                        </span>

                        <div className="resource-price">

                          ₹{resource.rentPerDay}

                          <span>
                            / day
                          </span>

                        </div>

                      </div>


                      <div className="security-deposit">

                        <span>
                          Deposit
                        </span>

                        <strong>
                          ₹{resource.securityDeposit}
                        </strong>

                      </div>

                    </div>


                    {/* ACTIONS */}

                    <div className="resource-card-actions">

  {/* <Link
    to={`/resources/${resource.resourceId}`}
    className="view-resource-btn"
  >
    View
  </Link> */}


  <Link
    to={`/resources/${resource.resourceId}/edit`}
    className="edit-resource-btn"
  >
    Edit
  </Link>


  <button
    type="button"
    className="delete-resource-btn"
    onClick={() =>
      handleDelete(resource.resourceId)
    }
    disabled={deletingId === resource.resourceId}
  >
    {deletingId === resource.resourceId
      ? "Deleting..."
      : "Delete"
    }
  </button>

</div>

                  </div>

                </article>

              ))}

            </div>

          )}


        {/* BACK */}

        <div className="resources-back">

          <Link to="/dashboard">
            ← Back to Dashboard
          </Link>

        </div>

      </div>

    </main>
  );
}


export default MyResources;