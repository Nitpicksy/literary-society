import React, { useEffect, useState } from 'react';
import { withStyles } from '@material-ui/core/styles';
import Dialog from '@material-ui/core/Dialog';
import MuiDialogTitle from '@material-ui/core/DialogTitle';
import MuiDialogContent from '@material-ui/core/DialogContent';
import MuiDialogActions from '@material-ui/core/DialogActions';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import { Grid } from '@material-ui/core'
import Typography from '@material-ui/core/Typography';
import KeyboardArrowLeftIcon from '@material-ui/icons/KeyboardArrowLeft';
import KeyboardArrowRightIcon from '@material-ui/icons/KeyboardArrowRight';
// using ES6 modules
import { Document, Page} from 'react-pdf/dist/esm/entry.webpack';

import { useStyles } from './PreviewPDFModalStyles';

const options = {
    cMapUrl: 'cmaps/',
    cMapPacked: true,
  };
  
const styles = (theme) => ({
  root: {
    margin: 0,
    padding: theme.spacing(2),
  },
  closeButton: {
    position: 'absolute',
    right: theme.spacing(1),
    top: theme.spacing(1),
    color: theme.palette.grey[500],
  },
});

const DialogTitle = withStyles(styles)((props) => {
  const { children, classes, onClose, ...other } = props;
  return (
    <MuiDialogTitle disableTypography className={classes.root} {...other}>
      <Typography variant="h6">{children}</Typography>
      {onClose ? (
        <IconButton aria-label="close" className={classes.closeButton} onClick={onClose}>
          <CloseIcon />
        </IconButton>
      ) : null}
    </MuiDialogTitle>
  );
});

const DialogContent = withStyles((theme) => ({
  root: {
    padding: theme.spacing(2),
  },
}))(MuiDialogContent);

const DialogActions = withStyles((theme) => ({
  root: {
    margin: 0,
    padding: theme.spacing(1),
  },
}))(MuiDialogActions);


const PreviewPDFModal = props => {

    const [open, setOpen] = React.useState(false);
    const [numPages, setNumPages] = useState(null);
    const [pageNumber, setPageNumber] = useState(1);

    const classes = useStyles();

    useEffect(() => {
        setOpen(true)
    }, [props])
    
    const handleClose = () => {
      setOpen(false);
      props.close();
    };

    const onDocumentLoadSuccess = ({ numPages: nextNumPages }) => {
        setNumPages(nextNumPages);
        setPageNumber(1);
    }

    const changePage = (offset) => {
        setPageNumber(prevPageNumber => prevPageNumber + offset);
      }

    const previousPage = () => {
        changePage(-1);
    }

    const nextPage = () => {
        changePage(1);
    }


    return (
        <div>
          <Dialog onClose={handleClose} aria-labelledby="customized-dialog-title" open={open}
            maxWidth = {'md'}>
            <DialogTitle id="customized-dialog-title" onClose={handleClose}>
              Document preview
            </DialogTitle>
            <DialogContent dividers>
            <div>
            <Typography component="h6" variant="h6" className={classes.title}>Page {pageNumber || (numPages ? 1 : '--')} of {numPages || '--'}</Typography>
            <Grid 
                container 
                alignItems="center"
                justify="center" 
                spacing={0}
            >
                <Grid item>
                    <IconButton aria-label="left" 
                        disabled={pageNumber <= 1}
                        onClick={previousPage}
                    >
                    <KeyboardArrowLeftIcon />
                    </IconButton>
                </Grid>
                <Grid item>
                    <IconButton aria-label="right"
                    disabled={pageNumber >= numPages}
                    onClick={nextPage}
                    >
                    <KeyboardArrowRightIcon />
                    </IconButton>
                </Grid>
            </Grid>
             </div>
                <Document
                file={props.file}
                onLoadSuccess={onDocumentLoadSuccess}
                options={options}
                >
                <Page scale={1}
                pageNumber={pageNumber}
                />
            </Document>
            </DialogContent>
            <DialogActions>
              {/* <Button autoFocus onClick={handleClose} color="primary">
                Save changes
              </Button> */}
            </DialogActions>
          </Dialog>
        </div>
      );
}

export default PreviewPDFModal;

