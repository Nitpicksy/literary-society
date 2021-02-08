import axios from "axios";

export const instance = axios.create({
    baseURL: 'https://192.168.43.3:8100/api',
    confirmPayment: '/payments/confirm'
});

export default instance;
