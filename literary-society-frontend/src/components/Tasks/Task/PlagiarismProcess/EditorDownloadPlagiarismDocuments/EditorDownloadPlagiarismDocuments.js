import React, { useEffect, useState } from 'react';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import ListIcon from '@material-ui/icons/List';
import GetAppIcon from '@material-ui/icons/GetApp';
import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';
import { connect } from 'react-redux';
import { useStyles } from './EditorDownloadPlagiarismDocumentsStyles';
import * as signInActions from '../../../../Authentication/SignIn/SignInExport';
import { responseInterceptor } from '../../../../../responseInterceptor';
import { useHistory } from 'react-router';
import Button from '@material-ui/core/Button';
import PlagiarismDetailsCard from '../PlagiarismDetailsCard/PlagiarismDetailsCard';
import * as actions from './EditorDownloadPlagiarismDocumentsExport';
import BookDetailsCard from '../BookDetailsCard/BookDetailsCard';
import { Grid } from '@material-ui/core';

const EditorDownloadPlagiarismDocuments = (props) => {


    const history = useHistory();
    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);
    const classes = useStyles();

    const { selectedTask } = props;
    const { fetchForm } = props;
    const { plagiarismDetails } = props;

    let [downloaded, setDownloaded] = useState(false);

    let plagiarismDetailsCard = null;
    let submittedBookDetailsCard = null;

    useEffect(() => {
        fetchForm(selectedTask.piId, selectedTask.taskId);
    }, [fetchForm, selectedTask.piId, selectedTask.taskId]);


    const download = (title, type) => {
        console.log('ti', title, type)
        if(!downloaded) {
            props.onDownload(selectedTask.piId, selectedTask.taskId, title, type, history, false);
            setDownloaded(true);
        } else {
            props.onDownload(selectedTask.piId, selectedTask.taskId, title, type, history, true ); 
        }
        // props.onDownload(selectedTask.piId, selectedTask.taskId, plagiarismDetails.title, 'REPORTED', history);
    }

    console.log('pd', plagiarismDetails)
    if (plagiarismDetails) {
        plagiarismDetailsCard = <PlagiarismDetailsCard plagiarismDetails={plagiarismDetails} showDownload={true} download={download}/>

        if(plagiarismDetails.bookDetails != null) {
            submittedBookDetailsCard = <BookDetailsCard bookDetails = {plagiarismDetails.bookDetails} showDownload={true} download={download}/>
        }
    }


    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <ListIcon />
                </Avatar>
                <Typography component="h1" variant="h4" className={classes.title}>Plagiarism report</Typography>
                <div className={classes.card} justify="center">
                    {submittedBookDetailsCard}
                    &nbsp;
                    {plagiarismDetailsCard}
                </div>
            </div>
        </Container>
    );
};


const mapStateToProps = state => {
    return {
        selectedTask: state.tasks.selectedTask,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent,
        plagiarismDetails: state.editorsDownloadPlagiarismDocuments.plagiarismDetails,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
        fetchForm: (piId, taskId) => dispatch(actions.fetchForm(piId, taskId)),
        onDownload: (piId, taskId, writerTitle, type, history, downloaded) => dispatch(actions.download(piId, taskId, writerTitle, type, history, downloaded)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(EditorDownloadPlagiarismDocuments);