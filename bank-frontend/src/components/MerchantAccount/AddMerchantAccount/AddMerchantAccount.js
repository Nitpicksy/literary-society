import React, { useState } from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import * as actions from './AddMerchantAccountExport';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './AddMerchantAccountStyles';
import Form from '../../../UI/Form/Form';
import { connect } from 'react-redux';
import {  useHistory } from 'react-router';

const AddMerchantAccount = (props) => {
    const history = useHistory();

    const classes = useStyles();
    const [formIsValid, setFormIsValid] = useState(false);

    const [controls, setControls] = useState({
        name: {
            elementType: 'input',
            elementConfig: {
                label: 'Name'
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
        merchantPassword: {
            elementType: 'input',
            elementConfig: {
                type: 'password',
                label: 'Merchant password'
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
        props.onAddMerchant(controls.name.value, controls.merchantPassword.value,
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
                <Typography component="h1" variant="h4">Create merchant</Typography>
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
        onAddMerchant: (name, merchantPassword,city,country,email,balance,history) => 
        dispatch(actions.addMerchant(name, merchantPassword,city,country,email,balance,history)),
    }
};

export default connect(null, mapDispatchToProps)(AddMerchantAccount);