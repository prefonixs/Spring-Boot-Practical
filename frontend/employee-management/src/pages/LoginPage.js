import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import {
  TextField,
  Button,
  Box,
  Typography,
  Container,
  Link,
  InputAdornment,
  IconButton,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../api/EmployeeApi";
import { Visibility, VisibilityOff } from "@mui/icons-material";

// Validation schema using Yup
const schema = yup.object().shape({
  username: yup
    .string()
    .min(2, "Name must be at least 2 characters")
    .max(100, "Name must be less than 100 characters")
    .required("User Name is required"),
  password: yup
    .string()
    .min(6, "Password must be at least 6 characters")
    .required("Password is required"),
});

const LoginPage = () => {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: yupResolver(schema),
  });

  const login = async (data) => {
    try {
      const response = await loginUser(data); // Call API
      console.log(response);

      if (response.status === 200) {
        navigate("/employee-list"); // Redirect on success
      } else {
        setErrorMessage("Invalid credentials. Please try again.");
      }
    } catch (error) {
      setErrorMessage("Login failed. Please check your username and password.");
    }
  };

  const handleTogglePassword = () => {
    setShowPassword((prev) => !prev);
  };

  return (
    <Container maxWidth="xs">
      <Box
        sx={{ mt: 8, p: 3, boxShadow: 3, borderRadius: 2, textAlign: "center" }}
      >
        <Typography variant="h5" gutterBottom>
          Login
        </Typography>
        {errorMessage && (
          <Typography color="error" variant="body2">
            {errorMessage}
          </Typography>
        )}
        <form onSubmit={handleSubmit(login)}>
          <TextField
            fullWidth
            label="User Name"
            margin="normal"
            {...register("username")}
            error={!!errors.username}
            helperText={errors.username?.message}
          />
          <TextField
            fullWidth
            type={showPassword ? "text" : "password"}
            label="Password"
            margin="normal"
            {...register("password")}
            error={!!errors.password}
            helperText={errors.password?.message}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton onClick={handleTogglePassword} edge="end">
                    {showPassword ? <Visibility /> : <VisibilityOff />}
                  </IconButton>
                </InputAdornment>
              ),
            }}
          />
          <Button fullWidth type="submit" variant="contained" sx={{ mt: 2 }}>
            Login
          </Button>
        </form>
        <Typography variant="body2" sx={{ mt: 2 }}>
          Don't have an account?{" "}
          <Button
            onClick={() => navigate("/register")}
            sx={{
              textTransform: "none",
              color: "blue",
              backgroundColor: "transparent",
              padding: 0,
              minWidth: "auto",
              "&:hover": {
                textDecoration: "underline",
                backgroundColor: "transparent",
              },
            }}
          >
            Register here
          </Button>
        </Typography>
      </Box>
    </Container>
  );
};

export default LoginPage;
