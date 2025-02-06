import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import ManageEmployee from "./pages/ManageEmployee";
import EmployeeList from "./pages/EmployeeList";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import PrivateRoute from "./components/PrivateRoute";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route element={<PrivateRoute />}>
          <Route path="/employee-list" element={<EmployeeList />} />
          <Route path="/manage-employee/:email" element={<ManageEmployee />} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
