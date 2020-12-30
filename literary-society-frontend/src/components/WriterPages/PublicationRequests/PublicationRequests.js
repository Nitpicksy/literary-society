import React, { useEffect } from 'react';
import DescriptionIcon from '@material-ui/icons/Description';
import AddIcon from '@material-ui/icons/Add';
import { Container, CssBaseline, Grid, Typography, Button, Avatar } from '@material-ui/core/';
import { useStyles } from './PublicationRequestsStyles';
import { useHistory } from 'react-router';
import { connect } from 'react-redux';
import * as actions from './PublicationRequestsExport';
import * as signInActions from '../../Authentication/SignIn/SignInExport';
import { responseInterceptor } from '../../../responseInterceptor';

const PublicationRequests = (props) => {

    const classes = useStyles();
    const history = useHistory();
    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);
    const { clearProcessState } = props;

    useEffect(() => {
        clearProcessState();
    }, [clearProcessState]);

    const showForm = () => {
        props.startProcess();
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

const mapStateToProps = state => {
    return {
        processInstanceId: state.publRequests.processInstanceId,
        taskId: state.publRequests.taskId,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        startProcess: () => dispatch(actions.startProcess()),
        clearProcessState: () => dispatch(actions.clearProcessState()),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PublicationRequests);