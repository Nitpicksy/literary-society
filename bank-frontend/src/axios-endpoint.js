import axios from "axios";

export const instance = axios.create({
    baseURL: 'https://localhost:8100/api',
    confirmPayment: '/payments/confirm'
});

export default instance;
