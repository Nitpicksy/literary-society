import axios from "axios";

export const instance = axios.create({
  // baseURL: "https://localhost:8080/payment-gateway/api",
  baseURL: "https://localhost:33383/api",
  orders: "/orders",
  payments: "/payments",
});

instance.interceptors.request.use(
  async (request) => {
    if (!request.url.includes("/auth")) {
      const accessToken = localStorage.getItem("accessToken");
      if (accessToken) {
        request.headers = {
          Auth: `Bearer ${accessToken}`,
        };
      }
    } else {
      const refreshToken = localStorage.getItem("refreshToken");
      if (refreshToken) {
        if (request.url.includes("refresh")) {
          request.headers = {
            Auth: `Bearer ${refreshToken}`,
          };
        }
      }
    }
    return request;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default instance;
