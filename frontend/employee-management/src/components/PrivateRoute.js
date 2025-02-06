import { Navigate, Outlet } from "react-router-dom";
import Navbar from "./Navbar";
import Cookies from "js-cookie";
import { useEffect, useState } from "react";
import { authApi } from "../api/EmployeeApi";

const PrivateRoute = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const checkAuth = async () => {
      try {
        const response = await authApi();
        console.log(response);
        
        if (response) {
          setIsAuthenticated(true);
        } else {
          setIsAuthenticated(false);
        }
      } catch (error) {
        setIsAuthenticated(false);
      } finally {
        setIsLoading(false);
      }
    };

    checkAuth();
  }, []);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return isAuthenticated ? (
    <>
      <Navbar />
      <Outlet />
    </>
  ) : (
    <Navigate to="/" />
  );
};

export default PrivateRoute;
