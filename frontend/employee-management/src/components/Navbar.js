import React from "react";
import { AppBar, Toolbar, Typography, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import axios from "axios";
import { logoutUser } from "../api/EmployeeApi";

const Navbar = () => {
  const navigate = useNavigate();

  const handleLogout = async() => {
    try {
      const response = await logoutUser();
      console.log(response);
      navigate("/")
    } catch (error) {
      alert(error);
    };
  };

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" sx={{ flexGrow: 1 }}>
          Employee Management
        </Typography>
        <Button color="inherit" onClick={() => navigate("/employee-list")}>
          Employee List
        </Button>
        <Button color="inherit" onClick={handleLogout}>
          Logout
        </Button>
      </Toolbar>
    </AppBar>
  );
};

export default Navbar;
