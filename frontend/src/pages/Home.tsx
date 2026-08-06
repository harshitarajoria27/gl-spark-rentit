import { Link } from "react-router-dom";
import "./Home.css";

function Home() {

  return (
    <main className="home">

      {/* HERO */}

      <section className="hero">

        <div className="hero-content">

          <div className="hero-badge">
            Simple • Affordable • Sustainable
          </div>

          <h1>
            Why buy it when
            <span> you can rent it?</span>
          </h1>

          <p>
            RentIt connects people who need things
            with people who already own them.
            Rent what you need, when you need it.
          </p>

          <div className="hero-buttons">

            <Link
              to="/resources"
              className="primary-button"
            >
              Browse Resources
            </Link>

            <Link
              to="/register"
              className="secondary-button"
            >
              Start Renting
            </Link>

          </div>

        </div>

        <div className="hero-visual">

          <div className="visual-card">

            <div className="visual-icon">
              🏕️
            </div>

            <h3>Camping Tent</h3>

            <p>Available in your city</p>

            <div className="visual-price">
              ₹500
              <span>/ day</span>
            </div>

          </div>

        </div>

      </section>


      {/* HOW IT WORKS */}

      <section className="how-section">

        <div className="section-heading">

          <span>HOW IT WORKS</span>

          <h2>
            Renting made simple
          </h2>

          <p>
            Find what you need and rent it in just
            a few simple steps.
          </p>

        </div>

        <div className="steps-container">

          <div className="step-card">

            <div className="step-number">
              01
            </div>

            <h3>Find a Resource</h3>

            <p>
              Browse resources available for rent
              near you.
            </p>

          </div>


          <div className="step-card">

            <div className="step-number">
              02
            </div>

            <h3>Book It</h3>

            <p>
              Select your rental duration and send
              a booking request.
            </p>

          </div>


          <div className="step-card">

            <div className="step-number">
              03
            </div>

            <h3>Use & Return</h3>

            <p>
              Use the resource and return it safely
              when you're finished.
            </p>

          </div>

        </div>

      </section>


      {/* OWNER CTA */}

      <section className="owner-section">

        <div>

          <span className="owner-label">
            HAVE SOMETHING TO RENT?
          </span>

          <h2>
            Turn unused items into earnings.
          </h2>

          <p>
            List your resources on RentIt and let
            others make use of what you already own.
          </p>

        </div>

        <Link
          to="/resources/add"
          className="owner-button"
        >
          List a Resource →
        </Link>

      </section>

    </main>
  );
}

export default Home;