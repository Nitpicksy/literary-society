import React, { useEffect, useState } from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import * as actions from './EnterCreditCardDataExport';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './EnterCreditCardDataStyles';
import Form from '../../UI/Form/Form';
import { useHistory } from 'react-router';
import CreditCardIcon from '@material-ui/icons/CreditCard';
import { connect } from 'react-redux';

const EnterCreditData = (props) => {

    const classes = useStyles();
    const [formIsValid, setFormIsValid] = useState(false);
    const history = useHistory();
    const [paymentId, setPaymentId] = useState(false);

    const [controls, setControls] = useState({
        pan: {
            elementType: 'input',
            elementConfig: {
                type: 'password',
                label: 'PAN',
            },
            value: '',
            validation: {
                required: true,
                pattern: '^[0-9]{16}$'
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: 'Invalid PAN number.'
            }
        },
        cardHolderName: {
            elementType: 'input',
            elementConfig: {
                type: 'text',
                label: 'Card Holder Name'
            },
            value: '',
            validation: {
                required: true,
                pattern: "^(([A-Za-zÀ-ƒ]+[.]?[ ]?|[a-zÀ-ƒ]+['-]?){0,4})$"
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: 'Invalid Card Holder Name'
            }, 
            toUpperCase: true
        },
        expirationDate: {
            elementType: 'input',
            elementConfig: {
                type: 'text',
                label: 'Expiration date',
            },
            value: '',
            validation: {
                required: true,
                pattern: "^([01]?[0-9]?(\/)[0-9]{2})$"
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: 'You need to enter month/year'
            }
        },
        securityCode: {
            elementType: 'input',
            elementConfig: {
                type: 'password',
                label: 'Security Code'
            },
            value: '',
            validation: {
                required: true,
                pattern: '^[0-9]{3,4}$'
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: 'Security code should contain only numbers.'
            }
        },
    })


    const submitHander = (event) => {
        event.preventDefault();
        props.onConfirmPayment(controls.pan.value, controls.securityCode.value,
            controls.cardHolderName.value,controls.expirationDate.value,paymentId, history);
    }

    useEffect(() => {
        const { id } = props.match.params;
        setPaymentId(id);
    }, [props.match.params]);

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <CreditCardIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Credit Card Data</Typography>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />
                    <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained"
                        onClick={submitHander} disabled={!formIsValid}>Confirm purchase</Button>
                </form>
            </div>
        </Container>
    );
};

const mapDispatchToProps = dispatch => {
    return {
        onConfirmPayment: (pan, securityCode,cardHolderName,expirationDate,paymentId, history) => 
            dispatch(actions.confirmPayment(pan, securityCode,cardHolderName,expirationDate,paymentId, history))
    }
};

export default connect(null, mapDispatchToProps)(EnterCreditData);
