import React, { useState } from 'react';
import { Avatar, Button, CssBaseline, Typography, Container, TextField } from '@material-ui/core';
import BusinessIcon from '@material-ui/icons/Business';
import Form from '../../../UI/Form/Form';
import { useStyles } from './AddCompanyStyles';
import { connect } from 'react-redux';
import { Redirect, useHistory } from 'react-router';

const AddCompany = (props) => {
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
        websiteURL: {
            elementType: 'input',
            elementConfig: {
                label: 'Website URL'
            },
            value: '',
            validation: {
                required: true,
                pattern: '^(http(s)?:\\/\\/)?((www\\.)|(localhost:))[(\\/)?a-zA-Z0-9@:%._\\+~#=-]{1,256}$'
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: 'Website URL is not valid',
        },
        commonName: {
            elementType: 'input',
            elementConfig: {
                label: 'Common Name'
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
        successURL: {
            elementType: 'input',
            elementConfig: {
                label: 'Success URL'
            },
            value: '',
            validation: {
                required: true,
                pattern: '^(http(s)?:\\/\\/)?((www\\.)|(localhost:))[(\\/)?a-zA-Z0-9@:%._\\+~#=-]{1,256}$'
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: 'Success URL is not valid',
        },
        failedURL: {
            elementType: 'input',
            elementConfig: {
                label: 'Failed URL'
            },
            value: '',
            validation: {
                required: true,
                pattern: '^(http(s)?:\\/\\/)?((www\\.)|(localhost:))[(\\/)?a-zA-Z0-9@:%._\\+~#=-]{1,256}$'
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: 'Failed URL is not valid',
        },
        errorURL: {
            elementType: 'input',
            elementConfig: {
                label: 'Error URL'
            },
            value: '',
            validation: {
                required: true,
                pattern: '^(http(s)?:\\/\\/)?((www\\.)|(localhost:))[(\\/)?a-zA-Z0-9@:%._\\+~#=-]{1,256}$'
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: 'Error URL is not valid',
        },
    });

    const textInputChangedHandler = (event) => {
        let errorMessage;
        let value = event.target.value;
        console.log(event)
        // if (props.controls[controlName].elementType === 'checkbox') {
        //     value = event.target.checked;
        // } else {
        //     if (props.controls[controlName].additionalData) {
        //         errorMessage = props.controls[controlName].additionalData.errorMessage
        //     }
        // }
        // let controlToValidate = props.controls[controlName];
        // const validationData = checkValidity(controlToValidate.elementConfig.label, value, controlToValidate.validation, controlToValidate.elementType, errorMessage);

        // const updatedControls = {
        //     ...props.controls,
        //     [controlName]: {
        //         ...props.controls[controlName],
        //         value: value,
        //         error: !validationData.isValid,
        //         errorMessage: validationData.errorMessage,
        //         touched: true,
        //         valid: validationData.isValid
        //     }
        // };

        // let formIsValid = true;
        // for (let inputId in updatedControls) {
        //     formIsValid = updatedControls[inputId].valid && formIsValid;
        // }
        // props.setControls(updatedControls);
        // props.setFormIsValid(formIsValid);
    }


    const submitHander = (event) => {
        event.preventDefault();
        //proveri da li je uneo barem jedan paymentData
        //posalji i podatke sa form i sa formData
        // props.onSignIn(controls.username.value, controls.password.value);
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <BusinessIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Add your Company</Typography>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    {/* <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} /> */}
                    <TextField margin="normal" fullWidth id='websiteURL'
                        error={props.error} helperText={props.error ? props.errorMessage : ''}
                        label='Website URL' onChange={(event) => textInputChangedHandler(event)} />
                    <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained"
                        onClick={submitHander} disabled={!formIsValid}>Submit</Button>
                </form>
            </div>
        </Container>
    );
};

// const mapStateToProps = state => {
//     return {
//         authRedirectPath: state.signIn.authRedirectPath,
//     }
// };

// const mapDispatchToProps = dispatch => {
//     return {
//         onSignIn: (username, password) => dispatch(actions.signIn(username, password)),
//         onRefreshToken: (history) => dispatch(actions.refreshToken(history))
//     }
// };

// export default connect(mapStateToProps, mapDispatchToProps)(SignIn);
export default AddCompany;