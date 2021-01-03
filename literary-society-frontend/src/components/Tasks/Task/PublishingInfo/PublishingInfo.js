import React, { useEffect, useState } from 'react';
import ListIcon from '@material-ui/icons/List';
import { connect } from 'react-redux';
import { useStyles } from './PublishingInfoStyles';
import * as actions from './PublishingInfoExport';
import * as signInActions from '../../../Authentication/SignIn/SignInExport';
import { responseInterceptor } from '../../../../responseInterceptor';
import { useHistory } from 'react-router';
import Form from '../../../../UI/Form/Form';
import { extractControls } from '../../../../utility/extractControls';
import PublicationRequestCard from '../PublicationRequest/PublicationRequestCard/PublicationRequestCard';
import { Typography, Container, Avatar, CssBaseline, Button, Grid } from '@material-ui/core';
import { toastr } from 'react-redux-toastr';

const PublishingInfo = (props) => {
    const history = useHistory();
    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);
    const classes = useStyles();

    const { selectedTask } = props;
    const { formFields, fetchForm } = props;
    const { publicationRequest } = props;
    const [image, setImage] = useState(null);

    let [controls, setControls] = useState(null);
    const [formIsValid, setFormIsValid] = useState(false);

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
        if (!image) {
            toastr.warning('Publishing Info', 'Please choose image file.');
            return;
        } 

        let array = [];
        for (let [key, data] of Object.entries(controls)) {
            let value = data.value
            if (Array.isArray(data.value)) {
                value = data.value.join();
            }
            array.push({ fieldId: key, fieldValue: value });
        }
        const formData = new FormData();
        formData.append('image', image);
        formData.append('formDTOList', new Blob([JSON.stringify(array)], { type: "application/json" }));
      
        props.onConfirm(formData, selectedTask.taskId, history);
    }

    const handleChooseFile = ({ target }) => {
        setImage(target.files[0]);
    }

    if (controls) {
        form = <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />;
    }

    if (publicationRequest) {
        publicationRequestCard = <PublicationRequestCard book={publicationRequest} />
    }

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
                    {form}
                    <Grid item xs={12} >
                        <input type="file" accept="image/*" hidden id="upload-file"
                            onChange={handleChooseFile} 
                        />
                        <label htmlFor="upload-file">
                            <Grid container >
                                <Grid item xs={4} >
                                    <Button color="primary" variant="contained" component="span">
                                        Choose file
                                    </Button>
                                </Grid>
                                <Grid item xs={8} className={classes.fileNameGrid}>
                                    <Typography variant="body2" component="span" className={classes.fileName}>
                                        {image ? image.name : ''}
                                    </Typography>
                                </Grid>
                            </Grid>
                        </label>
                    </Grid>
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
        formFields: state.publishingInfo.formFields,
        publicationRequest: state.publishingInfo.publicationRequest,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchForm: (piId, taskId) => dispatch(actions.fetchForm(piId, taskId)),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
        onConfirm: (formData, taskId, history) => dispatch(actions.confirm(formData, taskId, history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PublishingInfo);