import * as actionTypes from './PaperResultDetailsActionTypes';

const initialState = {
    paperResultDetails: null,
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_PAPER_RESULT_DETAILS_SUCCESS:
            return {
                ...state,
                paperResultDetails: action.paperResultDetails
            };
        default:
            return state;
    }
};

export default reducer;