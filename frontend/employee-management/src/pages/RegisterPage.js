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
  IconButton,
  InputAdornment,
  Link,
} from "@mui/material";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import { useNavigate } from "react-router-dom";
import { createUser } from "../api/EmployeeApi";

// Password validation schema
const schema = yup.object().shape({
  username: yup
    .string()
    .min(2, "Name must be at least 2 characters")
    .max(100, "Name must be less than 100 characters")
    .required("User Name is required"),
  password: yup
    .string()
    .min(6, "Password must be at least 6 characters")
    .matches(/[A-Z]/, "Must include at least one uppercase letter")
    .matches(/[a-z]/, "Must include at least one lowercase letter")
    .matches(/[0-9]/, "Must include at least one number")
    .matches(/[\W_]/, "Must include at least one special character")
    .required("Password is required"),
  confirmPassword: yup
    .string()
    .oneOf([yup.ref("password"), null], "Passwords must match")
    .required("Confirm Password is required"),
});

const RegisterPage = () => {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [passwordStrength, setPasswordStrength] = useState("");

  const {
    register,
    handleSubmit,
    formState: { errors },
    watch,
  } = useForm({
    resolver: yupResolver(schema),
  });

  const registerUser = async (data) => {
    try {
      const response = await createUser(data);
      console.log(response);
      navigate("/");
    } catch (error) {
      alert(error);
    }
  };

  const handleTogglePassword = () => {
    setShowPassword((prev) => !prev);
  };

  const handleToggleConfirmPassword = () => {
    setShowConfirmPassword((prev) => !prev);
  };

  // Check password strength dynamically
  const password = watch("password");
  React.useEffect(() => {
    if (!password) {
      setPasswordStrength("");
    } else if (password.length < 6) {
      setPasswordStrength("Weak");
    } else if (
      password.match(/[A-Z]/) &&
      password.match(/[0-9]/) &&
      password.match(/[\W_]/)
    ) {
      setPasswordStrength("Strong");
    } else {
      setPasswordStrength("Medium");
    }
  }, [password]);

  return (
    <Container maxWidth="xs">
      <Box
        sx={{ mt: 8, p: 3, boxShadow: 3, borderRadius: 2, textAlign: "center" }}
      >
        <Typography variant="h5" gutterBottom>
          Register
        </Typography>
        <form onSubmit={handleSubmit(registerUser)}>
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
          <Typography
            variant="body2"
            sx={{
              textAlign: "left",
              color:
                passwordStrength === "Strong"
                  ? "green"
                  : passwordStrength === "Medium"
                  ? "orange"
                  : "red",
            }}
          >
            {passwordStrength && `Password Strength: ${passwordStrength}`}
          </Typography>
          <TextField
            fullWidth
            type={showConfirmPassword ? "text" : "password"}
            label="Confirm Password"
            margin="normal"
            {...register("confirmPassword")}
            error={!!errors.confirmPassword}
            helperText={errors.confirmPassword?.message}
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton onClick={handleToggleConfirmPassword} edge="end">
                    {showConfirmPassword ? <Visibility /> : <VisibilityOff />}
                  </IconButton>
                </InputAdornment>
              ),
            }}
          />
          <Button fullWidth type="submit" variant="contained" sx={{ mt: 2 }}>
            Register
          </Button>
        </form>
        <Typography variant="body2" sx={{ mt: 2 }}>
          Already have an account?{" "}
          <Button
            onClick={() => navigate("/")}
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
            Login
          </Button>
        </Typography>
      </Box>
    </Container>
  );
};

export default RegisterPage;
