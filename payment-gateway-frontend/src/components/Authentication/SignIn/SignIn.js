import React, { useState } from 'react';
import {Avatar, Button, CssBaseline, Typography, Container, Paper} from '@material-ui/core';
import * as actions from './SignInExport';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import { useStyles } from './SignInStyles';
import Form from '../../../UI/Form/Form';
import { connect } from 'react-redux';
import { Redirect, useHistory } from 'react-router';
import { responseInterceptor } from '../../../responseInterceptor';

const SignIn = (props) => {
    const history = useHistory();
    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);

    const classes = useStyles();
    const [formIsValid, setFormIsValid] = useState(false);

    const [controls, setControls] = useState({
        username: {
            elementType: 'input',
            elementConfig: {
                label: 'Username'
            },
            value: '',
            validation: {
                required: true,
                isUsername: true,
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
        },
        password: {
            elementType: 'input',
            elementConfig: {
                type: 'password',
                label: 'Password'
            },
            value: '',
            validation: {
                required: true,
                pattern: '^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[_#?!@$%^&*-.,:;]).{10,64}$'
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: 'Password should contain between 10 and 64 characters including 1 number, 1 special character, 1 lowercase and 1 uppercase letter.'
            }
        }
    })


    const submitHander = (event) => {
        event.preventDefault();
        props.onSignIn(controls.username.value, controls.password.value);
    }

    let authredirect = null;
    if (props.authRedirectPath) {
        authredirect = <Redirect to={props.authRedirectPath} />
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            {authredirect}
            <Paper className={classes.mainPaper}>
                <div className={classes.centered}>
                    <Avatar className={classes.avatar}>
                        <LockOutlinedIcon />
                    </Avatar>
                    <Typography component="h1" variant="h4">Sign in</Typography>
                </div>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />
                    <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained"
                        onClick={submitHander} disabled={!formIsValid}>Sign In</Button>
                </form>
            </Paper>
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        isAuthenticated: state.signIn.isAuthenticated,
        authRedirectPath: state.signIn.authRedirectPath,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onSignIn: (username, password) => dispatch(actions.signIn(username, password)),
        onRefreshToken: (history) => dispatch(actions.refreshToken(history))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(SignIn);