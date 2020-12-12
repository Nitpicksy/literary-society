import axios from "../../../axios-endpoint";
import { toastr } from "react-redux-toastr";
import * as actionTypes from "./ActivateAccountActionTypes";

export const activateAccountStart = () => {
  return {
    type: actionTypes.ACTIVATE_ACCOUNT_START,
  };
};

export const activateAccountSuccess = () => {
  return {
    type: actionTypes.ACTIVATE_ACCOUNT_SUCCESS,
  };
};

export const activateAccountFail = (error) => {
  return {
    type: actionTypes.ACTIVATE_ACCOUNT_FAIL,
    error: error,
  };
};

export const activateAccount = (hashId,taskId) => {
  return (dispatch) => {
    axios
      .put(`/auth/activate?taskId=${taskId}&t=${hashId}`, {})
      .then((response) => {
        toastr.success("Activation successful");
        dispatch(activateAccountSuccess());
      })
      .catch((error) => {
        toastr.error("Activation failed", error.response.data.message);
        dispatch(activateAccountFail(error.response.data.message));
      });
  };
};
