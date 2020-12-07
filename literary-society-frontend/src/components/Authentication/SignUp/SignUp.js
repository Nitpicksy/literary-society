import React, { useEffect, useState } from 'react';
import * as actions from './SignUpExport';
import {extractControls} from '../../../shared/extractControls';
import Form from '../../../UI/Form/Form';
import CssBaseline from '@material-ui/core/CssBaseline';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import { useStyles } from '../SignIn/SignInStyles';
import { connect } from 'react-redux';

const SignUp = (props) => {
    const classes = useStyles();
    const {formFields,fetchForm} = props;

    let form = null;
    let [controls, setControls] = useState(null);

    useEffect(() => {
        fetchForm();
    }, [fetchForm]);

    useEffect(() => {
        if(formFields){
            let extractedControls = extractControls(formFields)
            setControls(extractedControls);
        }
       
    }, [formFields]);

    if(controls){
        form = <Form controls={controls} setControls={setControls} />;
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <LockOutlinedIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Sign up</Typography>
                <form className={classes.form} noValidate >
                    {form}
                    <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained"
                        >Sign up</Button>
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


const mapStateToProps = state => {
    return {
        formFields: state.signUp.formFields,
        processInstanceId: state.signUp.processInstanceId,
        taskId: state.signUp.taskId
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchForm: () => dispatch(actions.fetchForm()),

    }
};

export default connect(mapStateToProps, mapDispatchToProps)(SignUp);