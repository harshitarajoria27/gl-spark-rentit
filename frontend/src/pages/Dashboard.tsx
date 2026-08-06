import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import AddResource from "./AddResource";

import {
  getProfile,
  updateProfile
} from "../api/userApi";

import type {
  UserResponse,
  UpdateProfileRequest
} from "../types/user";

import "./Dashboard.css";


function Dashboard() {

  /* =====================================================
     STATE
  ===================================================== */

  const [profile, setProfile] =
    useState<UserResponse | null>(null);

  const [loadingProfile, setLoadingProfile] =
    useState(true);

  const [profileError, setProfileError] =
    useState("");

  const [editing, setEditing] =
    useState(false);

  const [saving, setSaving] =
    useState(false);

  const [success, setSuccess] =
    useState("");


  /* =====================================================
     EDIT PROFILE FORM DATA
  ===================================================== */

  const [formData, setFormData] =
    useState<UpdateProfileRequest>({
      phone: "",
      profileImage: "",
      bio: "",
      address: "",
      city: "",
      state: "",
      pincode: ""
    });


  /* =====================================================
     GET PROFILE
  ===================================================== */

  useEffect(() => {

    const loadProfile = async () => {

      try {

        setLoadingProfile(true);
        setProfileError("");

        const data = await getProfile();

        console.log("PROFILE FROM API:", data);

        setProfile(data);


        // Populate edit form with existing values
        setFormData({
          phone: data.phone ?? "",
          profileImage: data.profileImage ?? "",
          bio: data.bio ?? "",
          address: data.address ?? "",
          city: data.city ?? "",
          state: data.state ?? "",
          pincode: data.pincode ?? ""
        });

      } catch (error) {

        console.error(
          "Failed to load profile:",
          error
        );

        setProfileError(
          "Unable to load your profile."
        );

      } finally {

        setLoadingProfile(false);
      }
    };


    loadProfile();

  }, []);


  /* =====================================================
     INPUT CHANGE
  ===================================================== */

  const handleChange = (
    event:
      React.ChangeEvent<
        HTMLInputElement | HTMLTextAreaElement
      >
  ) => {

    const { name, value } =
      event.target;

    setFormData(previous => ({
      ...previous,
      [name]: value
    }));
  };


  /* =====================================================
     START EDITING
  ===================================================== */

  const handleEditProfile = () => {

    if (!profile) {
      return;
    }


    // Reset form to current profile values
    setFormData({
      phone: profile.phone ?? "",
      profileImage: profile.profileImage ?? "",
      bio: profile.bio ?? "",
      address: profile.address ?? "",
      city: profile.city ?? "",
      state: profile.state ?? "",
      pincode: profile.pincode ?? ""
    });


    setProfileError("");
    setSuccess("");

    setEditing(true);
  };


  /* =====================================================
     CANCEL EDITING
  ===================================================== */

  const handleCancelEdit = () => {

    if (profile) {

      setFormData({
        phone: profile.phone ?? "",
        profileImage: profile.profileImage ?? "",
        bio: profile.bio ?? "",
        address: profile.address ?? "",
        city: profile.city ?? "",
        state: profile.state ?? "",
        pincode: profile.pincode ?? ""
      });
    }

    setEditing(false);
    setProfileError("");
  };


  /* =====================================================
     UPDATE PROFILE
  ===================================================== */

  const handleUpdateProfile = async (
    event: React.FormEvent<HTMLFormElement>
  ) => {

    event.preventDefault();


    if (!profile) {
      return;
    }


    try {

      setSaving(true);

      setSuccess("");
      setProfileError("");


      const updatedProfile =
        await updateProfile(
          profile.email,
          formData
        );


      setProfile(updatedProfile);


      // Keep form synchronized with backend response
      setFormData({
        phone: updatedProfile.phone ?? "",
        profileImage:
          updatedProfile.profileImage ?? "",
        bio: updatedProfile.bio ?? "",
        address: updatedProfile.address ?? "",
        city: updatedProfile.city ?? "",
        state: updatedProfile.state ?? "",
        pincode: updatedProfile.pincode ?? ""
      });


      setEditing(false);

      setSuccess(
        "Profile updated successfully."
      );


    } catch (error) {

      console.error(
        "Profile update failed:",
        error
      );

      setProfileError(
        "Unable to update profile."
      );

    } finally {

      setSaving(false);
    }
  };


  /* =====================================================
     PROFILE INITIAL
  ===================================================== */

  const profileInitial =
    profile?.fullName
      ?.trim()
      .charAt(0)
      .toUpperCase() || "?";


  /* =====================================================
     RETURN
  ===================================================== */

  return (

    <main className="dashboard-page">

      <div className="dashboard-container">


        {/* =================================================
            DASHBOARD HEADER
        ================================================= */}

        <div className="dashboard-header">

          <div>

            <span className="dashboard-label">
              OVERVIEW
            </span>

            <h1>
              {profile
                ? `Welcome, ${profile.fullName} 👋`
                : "Welcome to RentIt 👋"
              }
            </h1>

            <p>
              Manage your resources, bookings,
              transactions and profile from one place.
            </p>

          </div>


          <Link
            to="/resources/add"
            className="add-resource-btn"
          >
            + Add Resource
          </Link>

        </div>


        {/* =================================================
            PROFILE SECTION
        ================================================= */}

        <section className="profile-section">


          {/* PROFILE HEADER */}

          <div className="profile-section-header">

            <div>

              <span className="dashboard-label">
                ACCOUNT
              </span>

              <h2>
                My Profile
              </h2>

              <p>
                View and manage your personal
                information.
              </p>

            </div>


            {!editing && profile && (

              <button
                type="button"
                className="edit-profile-btn"
                onClick={handleEditProfile}
              >
                Edit Profile
              </button>

            )}

          </div>


          {/* =================================================
              LOADING
          ================================================= */}

          {loadingProfile && (

            <div className="profile-loading">
              Loading your profile...
            </div>

          )}


          {/* =================================================
              ERROR MESSAGE
          ================================================= */}

          {profileError && (

            <div className="profile-error">
              {profileError}
            </div>

          )}


          {/* =================================================
              SUCCESS MESSAGE
          ================================================= */}

          {success && (

            <div className="profile-success">
              {success}
            </div>

          )}


          {/* =================================================
              PROFILE VIEW
          ================================================= */}

          {!loadingProfile &&
            profile &&
            !editing && (

              <div className="profile-card">


                {/* USER BASIC INFO */}

                <div className="profile-main">


                  {/* PROFILE IMAGE OR INITIAL */}

                  {profile.profileImage ? (

                    <img
                      src={profile.profileImage}
                      alt={`${profile.fullName} profile`}
                      className="profile-avatar profile-avatar-image"
                    />

                  ) : (

                    <div className="profile-avatar">
                      {profileInitial}
                    </div>

                  )}


                  <div>

                    <h3>
                      {profile.fullName}
                    </h3>

                    <p>
                      {profile.email}
                    </p>

                    {profile.bio && (

                      <p className="profile-bio">
                        {profile.bio}
                      </p>

                    )}

                  </div>

                </div>


                {/* PROFILE DETAILS */}

                <div className="profile-details">


                  <div className="profile-detail">

                    <span>
                      Phone
                    </span>

                    <strong>
                      {profile.phone ||
                        "Not provided"}
                    </strong>

                  </div>


                  <div className="profile-detail">

                    <span>
                      Address
                    </span>

                    <strong>
                      {profile.address ||
                        "Not provided"}
                    </strong>

                  </div>


                  <div className="profile-detail">

                    <span>
                      City
                    </span>

                    <strong>
                      {profile.city ||
                        "Not provided"}
                    </strong>

                  </div>


                  <div className="profile-detail">

                    <span>
                      State
                    </span>

                    <strong>
                      {profile.state ||
                        "Not provided"}
                    </strong>

                  </div>


                  <div className="profile-detail">

                    <span>
                      Pincode
                    </span>

                    <strong>
                      {profile.pincode ||
                        "Not provided"}
                    </strong>

                  </div>

                </div>

              </div>

            )}


          {/* =================================================
              EDIT PROFILE
          ================================================= */}

          {editing && profile && (

            <form
              className="profile-edit-form"
              onSubmit={handleUpdateProfile}
            >


              <div className="profile-form-grid">


                {/* FULL NAME */}

                <div className="profile-form-group">

                  <label>
                    Full Name
                  </label>

                  <input
                    type="text"
                    value={profile.fullName}
                    disabled
                  />

                </div>


                {/* EMAIL */}

                <div className="profile-form-group">

                  <label>
                    Email
                  </label>

                  <input
                    type="email"
                    value={profile.email}
                    disabled
                  />

                </div>


                {/* PHONE */}

                <div className="profile-form-group">

                  <label>
                    Phone
                  </label>

                  <input
                    type="tel"
                    name="phone"
                    value={formData.phone}
                    onChange={handleChange}
                    placeholder="Enter phone number"
                    maxLength={10}
                  />

                </div>


                {/* PROFILE IMAGE */}

                <div className="profile-form-group">

                  <label>
                    Profile Image URL
                  </label>

                  <input
                    type="url"
                    name="profileImage"
                    value={formData.profileImage}
                    onChange={handleChange}
                    placeholder="Enter profile image URL"
                  />

                </div>


                {/* ADDRESS */}

                <div className="profile-form-group">

                  <label>
                    Address
                  </label>

                  <input
                    type="text"
                    name="address"
                    value={formData.address}
                    onChange={handleChange}
                    placeholder="Enter your address"
                  />

                </div>


                {/* CITY */}

                <div className="profile-form-group">

                  <label>
                    City
                  </label>

                  <input
                    type="text"
                    name="city"
                    value={formData.city}
                    onChange={handleChange}
                    placeholder="Enter your city"
                  />

                </div>


                {/* STATE */}

                <div className="profile-form-group">

                  <label>
                    State
                  </label>

                  <input
                    type="text"
                    name="state"
                    value={formData.state}
                    onChange={handleChange}
                    placeholder="Enter your state"
                  />

                </div>


                {/* PINCODE */}

                <div className="profile-form-group">

                  <label>
                    Pincode
                  </label>

                  <input
                    type="text"
                    name="pincode"
                    value={formData.pincode}
                    onChange={handleChange}
                    placeholder="Enter pincode"
                    maxLength={6}
                  />

                </div>


                {/* BIO */}

                <div
                  className="
                    profile-form-group
                    profile-bio-group
                  "
                >

                  <label>
                    Bio
                  </label>

                  <textarea
                    name="bio"
                    value={formData.bio}
                    onChange={handleChange}
                    placeholder="Tell us something about yourself..."
                    maxLength={300}
                    rows={4}
                  />

                  <small>
                    {formData.bio.length}/300
                  </small>

                </div>

              </div>


              {/* FORM ACTIONS */}

              <div className="profile-form-actions">

                <button
                  type="button"
                  className="cancel-profile-btn"
                  onClick={handleCancelEdit}
                  disabled={saving}
                >
                  Cancel
                </button>


                <button
                  type="submit"
                  className="save-profile-btn"
                  disabled={saving}
                >

                  {saving
                    ? "Saving..."
                    : "Save Changes"
                  }

                </button>

              </div>

            </form>

          )}

        </section>


        {/* =================================================
            DASHBOARD STATS
        ================================================= */}

        


        {/* =================================================
            QUICK ACTIONS HEADER
        ================================================= */}

        <div className="dashboard-section-header">

          <h2>
            Quick Actions
          </h2>

          <p>
            What would you like to do?
          </p>

        </div>


        {/* =================================================
            QUICK ACTIONS
        ================================================= */}

        <div className="dashboard-actions-grid">


          <Link
            to="/resources"
            className="dashboard-action-card"
          >

            <div className="action-icon">
              🔍
            </div>

            <div>

              <h3>
                Browse Resources
              </h3>

              <p>
                Discover items available for rent.
              </p>

              <span>
                Browse →
              </span>

            </div>

          </Link>


          <Link
            to="/resources/add"
            className="dashboard-action-card"
          >

            <div className="action-icon">
              ➕
            </div>

            <div>

              <h3>
                Add Resource
              </h3>

              <p>
                List something you own for rent.
              </p>

              <span>
                Add Resource →
              </span>

            </div>

          </Link>


          <Link
            to="/resources/my"
            className="dashboard-action-card"
          >

            <div className="action-icon">
              📦
            </div>

            <div>

              <h3>
                My Resources
              </h3>

              <p>
                Manage resources you've listed.
              </p>

              <span>
                Manage →
              </span>

            </div>

          </Link>


          <Link
  to="/borrow-requests"
  className="dashboard-action-card"
>
  <div className="action-icon">
    📥
  </div>

  <div>
    <h3>
      Borrow Requests
    </h3>

    <p>
      Review requests for your resources.
    </p>

    <span>
      View Requests →
    </span>
  </div>
</Link>

          <Link
            to="/transactions/my"
            className="dashboard-action-card"
          >

            <div className="action-icon">
              🧾
            </div>

            <div>

              <h3>
                My Transactions
              </h3>

              <p>
                View your transaction history.
              </p>

              <span>
                View Transactions →
              </span>

            </div>

          </Link>


          <Link
            to="/transactions/owned"
            className="dashboard-action-card"
          >

            <div className="action-icon">
              💼
            </div>

            <div>

              <h3>
                Owner Transactions
              </h3>

              <p>
                Manage transactions for your items.
              </p>

              <span>
                Manage →
              </span>

            </div>

          </Link>
          <Link
  to="/bookings/my"
  className="dashboard-action-card"
>

  <div className="action-icon">
    📅
  </div>

  <div>

    <h3>
      My Bookings
    </h3>

    <p>
      Track all the resources you have requested to borrow.
    </p>

    <span>
      View Bookings →
    </span>

  </div>

</Link>

        </div>

      </div>

    </main>
  );
}


export default Dashboard;