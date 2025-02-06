import React, { useEffect, useState } from "react";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import { useNavigate, useParams } from "react-router-dom";
import {
  createEmployee,
  getEmployees,
  updateEmployee,
} from "../api/EmployeeApi";
import {
  TextField,
  Button,
  FormControl,
  FormLabel,
  RadioGroup,
  FormControlLabel,
  Radio,
  Container,
  Typography,
  Box,
  Checkbox,
  Autocomplete,
} from "@mui/material";

const getValidationSchema = (isNewEmployee) => {
  return yup.object({
    name: yup
      .string()
      .min(2, "Name must be at least 2 characters")
      .max(100, "Name must be less than 100 characters")
      .required("Name is required"),
    email: yup
      .string()
      .email("Invalid email format")
      .required("Email is required"),
    phoneNumber: yup
      .string()
      .matches(/^[0-9]{10}$/, "Phone number must be 10 digits")
      .required("Phone number is required"),
    gender: yup
      .string()
      .oneOf(["MALE", "FEMALE"], "Invalid gender")
      .required("Gender is required"),
    department: yup
      .string()
      .oneOf(["HR", "IT", "FINANCE", "MARKETING"], "Invalid department")
      .required("Department is required"),
    file: isNewEmployee
      ? yup
          .mixed()
          .required("File is required")
          .test(
            "fileSize",
            "File is too large",
            (value) => value && value[0]?.size <= 5 * 1024 * 1024
          )
          .test(
            "fileFormat",
            "Unsupported file format",
            (value) =>
              value &&
              ["image/jpeg", "image/png", "application/pdf"].includes(
                value[0]?.type
              )
          )
      : yup.mixed().notRequired(),
    healthInsurance: yup.boolean(),
    newsletter: yup.boolean(),
    workFromHome: yup.boolean(),
  });
};

function ManageEmployee() {
  const navigate = useNavigate();
  const email = useParams().email;
  const [file, setFile] = useState(null);

  const {
    register,
    handleSubmit,
    setValue,
    control,
    formState: { errors },
  } = useMUI(
    useForm({
      resolver: yupResolver(getValidationSchema(email === "new")),
    })
  );

  function useMUI(useFormObject) {
    const register = (name, options) => ({
      ...useFormObject.register(name, options),
      InputLabelProps: { shrink: !!useFormObject.watch(name) },
    });
    return {
      ...useFormObject,
      register,
    };
  }

  const onSubmit = async (data) => {
    try {
      const employeeData = {
        name: data.name,
        email: data.email,
        phoneNumber: data.phoneNumber,
        gender: data.gender,
        department: data.department,
        fileLocation: "tempfileloc",
        healthInsurance: data.healthInsurance,
        newsletter: data.newsletter,
        workFromHome: data.workFromHome,
      };

      if (email === "new") {
        await createEmployee(employeeData, file);
      } else {
        employeeData.fileLocation = data.fileLocation;
        await updateEmployee(email, employeeData);
      }
      navigate("/employee-list");
    } catch (error) {
      alert(error);
    }
  };

  useEffect(() => {
    if (email !== "new") {
      fetchEmployee(email);
    }
  }, [email]);

  const fetchEmployee = async (email) => {
    try {
      const response = await getEmployees(email);
      console.log(response.department);
      setValue("name", response.name);
      setValue("email", response.email);
      setValue("phoneNumber", response.phoneNumber);
      setValue("gender", response.gender);
      setValue("department", response.department);
      setValue("fileLocation", response.fileLocation);
      setValue("healthInsurance", response.healthInsurance || false);
      setValue("newsletter", response.newsletter || false);
      setValue("workFromHome", response.workFromHome || false);
    } catch (error) {
      alert(error);
    }
  };

  return (
    <Container maxWidth="sm" sx={{marginBottom:7}}>
      <Typography variant="h4" sx={{ mt: 5, mb: 3 }}>
        {email === "new" ? "Add Employee" : "Edit Employee"}
      </Typography>
      <form onSubmit={handleSubmit(onSubmit)}>
        <TextField
          fullWidth
          label="Name"
          margin="normal"
          error={!!errors.name}
          helperText={errors.name?.message}
          {...register("name")}
        />
        <TextField
          fullWidth
          label="Email"
          margin="normal"
          error={!!errors.email}
          helperText={errors.email?.message}
          {...register("email")}
        />
        <TextField
          fullWidth
          label="Phone Number"
          margin="normal"
          error={!!errors.phoneNumber}
          helperText={errors.phoneNumber?.message}
          {...register("phoneNumber")}
        />

        <FormControl component="fieldset" margin="normal">
          <FormLabel component="legend">Gender</FormLabel>
          <Controller
            name="gender"
            control={control}
            defaultValue=""
            render={({ field }) => (
              <RadioGroup row {...field}>
                <FormControlLabel
                  value="MALE"
                  control={<Radio />}
                  label="Male"
                />
                <FormControlLabel
                  value="FEMALE"
                  control={<Radio />}
                  label="Female"
                />
              </RadioGroup>
            )}
          />
        </FormControl>
        {errors.gender && (
          <Typography color="error">{errors.gender.message}</Typography>
        )}

        <FormControl fullWidth margin="normal">
          <Controller
            name="department"
            control={control}
            defaultValue=""
            render={({ field }) => (
              <Autocomplete
                {...field}
                options={["HR", "IT", "FINANCE", "MARKETING"]}
                freeSolo
                onChange={(_, value) => field.onChange(value)}
                renderInput={(params) => (
                  <TextField {...params} label="Department" />
                )}
              />
            )}
          />
        </FormControl>
        {errors.department && (
          <Typography color="error">{errors.department.message}</Typography>
        )}

        {email === "new" && (
          <Controller
            name="file"
            control={control}
            defaultValue={null}
            render={({ field }) => (
              <TextField
                fullWidth
                type="file"
                margin="normal"
                inputProps={{
                  accept: "image/jpeg, image/png, application/pdf",
                }}
                onChange={(e) => {
                  setFile(e.target.files[0]);
                  field.onChange(e.target.files);
                }}
              />
            )}
          />
        )}
        {errors.file && (
          <Typography color="error">{errors.file.message}</Typography>
        )}

        <FormControl component="fieldset" margin="normal">
          <FormLabel component="legend">Additional Options</FormLabel>
          <Box>
            <FormControlLabel
              control={
                <Controller
                  name="healthInsurance"
                  control={control}
                  defaultValue={false}
                  render={({ field }) => (
                    <Checkbox
                      {...field}
                      checked={field.value}
                      onChange={(e) => field.onChange(e.target.checked)}
                    />
                  )}
                />
              }
              label="Health Insurance"
            />
            <FormControlLabel
              control={
                <Controller
                  name="newsletter"
                  control={control}
                  defaultValue={false}
                  render={({ field }) => (
                    <Checkbox
                      {...field}
                      checked={field.value}
                      onChange={(e) => field.onChange(e.target.checked)}
                    />
                  )}
                />
              }
              label="Newsletter"
            />
            <FormControlLabel
              control={
                <Controller
                  name="workFromHome"
                  control={control}
                  defaultValue={false}
                  render={({ field }) => (
                    <Checkbox
                      {...field}
                      checked={field.value}
                      onChange={(e) => field.onChange(e.target.checked)}
                    />
                  )}
                />
              }
              label="Work from Home"
            />
          </Box>
        </FormControl>

        <Box mt={3} display="flex" justifyContent="space-between">
          <Button type="submit" variant="contained" color="primary">
            {email === "new" ? "Add Employee" : "Update Employee"}
          </Button>
          <Button
            variant="outlined"
            color="warning"
            onClick={() => navigate("/employee-list")}
          >
            Cancel
          </Button>
        </Box>
      </form>
    </Container>
  );
}

export default ManageEmployee;
