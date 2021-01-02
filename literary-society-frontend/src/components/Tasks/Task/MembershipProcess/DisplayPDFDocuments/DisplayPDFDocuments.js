import React, { useState } from 'react';
import { useStyles } from './DisplayPDFDocumentsStyles';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import IconButton from '@material-ui/core/IconButton';
import VisibilityIcon from '@material-ui/icons/Visibility';
import PreviewPDFModal from '../PreviewPDFModal/PreviewPDFModal';

const DisplayPDFDocuments = (props) => {
    const classes = useStyles();

    const { files } = props;

    const [currentFile, setCurrentFile] = useState(null);
    const [displayModal, setDisplayModal] = useState(false);

    const previewDocument = (file) => {
        setCurrentFile(file);
        setDisplayModal(true);
    }

    const exitPreview = () => {
        setCurrentFile(null);
        setDisplayModal(false)
    }

    let modal = null;

    if(displayModal) {
        modal = <PreviewPDFModal file={"data:application/pdf;base64," + currentFile.encodedData} close={exitPreview}/>
    }

    return (
        <Grid container
        alignItems="center"
        justify="center" 
        spacing={2} >
        {modal}
        <Grid item className={classes.fileNameGrid}>
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
            </Typography>
            <br/>
            </div>)
        })}
        </Grid>
    </Grid>
    )
}

export default DisplayPDFDocuments;