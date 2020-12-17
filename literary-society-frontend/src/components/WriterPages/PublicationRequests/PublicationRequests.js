import React from 'react';
import DescriptionIcon from '@material-ui/icons/Description';
import AddIcon from '@material-ui/icons/Add';
import { Container, CssBaseline, Grid, Typography, Button, Avatar } from '@material-ui/core/';
import { useStyles } from './PublicationRequestsStyles';
import { useHistory } from 'react-router';

const PublicationRequests = (props) => {

    const classes = useStyles();
    const history = useHistory();

    const showForm = () => {
        history.push('./create-publication-request');
    }

    return (
        <Container component="main" maxWidth="lg">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <DescriptionIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Publication Requests</Typography>
            </div>
            <Grid container spacing={3} align="center">
                <Button variant="contained" color="primary" startIcon={<AddIcon />} onClick={showForm}>
                    Publication Request
                </Button>
            </Grid>
        </Container>
    );
};

export default PublicationRequests;