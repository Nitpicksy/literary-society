import React, { useState } from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';

import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './SignInStyles';
import Input from '../../../UI/Input/Input';
import { checkValidity } from '../../../shared/utility';

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
            valid: true,
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
                isPassword: true
            },
            valid: true,
            touched: false,
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
        //     valid: false,
        //     touched: false,
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
        //     valid: false,
        //     touched: false,
        //     error: false,
        //     errorMessage: '',
        // },
    })

    const inputChangedHandler = (event, controlName) => {
        const validationData = checkValidity(event.target.value, controls[controlName].validation, controls[controlName].elementConfig.label);

        const updatedControls = {
            ...controls,
            [controlName]: {
                ...controls[controlName],
                value: event.target.value,
                valid: validationData.isValid,
                touched: true,
                error: !validationData.isValid,
                errorMessage: validationData.errorMessage,
            }
        };

        setControls(updatedControls);
    }

    const submitHander = (event) => {
        event.preventDefault();
        console.log("Log in");
        console.log(controls.username.value, controls.password.value);
    }

    const formElementsArray = [];
    for (let key in controls) {
        formElementsArray.push({
            id: key,
            config: controls[key]
        });
    }

    var form = formElementsArray.map(formElement => (
        <Input
            key={formElement.id}
            elementType={formElement.config.elementType}
            elementConfig={formElement.config.elementConfig}
            value={formElement.config.value}
            invalid={!formElement.config.valid}
            shouldValidate={formElement.config.validation}
            touched={formElement.config.touched}
            error={formElement.config.error}
            errorMessage={formElement.config.errorMessage}
            changed={(event) => inputChangedHandler(event, formElement.id)} />
    ));


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
                    {form}
                    {/* <Typography id="errorText" variant="body2" className={classes.errorText}></Typography> */}
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

export default SignIn;