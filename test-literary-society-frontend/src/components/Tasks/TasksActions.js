import axios from '../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import * as actionTypes from './TasksActionTypes';

export const fetchTasksSuccess = (tasks) => {
    return {
        type: actionTypes.FETCH_TASKS_SUCCESS,
        tasks: tasks
    };
};

export const fetchTasksFail = () => {
    return {
        type: actionTypes.FETCH_TASKS_FAIL
    };
};

export const fetchTasks = () => {
    return dispatch => {
        axios.get('/tasks')
            .then(response => {
                dispatch(fetchTasksSuccess(response.data));
            })
            .catch(err => {
                dispatch(fetchTasksFail());
                toastr.error('Tasks', 'Something went wrong. Please try again.');
            });
    };
};

export const setSelectedTask = (piId, taskId,taskName) => {
    return {
        type: actionTypes.SET_SELECTED_TASK,
        piId: piId,
        taskId:taskId,
        taskName:taskName
    };
};