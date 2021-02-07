import React, { useState, useEffect } from 'react';
import { Typography, Container, Avatar, CssBaseline, Button } from '@material-ui/core';
import { connect } from 'react-redux';
import { useStyles } from './CommitteePlagiarismVoteStyles';
import { responseInterceptor } from '../../../../../responseInterceptor';
import { useHistory } from 'react-router';
import * as signInActions from '../../../../Authentication/SignIn/SignInExport';
import * as actions from './CommitteePlagiarismVoteExport';
import Form from '../../../../../UI/Form/Form';
import { extractControls } from '../../../../../utility/extractControls';
import PeopleIcon from '@material-ui/icons/People';
import EditorComments from '../EditorComments/EditorComments'
import PlagiarismDetailsCard from '../PlagiarismDetailsCard/PlagiarismDetailsCard';

const CommitteePlagiarismVote = (props) => {

    const { selectedTask } = props;
    const { formFields, fetchForm } = props;
    const { plagiarismDetails, editorsComments } = props;

    const classes = useStyles();
    const history = useHistory();

    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);

    let [controls, setControls] = useState(null);
    const [formIsValid, setFormIsValid] = useState(false);

    useEffect(() => {
        fetchForm(selectedTask.piId, selectedTask.taskId);
    }, [fetchForm, selectedTask.piId, selectedTask.taskId]);

    let form = null;
    let displayPlagiarismDetailsCard = null;
    let displayEditorsComments = null;

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
            let value = data.value
            if (Array.isArray(data.value)) {
                value = data.value.join();
            }
            array.push({ fieldId: key, fieldValue: value });
        }


        props.onConfirm(array, selectedTask.taskId, history);
    }

    if (controls) {
        form = <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />;
    }

    if(plagiarismDetails) {
        displayPlagiarismDetailsCard = <PlagiarismDetailsCard plagiarismDetails={plagiarismDetails} showDownload={false}/>
    }

    if(editorsComments) {
        console.log('ed', editorsComments)
        displayEditorsComments = <EditorComments comments={editorsComments} />
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
                <Typography component="h5" variant="h5" className={classes.title}></Typography>
                {displayPlagiarismDetailsCard}
                <br/>
                {displayEditorsComments}

                <form className={classes.form} noValidate onSubmit={submitHander}>
                    {form}
                    <Button type="submit" color="primary" className={classes.submit}
                        variant="contained" disabled={!formIsValid} fullWidth>
                        Vote
                    </Button>

                </form>
                <br/>
            </div>
        </Container>
    )
}

const mapStateToProps = state => {
    return {
        selectedTask: state.tasks.selectedTask,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent,
        formFields: state.committeePlagiarismVote.formFields,
        plagiarismDetails: state.committeePlagiarismVote.plagiarismDetails,
        editorsComments: state.committeePlagiarismVote.editorsComments
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
        fetchForm: (piId, taskId) => dispatch(actions.fetchForm(piId, taskId)),
        onConfirm: (data, taskId, history) => dispatch(actions.confirm(data, taskId, history)),
    }
};

export default connect(mapStateToProps,mapDispatchToProps)(CommitteePlagiarismVote);