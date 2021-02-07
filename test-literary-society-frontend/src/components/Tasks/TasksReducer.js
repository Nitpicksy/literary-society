import * as actionTypes from './TasksActionTypes';

const initialState = {
    tasks: null,
    selectedTask: {
        piId: null,
        taskId: null,
        taskName: ''
    }
}

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.FETCH_TASKS_SUCCESS:
            return {
                ...state,
                tasks: action.tasks,
            };
        case actionTypes.FETCH_TASKS_FAIL:
            return {
                ...state,
                tasks: null,
            };
        case actionTypes.SET_SELECTED_TASK:
            return {
                ...state,
                selectedTask: {
                    piId: action.piId,
                    taskId: action.taskId,
                    taskName: action.taskName
                },
            };
        default:
            return state;
    }
};

export default reducer;