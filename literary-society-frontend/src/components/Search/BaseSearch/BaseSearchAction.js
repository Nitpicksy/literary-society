import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import { searchSuccess } from '../SearchResults/SearchResultsExport';

export const baseSearch = (history, searchText, pageNum, pageSize) => {
    const query = {
        searchAllParam: searchText,
        pageNum: pageNum,
        pageSize: pageSize
    };

    return dispatch => {
        axios.post('/search', query)
            .then(response => {
                dispatch(searchSuccess(response.data.content));
                if (!history.location.pathname.startsWith('/search-results')) {
                    history.push(`/search-results?q=${searchText}&psize=${pageSize}`);
                }
            })
            .catch(err => {
                toastr.error('Search books', 'Something went wrong. Please try again.');
            });
    };
};