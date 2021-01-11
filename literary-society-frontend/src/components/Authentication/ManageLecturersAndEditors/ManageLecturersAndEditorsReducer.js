import * as actionTypes from './ManageLecturersAndEditorsActionTypes';

const initialState = {
    users: null,
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_USERS_SUCCESS:
            return {
                ...state,
                users: action.users,
            };
        case actionTypes.FETCH_USERS_FAIL:
            return {
                ...state,
                users: null,
            };
        default:
            return state;
    }
};

export default reducer;