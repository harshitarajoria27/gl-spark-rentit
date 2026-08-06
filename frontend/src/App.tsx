import {
  BrowserRouter,
  Routes,
  Route
} from "react-router-dom";
import MyTransactions from "./pages/MyTransactions";
import OwnerTransactions from "./pages/OwnerTransactions";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import NotFound from "./pages/NotFound";

import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import ProtectedRoute from "./components/ProtectedRoute";
import AddResource from "./pages/AddResource";
import MyResources from "./pages/MyResources";
import BrowseResources from "./pages/BrowseResources";
import EditResource from "./pages/EditResource";

import BorrowRequests from "./pages/BorrowRequests";
import MyBorrowRequests from "./pages/MyBorrowRequests";

function App() {
  return (
    <BrowserRouter>

      <Navbar />

      <Routes>

        <Route
          path="/"
          element={<Home />}
        />

        

        <Route
          path="/register"
          element={<Register />}
        />

        <Route
          path="/login"
          element={<Login />}
        />

        <Route
          path="/resources/add"
          element={<AddResource />}
        />
        <Route
          path="/resources/my"
          element={<MyResources />}
        />
        <Route
          path="/resources/:id/edit"
          element={<EditResource />}
        />
        <Route
          path="/resources"
          element={<BrowseResources />}
        />
        
<Route
  path="/borrow-requests"
  element={<BorrowRequests />}
/>
<Route
    path="/bookings/my"
    element={<MyBorrowRequests />}
/>

<Route
  path="/transactions/my"
  element={<MyTransactions />}
/>

<Route
  path="/transactions/owned"
  element={<OwnerTransactions />}
/>

        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="*"
          element={<NotFound />}
        />

      </Routes>

      <Footer />

    </BrowserRouter>
  );
}

export default App;