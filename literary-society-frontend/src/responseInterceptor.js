import instance from './axios-endpoint';

export const responseInterceptor = {
    setupInterceptor: (history, refreshTokenRequestSent, onRefreshToken) => {

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
                    console.log(history)
                    return Promise.reject(error)
                }
                if (refreshToken && !refreshTokenRequestSent) {
                    console.log("refresh token")
                    onRefreshToken(history);
                    return Promise.resolve();
                } else {
                    console.log("dssdsddsdddddd")
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
