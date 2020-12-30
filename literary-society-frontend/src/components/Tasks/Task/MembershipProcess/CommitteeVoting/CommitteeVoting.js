import React, { useState, useEffect } from 'react';
import { Typography, Container, Avatar, CssBaseline, Button, Grid } from '@material-ui/core';
import { connect } from 'react-redux';
import { useStyles } from './CommitteeVotingStyles';
import { responseInterceptor } from '../../../../../responseInterceptor';
import { useHistory } from 'react-router';
import * as signInActions from '../../../../Authentication/SignIn/SignInExport';
import * as actions from './CommitteeVotingExport';


const CommitteeVoting = (props) => {

    const { selectedTask } = props;
    const { formFields, fetchForm } = props;

    const classes = useStyles();
    const history = useHistory();

    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);

    useEffect(() => {
        fetchForm(selectedTask.piId, selectedTask.taskId);
    }, [fetchForm, selectedTask.piId, selectedTask.taskId]);

    console.log('fields', formFields);

    return (
        <>
        <div>hi</div>
        </>
    )
}

const mapStateToProps = state => {
    return {
        selectedTask: state.tasks.selectedTask,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent,
        formFields: state.committeeVoting.formFields,
        writerDocuments: state.committeeVoting.writerDocuments,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
        fetchForm: (piId, taskId) => dispatch(actions.fetchForm(piId, taskId)),
        //TODO
    }
};

export default connect(mapStateToProps,mapDispatchToProps)(CommitteeVoting);