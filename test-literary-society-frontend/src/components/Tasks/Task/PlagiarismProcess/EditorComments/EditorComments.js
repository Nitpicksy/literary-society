import React from 'react';
import { Typography, Card, CardContent } from '@material-ui/core';
import { useStyles } from './EditorCommentsStyles';

const EditorComments = (props) => {

    const classes = useStyles();
    const { comments } = props;

    return (
        <Card className={classes.cardContent}>
        <Typography component="h5" variant="h5" className={classes.title}>Editor reviews</Typography>
            <CardContent>
                {comments.map((comment, index) => {
                    return (
                        <div key={index + comment.review}>
                         <Typography component="body2" variant="body2"><i> {comment.editorName}</i></Typography>
                         <br/>

                         <Typography component="body2" variant="body2"><i> Review</i>: {comment.review}</Typography>
                         <hr/>
                        </div>
                    )
                })}
            </CardContent>
        </Card>  
    )
}

export default EditorComments;