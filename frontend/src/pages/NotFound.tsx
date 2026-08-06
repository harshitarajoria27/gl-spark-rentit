import { Link } from "react-router-dom";
import "./NotFound.css";

function NotFound() {
  return (
    <main className="not-found-page">

      <div className="not-found-container">

        <div className="error-code">
          404
        </div>

        <div className="error-icon">
          🔍
        </div>

        <h1>Page Not Found</h1>

        <p>
          Looks like the page you're trying to visit
          doesn't exist or may have been moved.
        </p>

        <div className="not-found-actions">

          <Link
            to="/"
            className="not-found-primary"
          >
            ← Back to Home
          </Link>

          <Link
            to="/dashboard"
            className="not-found-secondary"
          >
            Go to Dashboard
          </Link>

        </div>

      </div>

    </main>
  );
}

export default NotFound;