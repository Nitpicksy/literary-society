import instance from './axios-endpoint';

export default {
    setupInterceptor: (history) => {

        instance.interceptors.response.use((response) => {
            return response
        }, error => {
            let isRefreshingToken = false;
            const request = error.config;
            const refreshToken = localStorage.getItem("refreshToken");

            if (error.response.status === 401) {
               
                if (request.url.includes("refresh") || request.url.includes("sign-in")) {
                    localStorage.removeItem('accessToken');
                    localStorage.removeItem('expiresIn');
                    localStorage.removeItem('refreshToken');
                    history.push('/error/non-authenticated')
                    return Promise.reject(error)
                }

                if (refreshToken && !isRefreshingToken) {
                    // console.log("refresh")
                    // isRefreshingToken = true;
                    // this.tokenSubject.next(null);

                    // this.authentificationService.refreshAccessToken().toPromise().then(
                    //     (res: UserTokenState) => {
                    //         this.authentificationService.access_token = null;
                    //         localStorage.removeItem('UserTokenState');
                    //         this.authentificationService.setNewAccessToken(res);
                    //         this.tokenSubject.next(res.accessToken);
                    //         this.isRefreshingToken = false;
                    //         return of(true);
                    //     },
                    //     () => {
                    //         this.authentificationService.clearLocalStorage();
                    //         this.isRefreshingToken = false;
                    //         this.router.navigate(['/error/non-authenticated']);
                    //     }
                    // );
                    // return of(true);
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
