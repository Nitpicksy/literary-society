import axios from "axios";

export const instance = axios.create({
  // baseURL: "https://localhost:8080/payment-gateway/api",
  baseURL: "https://localhost:63266/api",
  orders: "/orders",
  payments: "/payments",
});

export default instance;
