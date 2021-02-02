import { connect } from "react-redux";

import React from 'react';
import { Avatar, Button, Container, CssBaseline, Typography } from "@material-ui/core";
import { useStyles } from './DocumentSubmissionStyles';
import AttachFileRounded from '@material-ui/icons/AttachFileRounded';

const DocumentSubmission = (props) => {
    const classes = useStyles();
    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
            <Typography component="h1" variant="h4" className={classes.title}>Membership request</Typography>
                <Avatar className={classes.avatar}>
                    <AttachFileRounded />
                </Avatar>
                <Typography component="h1" variant="h6" className={classes.title}>Add at least 2 of your best work</Typography>
                <br/>
                <Button variant="contained" component="label"> 
                Submit
                    <input type="file" hidden />
                </Button>
            </div>
        </Container>
        
    );
}



export default connect(null,null)(DocumentSubmission);