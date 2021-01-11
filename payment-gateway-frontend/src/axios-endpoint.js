import axios from "axios";

export const instance = axios.create({
<<<<<<< HEAD
  baseURL: "https://localhost:44019/api",
=======
  // baseURL: "https://localhost:8080/payment-gateway/api",
  baseURL: "https://localhost:53971/api",
>>>>>>> origin/dev
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
