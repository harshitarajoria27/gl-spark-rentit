import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import "./Auth.css";

interface LoginFormData {
  email: string;
  password: string;
}

function Login() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState<LoginFormData>({
    email: "",
    password: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { name, value } = event.target;

    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (
    event: React.FormEvent<HTMLFormElement>
  ) => {
    event.preventDefault();

    setError("");
    setLoading(true);

    try {
      const response = await axios.post(
        "http://localhost:8080/users/login",
        formData
      );

      console.log("Login response:", response.data);

      /*
       * We'll adjust this after checking your exact LoginResponse DTO.
       * This assumes the backend returns:
       *
       * {
       *    "token": "eyJ..."
       * }
       */

      const token = response.data.token;

      if (!token) {
        setError("Token was not received from server.");
        return;
      }

      localStorage.setItem("token", token);

      navigate("/dashboard");

    } catch (err) {
      console.error("Login error:", err);

      if (axios.isAxiosError(err)) {
        setError(
          err.response?.data?.message ||
          err.response?.data?.error ||
          "Invalid email or password."
        );
      } else {
        setError("Something went wrong. Please try again.");
      }

    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="auth-page">

      <div className="auth-card">

        <div className="auth-brand">
          <h1>RentIt</h1>
          <p>Welcome back!</p>
        </div>

        <div className="auth-heading">
          <h2>Login</h2>

          <p>
            Login to manage your rentals and resources.
          </p>
        </div>

        {error && (
          <div className="auth-message error-message">
            {error}
          </div>
        )}

        <form
          className="auth-form"
          onSubmit={handleSubmit}
        >

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
              autoComplete="email"
              required
            />

          </div>

          <div className="form-group">

            <label htmlFor="password">
              Password
            </label>

            <input
              id="password"
              type="password"
              name="password"
              placeholder="Enter your password"
              value={formData.password}
              onChange={handleChange}
              autoComplete="current-password"
              required
            />

          </div>

          <button
            className="auth-button"
            type="submit"
            disabled={loading}
          >
            {loading ? "Logging in..." : "Login"}
          </button>

        </form>

        <div className="auth-footer">

          <p>
            Don't have an account?{" "}
            <Link to="/register">
              Create Account
            </Link>
          </p>

        </div>

      </div>

    </main>
  );
}

export default Login;