import React, { useState, useEffect } from 'react';
import { Typography, Container, Avatar, CssBaseline, Button, Card, CardContent } from '@material-ui/core';
import { connect } from 'react-redux';
import { useStyles } from './CommitteeVotingStyles';
import { responseInterceptor } from '../../../../../responseInterceptor';
import { useHistory } from 'react-router';
import * as signInActions from '../../../../Authentication/SignIn/SignInExport';
import * as actions from './CommitteeVotingExport';
import Form from '../../../../../UI/Form/Form';
import { extractControls } from '../../../../../utility/extractControls';
import PeopleIcon from '@material-ui/icons/People';
import DisplayPDFDocuments from '../DisplayPDFDocuments/DisplayPDFDocuments';

const CommitteeVoting = (props) => {

    const { selectedTask } = props;
    const { formFields, fetchForm } = props;
    const { writerDocuments } = props;

    const classes = useStyles();
    const history = useHistory();

    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);

    let [controls, setControls] = useState(null);
    const [formIsValid, setFormIsValid] = useState(false);

    useEffect(() => {
        fetchForm(selectedTask.piId, selectedTask.taskId);
    }, [fetchForm, selectedTask.piId, selectedTask.taskId]);

    let form = null;
    let displayDocuments = null;
    let writerUsername = null;

    useEffect(() => {
        if (formFields) {
            let extractedControls = extractControls(formFields)
            setControls(extractedControls);
        }
    }, [formFields, writerDocuments]);


    const submitHander = (event) => {
        event.preventDefault();
    }

    if (controls) {
        form = <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />;
    }

    if(writerDocuments) {
        displayDocuments = <DisplayPDFDocuments files = {writerDocuments} />
        writerUsername = writerDocuments[0].writerUsername;
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <PeopleIcon />
                </Avatar>
                <Typography component="h1" variant="h4" className={classes.title}>Committee voting</Typography>
                <br/>
                <Card className={classes.cardContent}>
                <Typography component="h5" variant="h5" className={classes.title}>Submitted documents</Typography>
                    <CardContent>
                    <Typography component="h6" variant="h6" className={classes.title}>{writerUsername}</Typography>
                    {displayDocuments}
                    </CardContent>
                </Card>

                <form className={classes.form} noValidate onSubmit={submitHander}>
                    {form}
                    <Button type="submit" color="primary" className={classes.submit}
                        variant="contained" disabled={!formIsValid} fullWidth>
                        Confirm
                    </Button>
                </form>
            </div>
        </Container>
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