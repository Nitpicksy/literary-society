import React, { useEffect, useState } from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import * as actions from './LecturerAndEditorSignUpExport';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './LecturerAndEditorSignUpStyles';
import Form from '../../../UI/Form/Form';
import { connect } from 'react-redux';
import { useHistory } from 'react-router';
import { toastr } from 'react-redux-toastr';
import PersonIcon from '@material-ui/icons/Person';

const LecturerAndEditorSignUp = (props) => {
    const history = useHistory();
    
    const classes = useStyles();
    const [formIsValid,setFormIsValid] = useState(false);

    const [controls, setControls] = useState({
        firstName: {
            elementType: 'input',
            elementConfig: {
                label: 'First Name'
            },
            value: '',
            validation: {
                required: true,
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
        },
        lastName: {
            elementType: 'input',
            elementConfig: {
                label: 'Last Name'
            },
            value: '',
            validation: {
                required: true,
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
        },
        city: {
            elementType: 'input',
            elementConfig: {
                label: 'City'
            },
            value: '',
            validation: {
                required: true,
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
        },
        country: {
            elementType: 'input',
            elementConfig: {
                label: 'Country'
            },
            value: '',
            validation: {
                required: true,
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
        },
        email: {
            elementType: 'input',
            elementConfig: {
                label: 'Email'
            },
            value: '',
            validation: {
                required: true,
                pattern: '^[a-zA-Z0-9_+&*-]+(?:.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+.)+[a-zA-Z]{2,7}$'
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: 'Email is not valid'
            }

        },
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
        repeatedPassword: {
            elementType: 'input',
            elementConfig: {
                type: 'password',
                label: 'Repeated password'
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
                errorMessage: 'Repeated password should contain between 10 and 64 characters including 1 number, 1 special character, 1 lowercase and 1 uppercase letter.'
            }
        }
    })

    useEffect(() => {
        if (props.signUpType === "readers") {
            history.push("/sign-up-options")
        }
    }, [props.signUpType]);
    
    const submitHander = (event) => {
        event.preventDefault();
        if(controls.password.value !== controls.repeatedPassword.value){
            toastr.error("Sign Up","Password and repeated password don't match");
        }else{
            props.onSignUp(props.signUpType, controls.firstName.value,controls.lastName.value,controls.city.value,controls.country.value,controls.email.value,
                controls.username.value, controls.password.value,controls.repeatedPassword.value,history);
        }
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <PersonIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Sign up</Typography>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    <Form controls={controls} setControls={setControls} setFormIsValid= {setFormIsValid}/>
                    <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained"
                        onClick={submitHander} disabled={!formIsValid}>Sign In</Button>
                    <Grid container>
                        <Grid item xs>
                            <Link href="/forgot-password" variant="body2">Forgot password?</Link>
                        </Grid>
                        <Grid item>
                            <Link href="/sign-up-options" variant="body2">Don't have an account? Sign Up</Link>
                        </Grid>
                    </Grid>
                </form>
            </div>
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        signUpType: state.signUpOptions.signUpType,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onSignUp: (signUpType,firstName,lastName,city,country,email,username, password,repeatedPassword,history) =>
         dispatch(actions.signUp(signUpType,firstName,lastName,city,country,email,username, password,repeatedPassword,history))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(LecturerAndEditorSignUp);