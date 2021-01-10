import React from 'react';
import { Typography, Card, CardContent, Button } from '@material-ui/core';
import { useStyles } from './BookDetailsCardStyles';
import GetAppIcon from '@material-ui/icons/GetApp';

const BookDetailsCard = (props) => {

    const classes = useStyles();
    const { bookDetails } = props;

    return (
        <Card className={classes.cardContent}>
        <br/>
        <Typography component="h5" variant="h5" className={classes.title}>Submitted book</Typography>
            <CardContent>
                        <div>
                        <Typography variant="body1"><i> Title</i>: {bookDetails.title}</Typography>
                        <Typography variant="body1"><i> Genre</i>: <strong> {bookDetails.genre} </strong></Typography>
                        <Typography variant="body1"><i> Synopsis</i>: <strong>{bookDetails.synopsis.length > 150 ? 
                        bookDetails.synopsis.substring(0,150) + '...' : bookDetails.synopsis} </strong></Typography>
                        <br/>
                {props.showDownload &&   <Button color="primary" className={classes.submit} variant="contained"
                                                startIcon={<GetAppIcon />} onClick={() => props.download(bookDetails.title, 'SUBMITTED')}>
                                                Download
                                             </Button>}
                        </div>
            </CardContent>
        </Card>  
    )
}

export default BookDetailsCard;