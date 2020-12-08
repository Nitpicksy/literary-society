import React, { useState } from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import * as actions from './ChangePasswordExport';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './ChangePasswordStyles';
import Form from '../../../UI/Form/Form';
import { connect } from 'react-redux';
import { useHistory } from 'react-router';
import VpnKeyIcon from '@material-ui/icons/VpnKey';

const ChangePassword = (props) => {

    const classes = useStyles();
    const [formIsValid, setFormIsValid] = useState(false);
    const history = useHistory();

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
        oldPassword: {
            elementType: 'input',
            elementConfig: {
                type: 'password',
                label: 'Old password'
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
        newPassword: {
            elementType: 'input',
            elementConfig: {
                type: 'password',
                label: 'New password'
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
                errorMessage: 'Password should contain between 10 and 64 characters including 1 number, 1 special character, 1 lowercase and 1 uppercase letter.'
            }
        },
    })


    const submitHander = (event) => {
        event.preventDefault();
        props.onChangePassword(controls.username.value, controls.oldPassword.value,
            controls.newPassword.value,controls.repeatedPassword.value, history);
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <VpnKeyIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Change password</Typography>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />
                    <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained"
                        onClick={submitHander} disabled={!formIsValid}>Save changes</Button>
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
        onChangePassword: (username, oldPassword,newPassword,repeatedPassword, history) => 
            dispatch(actions.changePassword(username, oldPassword,newPassword,repeatedPassword,history))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ChangePassword);