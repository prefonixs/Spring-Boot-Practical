import axiosInstance from "./axiosInstance";

export const createEmployee = async (employeeData, file) => {
  console.log("create");
  try {
    const formData = new FormData();
    formData.append("file", file);
    formData.append(
      "employeeDTO",
      new Blob([JSON.stringify(employeeData)], {
        type: "application/json",
      })
    );
    const response = await axiosInstance.post(`/create-employees`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || "Something went wrong!";
  }
};

export const updateEmployee = async (email, employeeData) => {
  console.log("update");
  try {
    const response = await axiosInstance.patch(
      `/update-employees/${email}`,
      employeeData
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || "Something went wrong!";
  }
};

export const getAllEmployees = async () => {
  console.log("getall");
  try {
    const response = await axiosInstance.get(`/get-employees`);
    return response.data;
  } catch (error) {
    throw error.response?.data || "Error fetching employees";
  }
};

export const getEmployees = async (email) => {
  console.log("getemail");

  try {
    const response = await axiosInstance.get(`/get-employees/${email}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || "Error fetching employee";
  }
};

export const downloadFile = async (fileLocation) => {
  console.log("download file");

  try {
    const response = await axiosInstance.get(`/files/${fileLocation}`);
    return response.data;
  } catch (error) {
    // throw error.response?.data || "Error fetching employee";
    throw error
  }
};

export const deleteEmployeeByEmail = async (email) => {
  console.log("delete");

  try {
    await axiosInstance.delete(`/delete-employees/${email}`);
  } catch (error) {
    throw error.response?.data || "Error deleteing employees";
  }
};

export const createUser = async (user) => {
  console.log("register");

  try {
    const response = await axiosInstance.post(`/register`, user);
    return response.data;
  } catch (error) {
    throw error.response?.data || "Something went wrong!";
  }
};

export const loginUser = async (user) => {
  console.log("login");

  try {
    const response = await axiosInstance.post(`/login`, user);
    return response;
  } catch (error) {
    throw error.response?.data || "Something went wrong!";
  }
};

export const logoutUser = async () => {
  console.log("logout");

  try {
    const response = await axiosInstance.post(`/logout`);
    return response;
  } catch (error) {
    console.log(error);
    
    throw error.response?.data || "Something went wrong!";
  }
};

export const authApi = async () => {
  console.log("authApi");

  try {
    const response = await axiosInstance.get(`/auth`);
    return response;
  } catch (error) {
    throw error.response?.data || "Error fetching employee";
  }
};