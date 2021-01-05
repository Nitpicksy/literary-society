import * as actionTypes from './ClientAccountListActionTypes';

const initialState = {
    clients: null,
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_CLIENTS_SUCCESS:
            return {
                ...state,
                clients: action.clients,
            };
        case actionTypes.FETCH_CLIENTS_FAIL:
            return {
                ...state,
                clients: null,
            };
        default:
            return state;
    }
};

export default reducer;