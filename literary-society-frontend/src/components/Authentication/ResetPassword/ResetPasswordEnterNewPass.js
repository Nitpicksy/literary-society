import React, { useEffect, useState } from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import * as actions from './ResetPasswordExport';
import VpnKeyIcon from '@material-ui/icons/VpnKey';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './ResetPasswordStyles';
import Form from '../../../UI/Form/Form';
import { connect } from 'react-redux';
import { useHistory } from 'react-router';

const ResetPasswordEnterNewPass = (props) => {
    const classes = useStyles();
    const [formIsValid, setFormIsValid] = useState(false);
    const [token, setToken] = useState(false);
    const history = useHistory();

    const [controls, setControls] = useState({
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
        }
    })

    useEffect(() => {
        const params = new URLSearchParams(props.location.search);
        setToken(params.get('t'));
    }, []);

    const submitHander = (event) => {
        event.preventDefault();
        console.log(token)
        props.onResetPassword(token, controls.newPassword.value, controls.repeatedPassword.value, history);
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <VpnKeyIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Reset password</Typography>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />
                    <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained"
                        onClick={submitHander} disabled={!formIsValid}>Confirm</Button>
                </form>
            </div>
        </Container>
    );
};


const mapDispatchToProps = dispatch => {
    return {
        onResetPassword: (token, newPassword, repeatedPassword, history) =>
            dispatch(actions.resetPassword(token, newPassword, repeatedPassword, history))
    }
};

export default connect(null, mapDispatchToProps)(ResetPasswordEnterNewPass);