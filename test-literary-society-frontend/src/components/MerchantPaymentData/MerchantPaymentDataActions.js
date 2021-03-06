import axios from "../../axios-endpoint";

export const insertPaymentData = () => {
  return (dispatch) => {
    axios
      .get("/merchants/payment-data")
      .then((response) => {
        if (response.data) {
          window.location.href = response.data;
          // window.open(response.data, '_blank');
        }
      })
      .catch(() => {
        // toastr.error('Insert Payment Data', 'Something went wrong. Please try again.');
      });
  };
};

