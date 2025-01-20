import axios from "axios";

const axiosInstance = axios.create();


axiosInstance.interceptors.response.use(
    (response) => response,  // Return the response as is
    (error) => {
        if (error.response) {
            if (error.response.status === 401 || error.response.status === 403) {
                //       window.location.href = '/login';
            }
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;  // Export the instance for use in other files
