import React, { useState } from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import * as actions from './SignInExport';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './SignInStyles';
import Form from '../../../UI/Form/Form';
import { connect } from 'react-redux';
import { Redirect, useHistory } from 'react-router';
import responseInterceptor from '../../../responseInterceptor';

const SignIn = (props) => {
    const history = useHistory();
    responseInterceptor.setupInterceptor(history);
    
    const classes = useStyles();
    const [formIsValid,setFormIsValid] = useState(false);

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
        },
        // textarea: {
        //     elementType: 'textarea',
        //     elementConfig: {
        //         label: 'Comment',
        //         rows: 4,
        //     },
        //     value: '',
        //     validation: {
        //         required: true,
        //     },
        //     error: false,
        //     errorMessage: '',
        // },
        // select: {
        //     elementType: 'select',
        //     elementConfig: {
        //         label: 'Products',
        //         options: [
        //             {
        //                 value: "mleko",
        //                 displayValue: "Mleko"
        //             },
        //             {
        //                 value: "jogurt",
        //                 displayValue: "Jogurt"
        //             }
        //         ],
        //     },
        //     value: '',
        //     validation: {
        //         required: true,
        //         isUsername: true,
        //     },
        //     error: false,
        //     errorMessage: '',
        // },
    })

    
    const submitHander = (event) => {
        event.preventDefault();
        props.onSignIn(controls.username.value, controls.password.value);
    }

    let authredirect = null;
    if (props.authRedirectPath) {
        console.log(props.authRedirectPath)
        authredirect = <Redirect to={props.authRedirectPath} />
    }

    return (
        <Container component="main" maxWidth="xs">
            {authredirect}
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <LockOutlinedIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Sign in</Typography>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    <Form controls={controls} setControls={setControls} setFormIsValid= {setFormIsValid}/>
                    <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained"
                        onClick={submitHander} disabled={!formIsValid}>Sign In</Button>
                    <Grid container>
                        <Grid item xs>
                            <Link href="/forgot-password" variant="body2">Forgot password?</Link>
                        </Grid>
                        <Grid item>
                            <Link href="/sign-up" variant="body2">Don't have an account? Sign Up</Link>
                        </Grid>
                    </Grid>
                </form>
            </div>
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        isAuthenticated: state.signIn.isAuthenticated,
        authRedirectPath: state.signIn.authRedirectPath
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onSignIn: (username, password) => dispatch(actions.signIn(username, password))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(SignIn);