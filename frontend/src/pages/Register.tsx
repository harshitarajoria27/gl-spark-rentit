import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import "./Auth.css";

interface RegisterFormData {
  fullName: string;
  email: string;
  phone: string;
  password: string;
  address: string;
  city: string;
  state: string;
  pincode: string;
}

function Register() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState<RegisterFormData>({
    fullName: "",
    email: "",
    phone: "",
    password: "",
    address: "",
    city: "",
    state: "",
    pincode: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleChange = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { name, value } = event.target;

    setFormData({
      ...formData,
      [name]: value,
    });

    // Remove old error when user starts correcting input
    setError("");
  };


  // =====================================
  // VALIDATION
  // =====================================

  const validateForm = (): boolean => {

    // -------------------------
    // NAME VALIDATION
    // -------------------------

    const nameRegex = /^[A-Za-z ]+$/;

    if (!nameRegex.test(formData.fullName.trim())) {
      setError(
        "Full name can contain only letters and spaces."
      );
      return false;
    }


    // -------------------------
    // PASSWORD VALIDATION
    // -------------------------

    if (formData.password.length < 6) {
      setError(
        "Password must be at least 6 characters long."
      );
      return false;
    }

    if (!/[A-Z]/.test(formData.password)) {
      setError(
        "Password must contain at least one uppercase letter."
      );
      return false;
    }

    if (!/[a-z]/.test(formData.password)) {
      setError(
        "Password must contain at least one lowercase letter."
      );
      return false;
    }

    if (!/[^A-Za-z0-9]/.test(formData.password)) {
      setError(
        "Password must contain at least one special character."
      );
      return false;
    }


    // -------------------------
    // PINCODE VALIDATION
    // -------------------------

    const pincodeRegex = /^[0-9]{6}$/;

    if (!pincodeRegex.test(formData.pincode)) {
      setError(
        "Pincode must contain exactly 6 digits."
      );
      return false;
    }


    return true;
  };


  // =====================================
  // SUBMIT
  // =====================================

  const handleSubmit = async (
    event: React.FormEvent<HTMLFormElement>
  ) => {
    event.preventDefault();

    setError("");
    setSuccess("");


    // Check validation first
    if (!validateForm()) {
      return;
    }


    setLoading(true);

    try {

      await axios.post(
        "http://localhost:8080/users/register",
        formData
      );


      setSuccess(
        "Account created successfully! Redirecting to login..."
      );


      setTimeout(() => {
        navigate("/login");
      }, 1200);

    } catch (err) {

      console.error(
        "Registration error:",
        err
      );


      if (axios.isAxiosError(err)) {

        setError(
          err.response?.data?.message ||
          err.response?.data?.error ||
          err.message ||
          "Registration failed. Please try again."
        );

      } else {

        setError(
          "Something went wrong. Please try again."
        );
      }

    } finally {

      setLoading(false);
    }
  };


  return (

    <main className="auth-page">

      <div className="auth-card register-card">

        <div className="auth-brand">

          <h1>
            RentIt
          </h1>

          <p>
            Rent. Share. Save.
          </p>

        </div>


        <div className="auth-heading">

          <h2>
            Create Account
          </h2>

          <p>
            Join RentIt and start renting or earning
            from your resources.
          </p>

        </div>


        {/* ERROR MESSAGE */}

        {error && (

          <div className="auth-message error-message">

            {error}

          </div>

        )}


        {/* SUCCESS MESSAGE */}

        {success && (

          <div className="auth-message success-message">

            {success}

          </div>

        )}


        <form
          className="auth-form"
          onSubmit={handleSubmit}
        >


          {/* FULL NAME */}

          <div className="form-group">

            <label htmlFor="fullName">
              Full Name
            </label>

            <input
              id="fullName"
              type="text"
              name="fullName"
              placeholder="Enter your full name"
              value={formData.fullName}
              onChange={handleChange}
              required
            />

          </div>


          <div className="form-row">


            {/* EMAIL */}

            <div className="form-group">

              <label htmlFor="email">
                Email Address
              </label>

              <input
                id="email"
                type="email"
                name="email"
                placeholder="Enter your email"
                value={formData.email}
                onChange={handleChange}
                required
              />

            </div>


            {/* PHONE */}

            <div className="form-group">

              <label htmlFor="phone">
                Phone Number
              </label>

              <input
                id="phone"
                type="tel"
                name="phone"
                placeholder="10 digit phone number"
                value={formData.phone}
                onChange={handleChange}
                pattern="[6-9][0-9]{9}"
                maxLength={10}
                required
              />

            </div>

          </div>


          {/* PASSWORD */}

          <div className="form-group">

            <label htmlFor="password">
              Password
            </label>

            <input
              id="password"
              type="password"
              name="password"
              placeholder="Create a strong password"
              value={formData.password}
              onChange={handleChange}
              autoComplete="new-password"
              required
            />

            <small className="password-hint">
              Minimum 6 characters, including uppercase,
              lowercase and a special character.
            </small>

          </div>


          {/* ADDRESS */}

          <div className="form-group">

            <label htmlFor="address">
              Address
            </label>

            <input
              id="address"
              type="text"
              name="address"
              placeholder="Enter your address"
              value={formData.address}
              onChange={handleChange}
            />

          </div>


          <div className="form-row">


            {/* CITY */}

            <div className="form-group">

              <label htmlFor="city">
                City
              </label>

              <input
                id="city"
                type="text"
                name="city"
                placeholder="City"
                value={formData.city}
                onChange={handleChange}
              />

            </div>


            {/* STATE */}

            <div className="form-group">

              <label htmlFor="state">
                State
              </label>

              <input
                id="state"
                type="text"
                name="state"
                placeholder="State"
                value={formData.state}
                onChange={handleChange}
              />

            </div>

          </div>


          {/* PINCODE */}

          <div className="form-group">

            <label htmlFor="pincode">
              Pincode
            </label>

            <input
              id="pincode"
              type="text"
              name="pincode"
              placeholder="6 digit pincode"
              value={formData.pincode}
              onChange={handleChange}
              inputMode="numeric"
              pattern="[0-9]{6}"
              minLength={6}
              maxLength={6}
              required
            />

          </div>


          {/* SUBMIT */}

          <button
            className="auth-button"
            type="submit"
            disabled={loading}
          >

            {loading
              ? "Creating Account..."
              : "Create Account"}

          </button>

        </form>


        <div className="auth-footer">

          <p>

            Already have an account?{" "}

            <Link to="/login">
              Login
            </Link>

          </p>

        </div>

      </div>

    </main>
  );
}

export default Register;