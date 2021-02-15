import React, { useEffect, useState } from 'react';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import ListIcon from '@material-ui/icons/List';
import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';
import { connect } from 'react-redux';
import { useStyles } from './EditorChooseBetaReadersStyles';
import * as actions from './EditorChooseBetaReadersExport';
import * as signInActions from '../../../Authentication/SignIn/SignInExport';
import { responseInterceptor } from '../../../../responseInterceptor';
import { useHistory } from 'react-router';
import Form from '../../../../UI/Form/Form';
import { extractControls } from '../../../../utility/extractControls';
import Button from '@material-ui/core/Button';
import PublicationRequestCard from '../PublicationRequest/PublicationRequestCard/PublicationRequestCard';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';

const EditorChooseBetaReaders = (props) => {
    const history = useHistory();
    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);
    const classes = useStyles();

    const { selectedTask, betaReaders } = props;
    const { formFields, fetchForm } = props;
    const { publicationRequest } = props;

    let [controls, setControls] = useState(null);
    const [formIsValid, setFormIsValid] = useState(false);
    const [filterBetaReaders, setFilterBetaReaders] = useState(false);

    let form = null;
    let publicationRequestCard = null;

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

    if (publicationRequest) {
        publicationRequestCard = <PublicationRequestCard book={publicationRequest} />
    }

    const inputChangedHandler = (event) => {
        let value = event.target.checked;
        props.filterBetaReaders(selectedTask.piId, value, formFields);

        setFilterBetaReaders(value);
    }

    useEffect(() => {
        if (betaReaders) {
            let extractedControls = extractControls(formFields)
            setControls(extractedControls);
        }
    }, [betaReaders]);

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <ListIcon />
                </Avatar>
                <Typography component="h1" variant="h4" className={classes.title}>Publication Request</Typography>
                <div className={classes.card} justify="center">
                    {publicationRequestCard}
                </div>

                <form className={classes.form} noValidate onSubmit={submitHander}>
                    <FormControlLabel label="Show only beta-readers farther than 100km from the writer"
                        control={
                            <Checkbox style={{
                                transform: "scale(1.1)"
                            }} value={filterBetaReaders} onChange={(event) => inputChangedHandler(event)} />
                        } />
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
        formFields: state.editorChooseBetaReaders.formFields,
        publicationRequest: state.editorChooseBetaReaders.publicationRequest,
        betaReaders: state.editorChooseBetaReaders.betaReaders,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchForm: (piId, taskId) => dispatch(actions.fetchForm(piId, taskId)),
        filterBetaReaders: (piId, filter, formFields) => dispatch(actions.filterBetaReaders(piId, filter,formFields)),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
        onConfirm: (data, taskId, history) => dispatch(actions.confirm(data, taskId, history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(EditorChooseBetaReaders);