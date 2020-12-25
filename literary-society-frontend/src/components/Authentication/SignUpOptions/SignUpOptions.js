import React, { useEffect } from 'react';
import * as signInActions from '../SignIn/SignInExport';
import * as actions from './SignUpOptionsExport';
import { connect } from 'react-redux';
import { Avatar, CssBaseline, Typography, Container, Grid } from '@material-ui/core';
import { useStyles } from './SignUpOptionsStyles';
import PersonIcon from '@material-ui/icons/Person';
import SignUpOptionCard from '../SignUpOptionCard/SignUpOptionCard';

const SignUpOptions = (props) => {

    const classes = useStyles();
    const { clearProcessState } = props;

    useEffect(() => {
        clearProcessState();
    }, [clearProcessState]);

    return (
        <Container component="main" maxWidth="md">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <PersonIcon />
                </Avatar>
                <Typography className={classes.title} component="h1" variant="h4">Sign up Options</Typography>
                <Typography className={classes.paragraph} component="h2" variant="body1">What type of user you want to be?</Typography>
                <Grid container spacing={3} align="center">
                    <SignUpOptionCard title="Reader" path="/sign-up" type="readers" smColumns={4} xsColumns={12}
                        description="Readers can purchase and download books. Additionally, beta-readers can review writers' manuscripts." />
                    <SignUpOptionCard title="Writer" path="/sign-up" type="writers" smColumns={4} xsColumns={12}
                        description="Writers can upload their manuscripts to get a review. Manuscripts with positive reviews can be published." />
                    <SignUpOptionCard title="Editor" smColumns={4} xsColumns={12}
                        description="Editors can review writers' manuscripts. They can ask writers to improve their manuscripts if it is necessary." />
                    <SignUpOptionCard title="Lecturer" smColumns={6} xsColumns={12}
                        description="Lecturers can download manuscripts to review and mark lexical errors made by writers and then upload them back." />
                    <SignUpOptionCard title="Merchant" smColumns={6} xsColumns={12}
                        description="Merchants can advertise books they sell and receive online payments for sold books." />
                </Grid>
            </div>
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        signUpType: state.signUpOptions.signUpType,
        processInstanceId: state.signUpOptions.processInstanceId,
        taskId: state.signUpOptions.taskId,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        clearProcessState: () => dispatch(actions.clearProcessState()),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(SignUpOptions);