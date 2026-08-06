import {
  useState
} from "react";

import {
  Link,
  useNavigate
} from "react-router-dom";

import {
  addResource
} from "../api/resourceApi";

import type {
  ResourceRequest,
  ResourceCondition
} from "../types/resource";

import "./AddResource.css";


function AddResource() {

  const navigate = useNavigate();


  const [formData, setFormData] =
    useState<ResourceRequest>({

      title: "",
      description: "",
      category: "",

      rentPerDay: 0,
      securityDeposit: 0,
      quantity: 1,

      condition: "GOOD",

      city: "",
      state: "",

      imageUrl: ""
    });


  const [saving, setSaving] =
    useState(false);

  const [error, setError] =
    useState("");


  /* =========================
     INPUT CHANGE
  ========================= */

  const handleChange = (
    event:
      React.ChangeEvent<
        HTMLInputElement |
        HTMLTextAreaElement |
        HTMLSelectElement
      >
  ) => {

    const { name, value } =
      event.target;


    // Numeric fields
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


  /* =========================
     CONDITION CHANGE
  ========================= */

  const handleConditionChange = (
  event: React.ChangeEvent<HTMLSelectElement>
) => {
  const condition = event.target.value as ResourceCondition;

  setFormData((previous) => ({
    ...previous,
    condition: condition
  }));
};


  /* =========================
     SUBMIT RESOURCE
  ========================= */

  const handleSubmit = async (
    event: React.FormEvent<HTMLFormElement>
  ) => {

    event.preventDefault();

    try {

      setSaving(true);
      setError("");


      const createdResource =
        await addResource(formData);


      console.log(
        "RESOURCE CREATED:",
        createdResource
      );


      // After creation go to My Resources
      navigate("/resources/my");


    } catch (error) {

      console.error(
        "Failed to add resource:",
        error
      );

      setError(
        "Unable to add resource. Please try again."
      );

    } finally {

      setSaving(false);
    }
  };


  return (

    <main className="add-resource-page">

      <div className="add-resource-container">


        {/* HEADER */}

        <div className="add-resource-header">

          <div>

            <span className="resource-label">
              RESOURCE
            </span>

            <h1>
              Add Resource
            </h1>

            <p>
              List an item you own and make it
              available for rent.
            </p>

          </div>


          <Link
            to="/dashboard"
            className="back-dashboard-btn"
          >
            ← Dashboard
          </Link>

        </div>


        {/* FORM CARD */}

        <form
          className="resource-form-card"
          onSubmit={handleSubmit}
        >


          {error && (

            <div className="resource-error">
              {error}
            </div>

          )}


          {/* BASIC INFORMATION */}

          <div className="resource-form-section">

            <div className="resource-section-heading">

              <h2>
                Basic Information
              </h2>

              <p>
                Tell renters what you're listing.
              </p>

            </div>


            <div className="resource-form-grid">


              {/* TITLE */}

              <div className="resource-form-group">

                <label htmlFor="title">
                  Resource Title
                </label>

                <input
                  id="title"
                  type="text"
                  name="title"
                  value={formData.title}
                  onChange={handleChange}
                  placeholder="e.g. Canon DSLR Camera"
                  required
                />

              </div>


              {/* CATEGORY */}

              <div className="resource-form-group">

                <label htmlFor="category">
                  Category
                </label>

                <input
                  id="category"
                  type="text"
                  name="category"
                  value={formData.category}
                  onChange={handleChange}
                  placeholder="e.g. Electronics"
                  required
                />

              </div>


              {/* DESCRIPTION */}

              <div
                className="
                  resource-form-group
                  resource-full-width
                "
              >

                <label htmlFor="description">
                  Description
                </label>

                <textarea
                  id="description"
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  placeholder="Describe your resource, its features and anything the renter should know..."
                  rows={5}
                  required
                />

              </div>

            </div>

          </div>


          {/* PRICING */}

          <div className="resource-form-section">

            <div className="resource-section-heading">

              <h2>
                Pricing & Quantity
              </h2>

              <p>
                Set the rental price and
                availability.
              </p>

            </div>


            <div className="resource-form-grid">


              {/* RENT */}

              <div className="resource-form-group">

                <label htmlFor="rentPerDay">
                  Rent Per Day (₹)
                </label>

                <input
                  id="rentPerDay"
                  type="number"
                  name="rentPerDay"
                  value={formData.rentPerDay}
                  onChange={handleChange}
                  min="0"
                  step="0.01"
                  required
                />

              </div>


              {/* SECURITY DEPOSIT */}

              <div className="resource-form-group">

                <label htmlFor="securityDeposit">
                  Security Deposit (₹)
                </label>

                <input
                  id="securityDeposit"
                  type="number"
                  name="securityDeposit"
                  value={formData.securityDeposit}
                  onChange={handleChange}
                  min="0"
                  step="0.01"
                  required
                />

              </div>


              {/* QUANTITY */}

              <div className="resource-form-group">

                <label htmlFor="quantity">
                  Quantity
                </label>

                <input
                  id="quantity"
                  type="number"
                  name="quantity"
                  value={formData.quantity}
                  onChange={handleChange}
                  min="1"
                  required
                />

              </div>


              {/* CONDITION */}

              <div className="resource-form-group">

                <label htmlFor="condition">
                  Condition
                </label>

                <select
                  id="condition"
                  name="condition"
                  value={formData.condition}
                  onChange={handleConditionChange}
                  required
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

          </div>


          {/* LOCATION */}

          <div className="resource-form-section">

            <div className="resource-section-heading">

              <h2>
                Location
              </h2>

              <p>
                Tell renters where the item is
                available.
              </p>

            </div>


            <div className="resource-form-grid">


              {/* CITY */}

              <div className="resource-form-group">

                <label htmlFor="city">
                  City
                </label>

                <input
                  id="city"
                  type="text"
                  name="city"
                  value={formData.city}
                  onChange={handleChange}
                  placeholder="e.g. Noida"
                  required
                />

              </div>


              {/* STATE */}

              <div className="resource-form-group">

                <label htmlFor="state">
                  State
                </label>

                <input
                  id="state"
                  type="text"
                  name="state"
                  value={formData.state}
                  onChange={handleChange}
                  placeholder="e.g. Uttar Pradesh"
                  required
                />

              </div>

            </div>

          </div>


          {/* IMAGE */}

          <div className="resource-form-section">

            <div className="resource-section-heading">

              <h2>
                Resource Image
              </h2>

              <p>
                Add an image URL so renters can
                see your item.
              </p>

            </div>


            <div className="resource-form-group">

              <label htmlFor="imageUrl">
                Image URL
              </label>

              <input
                id="imageUrl"
                type="url"
                name="imageUrl"
                value={formData.imageUrl}
                onChange={handleChange}
                placeholder="https://example.com/image.jpg"
              />

            </div>


            {/* IMAGE PREVIEW */}

            {formData.imageUrl && (

              <div className="resource-image-preview">

                <img
                  src={formData.imageUrl}
                  alt="Resource preview"
                />

              </div>

            )}

          </div>


          {/* ACTIONS */}

          <div className="resource-form-actions">

            <Link
              to="/dashboard"
              className="cancel-resource-btn"
            >
              Cancel
            </Link>


            <button
              type="submit"
              className="save-resource-btn"
              disabled={saving}
            >

              {saving
                ? "Adding Resource..."
                : "+ Add Resource"
              }

            </button>

          </div>

        </form>

      </div>

    </main>
  );
}


export default AddResource;