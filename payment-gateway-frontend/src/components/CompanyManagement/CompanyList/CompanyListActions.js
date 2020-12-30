import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './CompanyListActionTypes';

export const fetchCompaniesSuccess = (companies) => {
    return {
        type: actionTypes.FETCH_COMPANIES_SUCCESS,
        companies: companies
    };
};

export const fetchCompaniesFail = () => {
    return {
        type: actionTypes.FETCH_COMPANIES_FAIL
    };
};

export const fetchCompanies = () => {
    return dispatch => {
        axios.get('/companies')
            .then(response => {
                dispatch(fetchCompaniesSuccess(response.data));                
            })
            .catch(err => {
                dispatch(fetchCompaniesFail());
                toastr.error('Companies', 'Something went wrong. Please try again.');
            });
    };
};


export const changeCompanyStatus = (id, status) => {
    return dispatch => {
        axios.put(`/companies/${id}?status=${status}`)
            .then(() => {
                dispatch(fetchCompanies())
                toastr.success('Companies', 'Successfully ' + status + "ed company's request.");
            })
            .catch(() => {
                toastr.error('Companies', 'Something went wrong. Please try again.');
            });
    }
};