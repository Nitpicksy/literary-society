import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import { searchSuccess } from '../SearchResults/SearchResultsExport';

export const advancedSearch = (queryParams, pageNum, pageSize) => {
    const query = {
        queryParams: queryParams,
        pageNum: pageNum,
        pageSize: pageSize
    };

    return dispatch => {
        axios.post('/search', query)
            .then(response => {
                dispatch(searchSuccess(response.data.content));
            })
            .catch(err => {
                toastr.error('Search books', 'Something went wrong. Please try again.');
            });
    };
};