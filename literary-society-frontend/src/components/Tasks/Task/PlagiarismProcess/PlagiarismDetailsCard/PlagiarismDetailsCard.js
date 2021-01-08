import React from 'react';
import { Typography, Card, CardContent } from '@material-ui/core';
import { useStyles } from './PlagiarismDetailsCardStyles';

const PlagiarismDetailsCard = (props) => {

    const classes = useStyles();
    const { plagiarismDetails } = props;

    return (
        <Card className={classes.cardContent}>
        <br/>
        <Typography component="h5" variant="h5" className={classes.title}>Overview</Typography>
            <CardContent>
                        <div>
                        <Typography variant="body1"><i> Main editor</i>: {plagiarismDetails.mainEditor}</Typography>
                        <Typography variant="body1"><i> Potential plagiarism - book title</i>: <strong> {plagiarismDetails.title} </strong></Typography>
                        <Typography variant="body1"><i> Potential plagiarism - writers' name</i>: <strong>{plagiarismDetails.writerName} </strong></Typography>
                        </div>
            </CardContent>
        </Card>  
    )
}

export default PlagiarismDetailsCard;