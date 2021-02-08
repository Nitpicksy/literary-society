import axios from "axios";

export const instance = axios.create({
  // baseURL: "https://localhost:8090/api",
  baseURL: "https://192.168.43.199:8090/api",
  signIn: "/auth/sign-in",
});

instance.interceptors.request.use(
  async (request) => {
    if (!request.url.includes("/auth")) {
      const accessToken = localStorage.getItem("accessToken");
      request.headers = {
        Authorization: `Bearer ${accessToken}`,
      };
    } else {
      const refreshToken = localStorage.getItem("refreshToken");
      if (refreshToken) {
        if (request.url.includes("refresh")) {
          request.headers = {
            Authorization: `Bearer ${refreshToken}`,
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
