import React, { useEffect, useState } from 'react';
import * as actions from './SignUpExport';
import * as signInActions from '../SignIn/SignInExport';
import { extractControls } from '../../../utility/extractControls';
import Form from '../../../UI/Form/Form';
import CssBaseline from '@material-ui/core/CssBaseline';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import PersonIcon from '@material-ui/icons/Person';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import { useStyles } from './SignUpStyles';
import { connect } from 'react-redux';
import { responseInterceptor } from '../../../responseInterceptor';
import { useHistory } from 'react-router';

const SignUp = (props) => {

    const history = useHistory();
    const classes = useStyles();
    const { formFields, fetchForm } = props;
    let form = null;
    let [controls, setControls] = useState(null);
    const [formIsValid, setFormIsValid] = useState(false);

    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);

    useEffect(() => {
        console.log(props.processInstanceId, props.taskId, props.signUpType)
        if (props.processInstanceId && props.taskId && props.signUpType) {
            fetchForm(props.processInstanceId, props.taskId, props.signUpType);
        }
    }, [fetchForm, props.processInstanceId, props.taskId, props.signUpType]);

    useEffect(() => {
        if (formFields) {
            let extractedControls = extractControls(formFields)
            setControls(extractedControls);
        }
    }, [formFields]);

    const submitHander = (event) => {
        event.preventDefault();
        let array = [];
        for (let [key, data] of Object.entries(controls)) {
            // convert array to comma-separated string
            let value = data.value
            if (Array.isArray(data.value)) {
                value = data.value.join();
            }
            array.push({ fieldId: key, fieldValue: value });
        }

        if (props.signUpType === 'readers') {
            props.onSignUp(array, props.taskId, history, controls['isBetaReader'].value);
        }
        else {
            props.onSignUp(array, props.taskId, history, false);
        }

    }

    if (controls) {
        form = <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />;
    }


    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <PersonIcon />
                </Avatar>
                <Typography component="h1" variant="h4" className={classes.title}>Sign up</Typography>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    {form}
                    <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained"
                        disabled={!formIsValid}>Sign up</Button>
                    <Grid container justify="center">
                        <Grid item>
                            <Link href="/sign-in" variant="body2">Already have an account? Sign in</Link>
                        </Grid>
                    </Grid>
                </form>
            </div>
        </Container>
    );
};


const mapStateToProps = state => {
    return {
        formFields: state.signUp.formFields,
        signUpType: state.signUpOptions.signUpType,
        processInstanceId: state.signUpOptions.processInstanceId,
        taskId: state.signUpOptions.taskId,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchForm: (piId, taskId, signUpType) => dispatch(actions.fetchForm(piId, taskId, signUpType)),
        onSignUp: (signUpData, taskId, history, isBetaReader) => dispatch(actions.signUp(signUpData, taskId, history, isBetaReader)),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(SignUp);