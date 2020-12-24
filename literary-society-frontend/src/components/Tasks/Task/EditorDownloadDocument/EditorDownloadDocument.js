import React, { useEffect, useState } from 'react';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import ListIcon from '@material-ui/icons/List';
import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';
import { connect } from 'react-redux';
import { useStyles } from './EditorDownloadDocumentStyles';
import * as signInActions from '../../../Authentication/SignIn/SignInExport';
import { responseInterceptor } from '../../../../responseInterceptor';
import { useHistory } from 'react-router';
import Button from '@material-ui/core/Button';
import PublicationRequestCard from '../PublicationRequest/PublicationRequestCard/PublicationRequestCard';
import { toastr } from 'react-redux-toastr';
import * as actions from './EditorDownloadDocumentExport';

const EditorDownloadDocument = (props) => {

    const history = useHistory();
    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);
    const classes = useStyles();

    const { selectedTask } = props;
    const { fetchForm } = props;
    const { publicationRequest } = props;

    let publicationRequestCard = null;

    useEffect(() => {
        fetchForm(selectedTask.piId, selectedTask.taskId);
    }, [fetchForm, selectedTask.piId, selectedTask.taskId]);


    const download = () => {
        props.onDownload(selectedTask.piId, selectedTask.taskId,publicationRequest.title, history);
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
                <Button color="primary" className={classes.submit} variant="contained" onClick = {download}>
                    Download
                </Button>
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
        publicationRequest: state.editorDownloadDocument.publicationRequest,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
        fetchForm: (piId, taskId) => dispatch(actions.fetchForm(piId, taskId)),
        onDownload: (piId, taskId,title, history) => dispatch(actions.download(piId, taskId,title, history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(EditorDownloadDocument);