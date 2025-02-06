import axios from "axios";

const API_BASE_URL = "http://localhost:8080";

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
});

axiosInstance.interceptors.request.use((config) => {
  return config;
});

axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    const originalRequest = error.config;
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      localStorage.removeItem("token");
      window.location.href = "/login";
      return Promise.reject(error);
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
