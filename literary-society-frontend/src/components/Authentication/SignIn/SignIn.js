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
import {checkValidity} from '../../../shared/utility';

const SignIn = (props) => {

    const classes = useStyles();

    const [controls, setControls] = useState({
        username: {
            elementType: 'input',
            elementConfig: {
                type: 'text',
                label: 'Username'
            },
            value: '',
            validation: {
                required: true
            },
            valid: false,
            touched: false,
            errorMessage:'Username is required', 
            error: false
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
                minLength: 10,
                isPassword:true
            },
            valid: false,
            touched: false,
            errorMessage:"Password is required",
            error:false
        }
    })

    const inputChangedHandler = (event, controlName) => {
        const validData = checkValidity(event.target.value, controls[controlName].validation, controls[controlName].elementConfig.label);
        let error = !validData.isValid;
        console.log(error)

        console.log(validData.errorMessage)
        const updatedControls = {
            ...controls,
            [controlName]: {
                ...controls[controlName],
                value: event.target.value,
                valid: validData.isValid,
                touched: true,
                error:error
            }
        };
        console.log(validData.errorMessage)
        setControls(updatedControls);
    }

    const submitHander = (event) => {
        console.log("Log in");
        console.log(controls);
        
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
            errorMessage = {formElement.config.errorMessage}
            error = {formElement.config.error}
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
                    {/* <Typography id="errorText" variant="body2" className={classes.errorText}>hshs</Typography> */}
                    <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained">Sign In</Button>
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