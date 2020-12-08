import axios from 'axios';

export const instance = axios.create({
    baseURL: 'http://localhost:8090/api',
    signIn: '/auth/sign-in'
});

instance.interceptors.request.use(
    async request => {
        if (!request.url.includes("/auth")) {
            const accessToken = localStorage.getItem("accessToken");
            request.headers = {
                Authorization: `Bearer ${accessToken}`
            }
        }else {
            const refreshToken = localStorage.getItem("refreshToken");
            if (refreshToken) {
                if (request.url.includes("refresh")) {
                    console.log("refreshToken ddfff")
                    request.headers = {
                        Authorization: `Bearer ${refreshToken}`
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
