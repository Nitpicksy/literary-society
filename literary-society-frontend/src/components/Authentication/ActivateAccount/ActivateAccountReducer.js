import * as actionTypes from "./ActivateAccountActionTypes";

const initialState = {
  error: null,
  loading: true,
};

const reducer = (state = initialState, action) => {
  switch (action.type) {
    case actionTypes.ACTIVATE_ACCOUNT_START:
      return {
        ...state,
        error: null,
        loading: false,
      };
    case actionTypes.ACTIVATE_ACCOUNT_SUCCESS:
      return {
        ...state,
        error: null,
        loading: false,
      };
    case actionTypes.ACTIVATE_ACCOUNT_FAIL:
      return {
        ...state,
        error: action.error,
        loading: false,
      };
    default:
      return state;
  }
};

export default reducer;
