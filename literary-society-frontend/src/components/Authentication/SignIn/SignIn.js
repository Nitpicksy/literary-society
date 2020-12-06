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

const SignIn = (props) => {
    const classes = useStyles();

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
                isPassword: true
            },
            error: false,
            errorMessage: '',
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

    // let authredirect = null;
    // if (props.isAuthenticated) {
    //     authredirect = <Redirect to={props.authRedirect} />
    // }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <LockOutlinedIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Sign in</Typography>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    <Form controls={controls} setControls={setControls} />
                    <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained"
                        onClick={submitHander}>Sign In</Button>
                    <Grid container>
                        <Grid item xs>
                            <Link href="#" variant="body2">Forgot password?</Link>
                        </Grid>
                        <Grid item>
                            <Link href="#" variant="body2">Don't have an account? Sign Up</Link>
                        </Grid>
                    </Grid>
                </form>
            </div>
        </Container>
    );
};

const mapDispatchToProps = dispatch => {
    return {
        onSignIn: (email, password) => dispatch(actions.signIn(email, password))
    }
};

export default connect(null, mapDispatchToProps)(SignIn);