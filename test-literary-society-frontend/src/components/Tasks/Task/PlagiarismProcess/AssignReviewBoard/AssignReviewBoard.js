import React, { useEffect, useState } from 'react';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import PolicyIcon from '@material-ui/icons/Policy';
import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';
import { connect } from 'react-redux';
import { useStyles } from './AssignReviewBoardStyles';
import * as actions from './AssignReviewBoardExport';
import * as signInActions from '../../../../Authentication/SignIn/SignInExport';
import { responseInterceptor } from '../../../../../responseInterceptor';
import { useHistory } from 'react-router';
import Form from '../../../../../UI/Form/Form';
import { extractControls } from '../../../../../utility/extractControls';
import Button from '@material-ui/core/Button';
import PlagiarismDetailsCard from '../PlagiarismDetailsCard/PlagiarismDetailsCard';

const EditorChooseBetaReaders = (props) => {
    const history = useHistory();
    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);
    const classes = useStyles();

    const { selectedTask } = props;
    const { formFields, fetchForm } = props;
    const { plagiarismDetails } = props;

    let [controls, setControls] = useState(null);
    const [formIsValid, setFormIsValid] = useState(false);

    let form = null;
    let plagiarismDetailsCard = null;

    useEffect(() => {
        fetchForm(selectedTask.piId, selectedTask.taskId);
    }, [fetchForm, selectedTask.piId, selectedTask.taskId]);

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

    if (plagiarismDetails) {
        plagiarismDetailsCard = <PlagiarismDetailsCard plagiarismDetails={plagiarismDetails} />
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <PolicyIcon />
                </Avatar>
                <Typography component="h1" variant="h4" className={classes.title}>Plagiarism report</Typography>
                <div className={classes.card} justify="center">
                    {plagiarismDetailsCard}
                </div>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    {form}
                    <Button type="submit" color="primary" className={classes.submit}
                        variant="contained" disabled={!formIsValid} fullWidth>
                        Confirm
                    </Button>
                </form>
            </div>
        </Container>
    );
};


const mapStateToProps = state => {
    return {
        selectedTask: state.tasks.selectedTask,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent,
        formFields: state.assignReviewBoard.formFields,
        plagiarismDetails: state.assignReviewBoard.plagiarismDetails,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchForm: (piId, taskId) => dispatch(actions.fetchForm(piId, taskId)),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
        onConfirm: (data, taskId, history) => dispatch(actions.confirm(data, taskId, history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(EditorChooseBetaReaders);