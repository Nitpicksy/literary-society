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
import { toastr } from 'react-redux-toastr';

const PlagiarismComplaint = (props) => {
    const history = useHistory();
    const classes = useStyles();
    responseInterceptor.setupInterceptor(history, 
        props.refreshTokenRequestSent, 
        props.onRefreshToken);
    const { formFields, fetchForm } = props;
    const { chosenPlagiarismBook } = props;
    const { valid } = props;
    let form = null;
    let [controls, setControls] = useState(null);
    let [array, setArray] = useState(null);
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


    if(valid === false) {
        toastr.error('Oops', 'Unable to find a book with the given author and title')
        props.clearValidation();
    } else if(valid === true) {
        props.createRequest(array, props.taskId, history)
    }

    const submitHander = (event) => {
        event.preventDefault();
        let array = [];

        let title = '';
        let writer = '';

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

            if(key === 'title') {
                title = value;
            }
            if(key === 'writer') {
                writer = value;
            }

        }
        props.validateBook(title, writer);
        setArray(array)
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
                <Typography component="h1" variant="h5" className={classes.title}>Plagiarism report</Typography>
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
        formFields: state.plagiarismComplaint.formFields,
        processInstanceId: state.publRequests.plagiarismProcessInstanceId,
        taskId: state.publRequests.plagiarismTaskId,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent,
        chosenPlagiarismBook: state.publRequests.chosenPlagiarismBook,
        valid: state.plagiarismComplaint.valid,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchForm: (piId, taskId) => dispatch(actions.fetchForm(piId, taskId)),
        createRequest: (enteredData, taskId, history) => actions.createRequest(enteredData, taskId, history),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
        validateBook: (bookTitle, writerName) => dispatch(actions.validateBook(bookTitle, writerName)),
        clearValidation: () => dispatch(actions.clearValidation()),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PlagiarismComplaint);