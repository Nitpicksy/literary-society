import instance from './axios-endpoint';

export default {
    setupInterceptor: (history, refreshTokenRequestSent, onRefreshToken) => {
        console.log("Moj interceptor")
        instance.interceptors.response.use((response) => {
            return response
        }, error => {
            const request = error.config;
            const refreshToken = localStorage.getItem("refreshToken");
            if (error.response.status === 401) {

                if (request.url.includes("refresh") || request.url.includes("sign-in")) {
                    localStorage.removeItem('accessToken');
                    localStorage.removeItem('expiresIn');
                    localStorage.removeItem('refreshToken');
                    history.push('/error/non-authenticated');
                    return Promise.reject(error)
                }
                if (refreshToken && !refreshTokenRequestSent) {
                    onRefreshToken(history);
                    return Promise.resolve();
                } else {
                    localStorage.removeItem('accessToken');
                    localStorage.removeItem('expiresIn');
                    localStorage.removeItem('refreshToken');
                    history.push('/error/non-authenticated')
                }

            } else if (error.response.status === 403) {
                history.push('/error/non-authorized')
            }


            return Promise.reject(error)
        });

    }

}
