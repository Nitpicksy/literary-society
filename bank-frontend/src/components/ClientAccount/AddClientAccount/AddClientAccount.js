import React, { useState } from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import * as actions from './AddClientAccountExport';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './AddClientAccountStyles';
import Form from '../../../UI/Form/Form';
import { connect } from 'react-redux';
import {  useHistory } from 'react-router';

const AddClientAccount = (props) => {
    const history = useHistory();

    const classes = useStyles();
    const [formIsValid, setFormIsValid] = useState(false);

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
                label: 'Email',
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
                errorMessage: 'Invalid e-mail address entered.',
            }
        },
        balance: {
            elementType: 'input',
            elementConfig: {
                label: 'Balance'
            },
            value: null,
            validation: {
                isNumeric: true
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: 'Invalid balance entered.',
            }
        },
    })


    const submitHander = (event) => {
        event.preventDefault();
        props.onAddClient(controls.firstName.value, controls.lastName.value,
            controls.city.value,controls.country.value,
            controls.email.value,controls.balance.value,history);
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <LockOutlinedIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Create client account</Typography>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />
                    <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained"
                        onClick={submitHander} disabled={!formIsValid}>Submit</Button>
                </form>
            </div>
        </Container>
    );
};


const mapDispatchToProps = dispatch => {
    return {
        onAddClient: (firstName, lastName,city,country,email,balance,history) => 
        dispatch(actions.addClient(firstName, lastName,city,country,email,balance,history)),
    }
};

export default connect(null, mapDispatchToProps)(AddClientAccount);