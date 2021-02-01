import React, { useEffect, useState } from 'react';
import { Typography, Container, Avatar, CssBaseline, Button, Grid } from '@material-ui/core';
import ListIcon from '@material-ui/icons/List';
import PublishIcon from '@material-ui/icons/Publish';
import { connect } from 'react-redux';
import { useStyles } from './WriterUploadDocumentStyles';
import * as signInActions from '../../../Authentication/SignIn/SignInExport';
import { responseInterceptor } from '../../../../responseInterceptor';
import { useHistory } from 'react-router';
import PublicationRequestCard from '../PublicationRequest/PublicationRequestCard/PublicationRequestCard';
import * as actions from './WriterUploadDocumentExport';
import { toastr } from 'react-redux-toastr';
import { extractControls } from '../../../../utility/extractControls';
import Form from '../../../../UI/Form/Form';

const WriterUploadDocument = (props) => {

    const [pdfFile, setPdfFile] = useState(null);
    let [controls, setControls] = useState(null);
    const [formIsValid, setFormIsValid] = useState(false);

    const history = useHistory();
    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);
    const classes = useStyles();

    const { selectedTask } = props;
    const { publicationRequest } = props;
    const { formFields, fetchForm } = props;
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

    const handleChooseFile = ({ target }) => {
        setPdfFile(target.files[0]);
    }

    const handleUpload = () => {
        if (pdfFile) {
            const pdfFormData = new FormData();
            pdfFormData.append('pdfFile', pdfFile);
            props.onUpload(selectedTask.piId, selectedTask.taskId, pdfFormData, history);
        } else {
            toastr.warning('Upload manuscript', 'Please choose PDF file to upload.')
        }
    }

    if (publicationRequest) {
        publicationRequestCard = <PublicationRequestCard book={publicationRequest} />
    }

    if (controls) {
        form = <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} setPdfFile={setPdfFile} />;
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <ListIcon />
                </Avatar>
                <Typography component="h1" variant="h4" className={classes.title}>Publication Request</Typography>
                <Grid container className={classes.uploadGrid}>
                    <Grid item xs={9} >
                        {form}
                    </Grid>
                    <Grid item xs={3} className={classes.uploadBtn}>
                        <Button color="primary" variant="contained" component="span"
                            startIcon={<PublishIcon />} onClick={handleUpload} 
                            disabled={pdfFile ? false : true}>
                            Upload
                        </Button>
                    </Grid>
                </Grid>

                <div className={classes.card} justify="center">
                    {publicationRequestCard}
                </div>
            </div>
        </Container>
    );
};


const mapStateToProps = state => {
    return {
        selectedTask: state.tasks.selectedTask,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent,
        formFields: state.writerUploadDocument.formFields,
        publicationRequest: state.writerUploadDocument.publicationRequest,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
        fetchForm: (piId, taskId) => dispatch(actions.fetchForm(piId, taskId)),
        onUpload: (piId, taskId, pdfFormData, history) => dispatch(actions.upload(piId, taskId, pdfFormData, history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(WriterUploadDocument);