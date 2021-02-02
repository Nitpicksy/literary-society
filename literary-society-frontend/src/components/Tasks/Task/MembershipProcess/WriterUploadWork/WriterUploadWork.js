import React, { useState, useEffect } from 'react';
import { Typography, Container, Avatar, CssBaseline, Button, Grid} from '@material-ui/core';
import { connect } from 'react-redux';
import { useStyles } from './WriterUploadWorkStyles';
import { responseInterceptor } from '../../../../../responseInterceptor';
import { useHistory } from 'react-router';
import * as signInActions from '../../../../Authentication/SignIn/SignInExport';
import * as actions from './WriterUploadWorkExport';
import PublishIcon from '@material-ui/icons/Publish';
import PictureAsPdfIcon from '@material-ui/icons/PictureAsPdf';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import PreviewPDFModal from '../PreviewPDFModal/PreviewPDFModal';
import VisibilityIcon from '@material-ui/icons/Visibility';
import DisplayPDFDocuments from '../DisplayPDFDocuments/DisplayPDFDocuments';
import CommitteeComments from '../CommitteeComments/CommitteeComments';
import Form from '../../../../../UI/Form/Form';
import { extractControls } from '../../../../../utility/extractControls';


const WriterUploadWork = props => {

    const { formFields, fetchForm } = props;

    const { selectedTask } = props;
    const { fetchDrafts, drafts } = props;
    const { fetchComments, comments } = props;

    const classes = useStyles();
    const history = useHistory();

    const [files, setFiles] = useState([]);
    const [currentFile, setCurrentFile] = useState(null);
    const [displayModal, setDisplayModal] = useState(false);
    let [controls, setControls] = useState(null);
    const [formIsValid, setFormIsValid] = useState(false);

    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);



    useEffect(() => {
        fetchForm(selectedTask.piId, selectedTask.taskId);
    }, [fetchForm, selectedTask.piId, selectedTask.taskId]);



    useEffect(() => {
        if (formFields) {
            let extractedControls = extractControls(formFields)
            setControls(extractedControls);
        }
    }, [formFields]);


    let form = null;
    let displayDocuments = null;
    let modal = null;


    const handleChooseFile = ( target ) => {
        const file = files.filter(item => item.name === target.name) //if you're readding the same file, return
        if(file.length !== 0) {
            return;
        }

        setFiles(prevFiles => [...prevFiles, target]); //otherwise append it to the array
    }

    useEffect(() => {
        fetchDrafts();
    }, [fetchDrafts]);

    useEffect(() => {
        fetchComments();
    }, [fetchComments]);

    const handleRemoveDocument = (file) => {
        setFiles(files.filter(item => item.name !== file.name));
        setCurrentFile(null);
        setDisplayModal(false);
    }

    const previewDocument = (file) => {
        setCurrentFile(file);
        setDisplayModal(true);
    }

    const exitPreview = () => {
        setCurrentFile(null);
        setDisplayModal(false)
    }

    const handleUpload = () => {

        let filesFormData = new FormData();
        files.forEach(file => {
            filesFormData.append('files', file);
        })

        props.onUpload(selectedTask.piId, selectedTask.taskId, filesFormData, history);

    }

    const disableUploadButton = () => {
        // eslint-disable-next-line
        if(drafts == null || drafts.length == 0) {
            if(files.length < 2) {
                return true;
            }

            return false;
        }

        if(drafts.length > 0) {

            if(files.length < 1) {
                return true;
            }

            return false;
        }

        return false;
        // (files.length < 2 && (drafts === undefined || drafts.length === 0)) || ((files.length < 1 && (drafts !== undefined && drafts.length > 0)) ) ? true : false
    }

    if(drafts) {
        displayDocuments = <DisplayPDFDocuments files = {drafts} />
    }

    if(displayModal) {
        modal = <PreviewPDFModal file={currentFile} close={exitPreview}/>
    }

    if (controls) {
        form = <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} setPdfFile={handleChooseFile} removeText={true}/>;
    }

    console.log('form is valid', formIsValid)
    return (
        <Container component="main" maxWidth="xs">
            {modal}
            <CssBaseline />
            <div className={classes.paper}>
            <Typography component="h1" variant="h4" className={classes.title}>Membership request</Typography>
                <Avatar className={classes.avatar}>
                    <PictureAsPdfIcon />
                </Avatar>

                {(comments && comments.length > 0)   ? 
                <CommitteeComments comments={comments} /> : <Typography component="h1" variant="h6" className={classes.title}>Upload at least two of your best pieces of writing</Typography>
                } 
                <br/>           
                <br/>
                  
            <Grid container           
                alignItems="center"
                justify="center" 
                spacing={2} >
                {displayDocuments}
                <Grid item  className={classes.fileNameGrid}>
                {files.map((file, index) => {
                    return (
                    <div key={`index_${index}`}>
                    <Typography  variant="body2" component="span" className={classes.fileName}>
                        {file.name}
                        <IconButton className={classes.closeButton} aria-label="preview"
                        onClick={() => previewDocument(file)}
                        >
                        <VisibilityIcon />
                        </IconButton>

                        <IconButton className={classes.closeButton} aria-label="close" 
                        onClick={() => handleRemoveDocument(file)}
                        >
                        <CloseIcon />
                        </IconButton>
                    </Typography>
                    <br/>
                    </div>)
                })}
                </Grid>
            </Grid>
            <br/>

            <Grid 
                container 
                alignItems="center"
                justify="center" 
                spacing={2}
            >
                <Grid item  >
                    {form}
                </Grid>

                <Grid item>
                <Button m={1} color="primary" variant="contained" component="span"
                        startIcon={<PublishIcon />} 
                        onClick={handleUpload} 
                         disabled={disableUploadButton()}
                        // disabled={!formIsValid}
                        >
                        Upload
                    </Button>

                </Grid>
            </Grid>
            <br/>
            </div>
        </Container>
    )
}



const mapStateToProps = state => {
    return {
        selectedTask: state.tasks.selectedTask,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent,
        formFields: state.writerUploadWork.formFields,
        committeeComments: state.writerUploadWork.committeeComments,
        drafts: state.writerUploadWork.drafts,
        comments: state.writerUploadWork.comments
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
        onUpload: (piId, taskId, files, history) => dispatch(actions.upload(piId, taskId, files, history)),
        fetchDrafts: () => dispatch(actions.fetchDrafts()),
        fetchComments: () => dispatch(actions.fetchComments()),
        fetchForm: (piId, taskId) => dispatch(actions.fetchForm(piId, taskId)),
    }
};


export default connect(mapStateToProps, mapDispatchToProps)(WriterUploadWork);