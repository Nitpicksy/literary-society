import React, { useEffect, useState } from 'react';
import * as actions from './CreatePublicationRequestExport';
import { extractControls } from '../../../utility/extractControls';
import Form from '../../../UI/Form/Form';
import { CssBaseline, Typography, Container, Avatar, Button } from '@material-ui/core';
import NoteAddIcon from '@material-ui/icons/NoteAdd';
import { useStyles } from './CreatePublicationRequestStyles';
import { connect } from 'react-redux';
import { useHistory } from 'react-router';

const CreatePublicationRequest = (props) => {
    const history = useHistory();
    const classes = useStyles();
    const { formFields, fetchForm } = props;
    let form = null;
    let [controls, setControls] = useState(null);
    const [formIsValid, setFormIsValid] = useState(false);

    // DODAJ responseInterceptor

    useEffect(() => {
        fetchForm();
    }, [fetchForm]);

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
            array.push({ fieldId: key, fieldValue: value });
        }
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
                    <NoteAddIcon />
                </Avatar>
                <Typography component="h1" variant="h5" className={classes.title}>Create Publication Request</Typography>
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
        processInstanceId: state.createPublicationRequest.processInstanceId,
        taskId: state.createPublicationRequest.taskId,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchForm: () => dispatch(actions.fetchForm()),
        createRequest: (enteredData, taskId, history) => actions.createRequest(enteredData, taskId, history)
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(CreatePublicationRequest);