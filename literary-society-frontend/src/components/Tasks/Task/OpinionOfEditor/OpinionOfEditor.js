import React, { useEffect, useState } from 'react';
import { Typography, Container, Avatar, CssBaseline, Button, Grid, Paper, List, ListItem, ListItemText } from '@material-ui/core';
import ListIcon from '@material-ui/icons/List';
import PublishIcon from '@material-ui/icons/Publish';
import { connect } from 'react-redux';
import { useStyles } from './OpinionOfEditorStyles';
import * as signInActions from '../../../Authentication/SignIn/SignInExport';
import { responseInterceptor } from '../../../../responseInterceptor';
import { useHistory } from 'react-router';
import PublicationRequestCard from '../PublicationRequest/PublicationRequestCard/PublicationRequestCard';
import * as actions from './OpinionOfEditorExport';
import { toastr } from 'react-redux-toastr';
import Form from '../../../../UI/Form/Form';
import { extractControls } from '../../../../utility/extractControls';

const OpinionOfEditor = (props) => {

    const [pdfFile, setPdfFile] = useState(null);
    let [controls, setControls] = useState(null);
    const history = useHistory();
    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);
    const classes = useStyles();

    const { selectedTask } = props;
    const { fetchForm, fetchOpinion,formFields } = props;
    const { publicationRequest } = props;
    const [formIsValid, setFormIsValid] = useState(false);

    let publicationRequestCard = null;
    let opinionItem = null;
    let form = null;

    useEffect(() => {
        fetchForm(selectedTask.piId, selectedTask.taskId);
    }, [fetchForm, selectedTask.piId, selectedTask.taskId]);

    useEffect(() => {
        if (publicationRequest) {
            fetchOpinion(publicationRequest.id)
        }
    }, [publicationRequest, fetchOpinion])

    useEffect(() => {
        if (formFields) {
            let extractedControls = extractControls(formFields)
            setControls(extractedControls);
        }
    }, [formFields]);

    const handleUpload = () => {
        if (pdfFile) {
            const pdfFormData = new FormData();
            pdfFormData.append('pdfFile', pdfFile);
            props.onUpload(selectedTask.piId, selectedTask.taskId, pdfFormData, history);
        } else {
            toastr.warning('Upload edited manuscript', 'Please choose PDF file to upload.')
        }
    }

    if (publicationRequest) {
        publicationRequestCard = <PublicationRequestCard book={publicationRequest} />
    }

    if (controls) {
        form = <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} setPdfFile={setPdfFile} />;
    }

    if (props.opinion) {
        opinionItem =
            <ListItem alignItems="flex-start">
                <ListItemText primary={props.opinion.comment} secondary={<i>by editor {props.opinion.commenterName}</i>} />
            </ListItem>;
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

                <Paper justify="center" className={classes.opinionsPaper}>
                    <Typography component="h1" variant="h5" className={classes.title}>Opinion of Editor</Typography>
                    <List>
                        {opinionItem}
                    </List>
                </Paper>

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
            </div>
        </Container>
    );
};


const mapStateToProps = state => {
    return {
        formFields: state.opinionOfEditor.formFields,
        selectedTask: state.tasks.selectedTask,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent,
        publicationRequest: state.opinionOfEditor.publicationRequest,
        opinion: state.opinionOfEditor.opinion
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
        fetchForm: (piId, taskId) => dispatch(actions.fetchForm(piId, taskId)),
        fetchOpinion: (bookId) => dispatch(actions.fetchOpinion(bookId)),
        onUpload: (piId, taskId, pdfFormData, history) => dispatch(actions.upload(piId, taskId, pdfFormData, history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(OpinionOfEditor);