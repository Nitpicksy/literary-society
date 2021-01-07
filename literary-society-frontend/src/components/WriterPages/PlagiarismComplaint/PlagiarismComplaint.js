import React, { useEffect, useState } from 'react';
import * as actions from './PlagiarismComplaintExport';
import * as signInActions from '../../Authentication/SignIn/SignInExport';
import { extractControls } from '../../../utility/extractControls';
import Form from '../../../UI/Form/Form';
import { CssBaseline, Typography, Container, Avatar, Button } from '@material-ui/core';
import PolicyIcon from '@material-ui/icons/Policy';
import { useStyles } from './PlagiarismComplaintStyles';
import { connect } from 'react-redux';
import { useHistory } from 'react-router';
import { responseInterceptor } from '../../../responseInterceptor';

const PlagiarismComplaint = (props) => {
    const history = useHistory();
    const classes = useStyles();
    responseInterceptor.setupInterceptor(history, 
        props.refreshTokenRequestSent, 
        props.onRefreshToken);
    const { formFields, fetchForm } = props;
    const { chosenPlagiarismBook } = props;
    let form = null;
    let [controls, setControls] = useState(null);
    const [formIsValid, setFormIsValid] = useState(false);

    useEffect(() => {
        if (props.processInstanceId && props.taskId) {
            fetchForm(props.processInstanceId, props.taskId);
        }
    }, [fetchForm, props.processInstanceId, props.taskId]);

    useEffect(() => {
        if (formFields) {
            let extractedControls = extractControls(formFields)
            setControls(extractedControls);
        }
    }, [formFields]);

    const submitHander = (event) => {
        event.preventDefault();
        let array = [];
        for (let [key, data] of Object.entries(controls)) {
            // convert array to comma-separated string
            let value = data.value
            if (Array.isArray(data.value)) {
                value = data.value.join();
            }

            if(key === 'book') {
                array.push({ fieldId: key, fieldValue: chosenPlagiarismBook.id });
            } else {
                array.push({ fieldId: key, fieldValue: value });
            }

        }
                console.log('array', array)
        props.createRequest(array, props.taskId, history);
    }

    if (controls) {
        form = <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />;
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <PolicyIcon />
                </Avatar>
                <Typography component="h1" variant="h5" className={classes.title}>Plagiarism complaint</Typography>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    {form}
                    <Button type="submit" color="primary" className={classes.submit} fullWidth
                        variant="contained" disabled={!formIsValid}>
                        Create
                    </Button>
                </form>
            </div>
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        formFields: state.createPublicationRequest.formFields,
        processInstanceId: state.publRequests.plagiarismProcessInstanceId,
        taskId: state.publRequests.plagiarismTaskId,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent,
        chosenPlagiarismBook: state.publRequests.chosenPlagiarismBook,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchForm: (piId, taskId) => dispatch(actions.fetchForm(piId, taskId)),
        createRequest: (enteredData, taskId, history) => actions.createRequest(enteredData, taskId, history),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PlagiarismComplaint);