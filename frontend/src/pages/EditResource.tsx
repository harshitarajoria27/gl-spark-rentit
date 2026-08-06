import {
  useEffect,
  useState
} from "react";

import {
  Link,
  useNavigate,
  useParams
} from "react-router-dom";

import {
  getResourceById,
  updateResource
} from "../api/resourceApi";

import type {
  ResourceCondition,
  ResourceUpdateRequest
} from "../types/resource";

import "./EditResource.css";


function EditResource() {

  const { id } = useParams();

  const navigate = useNavigate();


  /* =========================================
     STATE
  ========================================= */

  const [formData, setFormData] =
    useState<ResourceUpdateRequest>({
      title: "",
      description: "",
      category: "",

      rentPerDay: 0,
      securityDeposit: 0,
      quantity: 1,

      condition: "GOOD",

      city: "",
      state: "",

      imageUrl: "",

      available: true
    });


  const [loading, setLoading] =
    useState(true);

  const [saving, setSaving] =
    useState(false);

  const [error, setError] =
    useState("");


  /* =========================================
     LOAD EXISTING RESOURCE
  ========================================= */

  useEffect(() => {

  const loadResource = async () => {

    if (!id) {
      setError("Invalid resource.");
      setLoading(false);
      return;
    }

    try {

      setLoading(true);
      setError("");

      const resource =
        await getResourceById(Number(id));

      console.log(
        "RESOURCE TO EDIT:",
        resource
      );

      setFormData({
        title: resource.title ?? "",
        description: resource.description ?? "",
        category: resource.category ?? "",

        rentPerDay: resource.rentPerDay ?? 0,
        securityDeposit: resource.securityDeposit ?? 0,
        quantity: resource.quantity ?? 1,

        condition: resource.condition ?? "GOOD",

        city: resource.city ?? "",
        state: resource.state ?? "",

        imageUrl: resource.imageUrl ?? "",

        available: resource.available ?? true
      });

    } catch (error) {

      console.error(
        "Failed to load resource:",
        error
      );

      setError(
        "Unable to load resource."
      );

    } finally {

      // THIS IS IMPORTANT
      setLoading(false);

    }
  };


  loadResource();

}, [id]);


  /* =========================================
     NORMAL INPUT CHANGE
  ========================================= */

  const handleChange = (
    event:
      React.ChangeEvent<
        HTMLInputElement |
        HTMLTextAreaElement
      >
  ) => {

    const { name, value } =
      event.target;


    if (
      name === "rentPerDay" ||
      name === "securityDeposit" ||
      name === "quantity"
    ) {

      setFormData(previous => ({

        ...previous,

        [name]:
          name === "quantity"
            ? Number.parseInt(value) || 0
            : Number.parseFloat(value) || 0

      }));

      return;
    }


    setFormData(previous => ({
      ...previous,
      [name]: value
    }));
  };


  /* =========================================
     CONDITION
  ========================================= */

  const handleConditionChange = (
  event: React.ChangeEvent<HTMLSelectElement>
) => {
  const selectedCondition =
    event.target.value as ResourceCondition;

  setFormData((previous) => ({
    ...previous,
    condition: selectedCondition,
  }));
};


  /* =========================================
     AVAILABILITY
  ========================================= */

  const handleAvailabilityChange = (
    event:
      React.ChangeEvent<HTMLSelectElement>
  ) => {

    setFormData(previous => ({
      ...previous,

      available:
        event.target.value === "true"
    }));
  };


  /* =========================================
     SUBMIT
  ========================================= */

  const handleSubmit = async (
    event:
      React.FormEvent<HTMLFormElement>
  ) => {

    event.preventDefault();


    if (!id) {
      return;
    }


    try {

      setSaving(true);
      setError("");


      const updatedResource =
        await updateResource(
          Number(id),
          formData
        );


      console.log(
        "RESOURCE UPDATED:",
        updatedResource
      );


      navigate(
        "/resources/my"
      );


    } catch (error) {

      console.error(
        "Failed to update resource:",
        error
      );

      setError(
        "Unable to update resource."
      );

    } finally {

      setSaving(false);
    }
  };


  /* =========================================
     LOADING
  ========================================= */

  if (loading) {

    return (

      <main className="edit-resource-page">

        <div className="edit-resource-container">

          <div className="edit-resource-message">
            Loading resource...
          </div>

        </div>

      </main>

    );
  }


  /* =========================================
     RETURN
  ========================================= */

  return (

    <main className="edit-resource-page">

      <div className="edit-resource-container">


        {/* HEADER */}

        <div className="edit-resource-header">

          <div>

            <span className="edit-resource-label">
              RESOURCE
            </span>

            <h1>
              Edit Resource
            </h1>

            <p>
              Update your resource information,
              pricing and availability.
            </p>

          </div>


          <Link
            to="/resources/my"
            className="back-resources-btn"
          >
            ← My Resources
          </Link>

        </div>


        {/* FORM */}

        <form
          className="edit-resource-form"
          onSubmit={handleSubmit}
        >


          {/* ERROR */}

          {error && (

            <div className="edit-resource-error">
              {error}
            </div>

          )}


          {/* BASIC INFO */}

          <section className="edit-form-section">

            <div className="edit-section-heading">

              <h2>
                Basic Information
              </h2>

              <p>
                Update the basic details of
                your resource.
              </p>

            </div>


            <div className="edit-resource-grid">


              <div className="edit-form-group">

                <label>
                  Resource Title
                </label>

                <input
                  type="text"
                  name="title"
                  value={formData.title}
                  onChange={handleChange}
                  required
                />

              </div>


              <div className="edit-form-group">

                <label>
                  Category
                </label>

                <input
                  type="text"
                  name="category"
                  value={formData.category}
                  onChange={handleChange}
                  required
                />

              </div>


              <div
                className="
                  edit-form-group
                  edit-full-width
                "
              >

                <label>
                  Description
                </label>

                <textarea
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  rows={5}
                  required
                />

              </div>

            </div>

          </section>


          {/* PRICING */}

          <section className="edit-form-section">

            <div className="edit-section-heading">

              <h2>
                Pricing & Quantity
              </h2>

              <p>
                Update pricing, quantity and
                condition.
              </p>

            </div>


            <div className="edit-resource-grid">


              <div className="edit-form-group">

                <label>
                  Rent Per Day (₹)
                </label>

                <input
                  type="number"
                  name="rentPerDay"
                  value={formData.rentPerDay}
                  onChange={handleChange}
                  min="0"
                  step="0.01"
                  required
                />

              </div>


              <div className="edit-form-group">

                <label>
                  Security Deposit (₹)
                </label>

                <input
                  type="number"
                  name="securityDeposit"
                  value={
                    formData.securityDeposit
                  }
                  onChange={handleChange}
                  min="0"
                  step="0.01"
                  required
                />

              </div>


              <div className="edit-form-group">

                <label>
                  Quantity
                </label>

                <input
                  type="number"
                  name="quantity"
                  value={formData.quantity}
                  onChange={handleChange}
                  min="1"
                  required
                />

              </div>


              <div className="edit-form-group">

                <label>
                  Condition
                </label>

                <select
                  name="condition"
                  value={formData.condition}
                  onChange={
                    handleConditionChange
                  }
                >

                  <option value="NEW">
                    New
                  </option>

                  <option value="LIKE_NEW">
                    Like New
                  </option>

                  <option value="GOOD">
                    Good
                  </option>

                  <option value="FAIR">
                    Fair
                  </option>

                  <option value="POOR">
                    Poor
                  </option>

                </select>

              </div>

            </div>

          </section>


          {/* LOCATION */}

          <section className="edit-form-section">

            <div className="edit-section-heading">

              <h2>
                Location & Availability
              </h2>

              <p>
                Update where the resource is
                located and whether renters
                can currently book it.
              </p>

            </div>


            <div className="edit-resource-grid">


              <div className="edit-form-group">

                <label>
                  City
                </label>

                <input
                  type="text"
                  name="city"
                  value={formData.city}
                  onChange={handleChange}
                  required
                />

              </div>


              <div className="edit-form-group">

                <label>
                  State
                </label>

                <input
                  type="text"
                  name="state"
                  value={formData.state}
                  onChange={handleChange}
                  required
                />

              </div>


              <div className="edit-form-group">

                <label>
                  Availability
                </label>

                <select
                  value={
                    String(
                      formData.available
                    )
                  }
                  onChange={
                    handleAvailabilityChange
                  }
                >

                  <option value="true">
                    Available
                  </option>

                  <option value="false">
                    Unavailable
                  </option>

                </select>

              </div>

            </div>

          </section>


          {/* IMAGE */}

          <section className="edit-form-section">

            <div className="edit-section-heading">

              <h2>
                Resource Image
              </h2>

              <p>
                Update the image used for
                this resource.
              </p>

            </div>


            <div className="edit-form-group">

              <label>
                Image URL
              </label>

              <input
                type="url"
                name="imageUrl"
                value={formData.imageUrl}
                onChange={handleChange}
                placeholder="https://example.com/image.jpg"
              />

            </div>


            {formData.imageUrl && (

              <div className="edit-image-preview">

                <img
                  src={formData.imageUrl}
                  alt="Resource preview"
                />

              </div>

            )}

          </section>


          {/* ACTIONS */}

          <div className="edit-form-actions">

            <Link
              to="/resources/my"
              className="cancel-edit-btn"
            >
              Cancel
            </Link>


            <button
              type="submit"
              className="save-edit-btn"
              disabled={saving}
            >

              {saving
                ? "Saving..."
                : "Save Changes"
              }

            </button>

          </div>

        </form>

      </div>

    </main>
  );
}


export default EditResource;