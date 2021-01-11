import React, { useState } from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import * as actions from './ResetPasswordExport';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './ResetPasswordStyles';
import Form from '../../../UI/Form/Form';
import { connect } from 'react-redux';
import VpnKeyIcon from '@material-ui/icons/VpnKey';

const ResetPasswordEnterUsername = (props) => {
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
        }
    })


    const submitHander = (event) => {
        event.preventDefault();
        props.onRequestToken(controls.username.value);
    }

    let form = (
        <div className={classes.paper} >
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
    );
    if (props.confirmed) {
        form = (
            <div className={classes.paper} >
                <Avatar className={classes.avatar}>
                    <VpnKeyIcon />
                </Avatar>
                <Typography component="h1" variant="h4">E-mail sent!</Typography>
                <Typography component="h2"> Check your inbox for the one-time reset link. Link will be active for the next 45 minutes. </Typography>
            </div>
        );
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            {form}
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        confirmed: state.resetPassword.confirmed
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onRequestToken: (username) => dispatch(actions.requestToken(username))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ResetPasswordEnterUsername);