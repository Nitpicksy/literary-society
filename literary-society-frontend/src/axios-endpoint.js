import axios from 'axios';

export const instance = axios.create({
    baseURL: 'http://localhost:8090/api',
    signIn: '/auth/sign-in'
});

instance.interceptors.request.use(
    async request => {
        if (!request.url.includes("/auth")) {
            const accessToken = localStorage.getItem("accessToken");
            const refreshToken = localStorage.getItem("refreshToken");
            if (accessToken && refreshToken) {
                if (request.url.includes("refresh")) {
                    request.headers = {
                        Authorization: `Bearer ${refreshToken}`
                    }
                } else {
                    console.log("Authorization")
                    request.headers = {
                        Authorization: `Bearer ${accessToken}`
                    }
                }
            }
        }
        return request;
    },
    error => {
        return Promise.reject(error)
    });


export default instance;
