import axios from '../../../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './PaperResultDetailsActionTypes';

export const fetchPaperResultDetailsSuccess = (paperResultDetails) => {
    return {
        type: actionTypes.FETCH_PAPER_RESULT_DETAILS_SUCCESS,
        paperResultDetails: paperResultDetails,
    };
};

export const fetchPaperResultDetails = (piId) => {
    return dispatch => {
        axios.get(`/books/paper-result?piId=${piId}`)
            .then(response => {
                dispatch(fetchPaperResultDetailsSuccess(response.data.items));
            })
            .catch(err => {
                toastr.error('Paper Result Details', 'Something went wrong. Please try again.');
            });
    };
};
