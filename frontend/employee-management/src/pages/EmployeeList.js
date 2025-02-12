import React, { useEffect, useState } from "react";
import { deleteEmployeeByEmail, getAllEmployees } from "../api/EmployeeApi";
import { Link } from "react-router-dom";
import { Container, Button, Typography, Box } from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import FileDownloadIcon from "@mui/icons-material/FileDownload";
import BorderColorIcon from "@mui/icons-material/BorderColor";
import DeleteIcon from "@mui/icons-material/Delete";
import PersonAddIcon from "@mui/icons-material/PersonAdd";

function EmployeeList() {
  const [employees, setEmployees] = useState([]);

  useEffect(() => {
    fetchEmployees();
  }, []);

  const fetchEmployees = async () => {
    try {
      const response = await getAllEmployees();
      setEmployees(response);
    } catch (error) {
      alert(error);
    }
  };

  const deleteEmployee = async (email) => {
    try {
      await deleteEmployeeByEmail(email);
      fetchEmployees();
    } catch (error) {
      alert(error);
    }
  };

  const handleOpenFile = (fileUrl) => {
    window.open(fileUrl, "_self"); // Opens the file in a new tab
  };

  const columns = [
    { field: "name", headerName: "Name", flex: 1 },
    { field: "email", headerName: "Email", flex: 1 },
    { field: "phoneNumber", headerName: "Phone Number", flex: 1 },
    { field: "department", headerName: "Department", flex: 1 },
    { field: "gender", headerName: "Gender", flex: 1 },
    {
      field: "file",
      headerName: "File",
      flex: 1,
      renderCell: (params) => (
        <Container>
          <Button
            // variant="contained"
            color="info"
            size="small"
            onClick={() => handleOpenFile(params.row.fileLocation)}
          >
            <FileDownloadIcon />
          </Button>
        </Container>
      ),
    },
    {
      field: "actions",
      headerName: "Actions",
      flex: 1,
      renderCell: (params) => (
        <Box flex={1} justifyContent={"center"}>
          <Button
            component={Link}
            to={`/manage-employee/${params.row.email}`}
            // variant="contained"
            color="warning"
            size="small"
            style={{ marginRight: 10 }}
          >
            <BorderColorIcon />
          </Button>
          <Button
            // variant="contained"
            color="error"
            size="small"
            onClick={() => deleteEmployee(params.row.email)}
          >
            <DeleteIcon />
          </Button>
        </Box>
      ),
    },
  ];

  return (
    <Container maxWidth="lg" sx={{ mt: 5 }}>
      <Box sx={{ display: "flex", justifyContent: "space-between" }}>
        <Typography variant="h4" gutterBottom>
          Employee List
        </Typography>
        <Box>
          <Button
            component={Link}
            to="/manage-employee/new"
            variant="contained"
            color="success"
          >
            <PersonAddIcon />
          </Button>
        </Box>
      </Box>
      <Box sx={{ height: 400, width: "100%" }}>
        <DataGrid
          rows={employees}
          columns={columns}
          getRowId={(row) => row.email}
          pageSize={5}
        />
      </Box>
    </Container>
  );
}

export default EmployeeList;
