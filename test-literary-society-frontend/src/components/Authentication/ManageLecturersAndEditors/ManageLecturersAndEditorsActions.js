import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './ManageLecturersAndEditorsActionTypes';

export const fetchUsersSuccess = (users) => {
    return {
        type: actionTypes.FETCH_USERS_SUCCESS,
        users: users
    };
};

export const fetchUsersFail = () => {
    return {
        type: actionTypes.FETCH_USERS_FAIL
    };
};

export const fetchUsers = () => {
    return dispatch => {
        axios.get('/users')
            .then(response => {
                dispatch(fetchUsersSuccess(response.data));                
            })
            .catch(() => {
                dispatch(fetchUsersFail());
                toastr.error('Users', 'Something went wrong. Please try again.');
            });
    };
};


export const changeUserStatus = (id, status) => {
    return dispatch => {
        axios.put(`/users/${id}?status=${status}`)
            .then(() => {
                dispatch(fetchUsers())
                toastr.success('Users', 'Successfully ' + status + "ed user's request.");
            })
            .catch(() => {
                toastr.error('Users', 'Something went wrong. Please try again.');
            });
    }
};