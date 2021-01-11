import React from 'react';
import { Typography, Card, CardContent } from '@material-ui/core';
import { useStyles } from './CommitteeCommentsStyles';

const CommitteeComments = (props) => {

    const classes = useStyles();
    const { comments } = props;

    return (
        <Card className={classes.cardContent}>
        <Typography component="h5" variant="h5" className={classes.title}>Committee feedback</Typography>
            <CardContent>
                {comments.map((comment, index) => {
                    return (
                        <div key={index + comment.comment}>
                         {comment.comment && <Typography component="h6" variant="h6"><i> {comment.committeeUsername}</i>: {comment.comment}</Typography> }
                        </div>
                    )
                })}
            </CardContent>
        </Card>  
    )
}

export default CommitteeComments;