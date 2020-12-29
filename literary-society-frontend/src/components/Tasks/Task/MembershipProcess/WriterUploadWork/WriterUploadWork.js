import React, { useState } from 'react';
import { Typography, Container, Avatar, CssBaseline, Button, Grid } from '@material-ui/core';
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


const WriterUploadWork = props => {

    const { selectedTask } = props;

    const classes = useStyles();
    const history = useHistory();

    const [files, setFiles] = useState([]);
    const [currentFile, setCurrentFile] = useState(null);
    const [displayModal, setDisplayModal] = useState(false);

    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);

    const handleChooseFile = ({ target }) => {
        const file = files.filter(item => item.name === target.files[0].name) //if you're readding the same file, return
        if(file.length !== 0) {
            return;
        }

        setFiles(prevFiles => [...prevFiles, target.files[0]]); //otherwise append it to the array
    }

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
        console.log('called')
        setCurrentFile(null);
        setDisplayModal(false)
    }

    const handleUpload = () => {
        if(files.length < 2) {
            return;
        }

        let filesFormData = new FormData();
        files.forEach(file => {
            filesFormData.append('files', file);
        })
            
        console.log('flesform', filesFormData.getAll('files'))
        props.onUpload(selectedTask.piId, selectedTask.taskId, filesFormData, history);

    }

    let modal = null;
    if(displayModal) {
        modal = <PreviewPDFModal file={currentFile} close={exitPreview}/>
    }

    return (
        <Container component="main" maxWidth="xs">
            {modal}
            <CssBaseline />
            <div className={classes.paper}>
            <Typography component="h1" variant="h4" className={classes.title}>Membership request</Typography>
                <Avatar className={classes.avatar}>
                    <PictureAsPdfIcon />
                </Avatar>
                <Typography component="h1" variant="h6" className={classes.title}>Add at least 2 of your best work</Typography>
                <br/>               
            <Grid container >
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
                <Grid item>
                <input type="file" accept="application/pdf" hidden id="writer-upload-files"
                            onChange={handleChooseFile} onClick={e => (e.target.value = null)}
                        />

                <label htmlFor="writer-upload-files">
                <Grid item  >
                    <Button color="primary" variant="contained" component="span">
                        Add file
                    </Button>
                </Grid>
                </label>
                </Grid>

                <Grid item>
                <Button m={1} color="primary" variant="contained" component="span"
                        startIcon={<PublishIcon />} 
                        onClick={handleUpload} 
                         disabled={files.length < 2 ? true : false}
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
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
        onUpload: (piId, taskId, files, history) => dispatch(actions.upload(piId, taskId, files, history))
    }
};


export default connect(mapStateToProps, mapDispatchToProps)(WriterUploadWork);