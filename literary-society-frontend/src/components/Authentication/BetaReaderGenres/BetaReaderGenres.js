import React, { useEffect, useState } from 'react';
import * as actions from './BetaReaderGenresExport';
import { extractControls } from '../../../shared/extractControls';
import Form from '../../../UI/Form/Form';
import CssBaseline from '@material-ui/core/CssBaseline';
import PlaylistAddCheckIcon from '@material-ui/icons/PlaylistAddCheck';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import { useStyles } from './BetaReaderGenresStyles';
import { connect } from 'react-redux';
import { useHistory } from 'react-router';

const BetaReaderGenres = (props) => {
    const history = useHistory();
    const classes = useStyles();
    const { formFields, fetchForm } = props;
    let form = null;
    let [controls, setControls] = useState(null);
    const [formIsValid, setFormIsValid] = useState(false);

    useEffect(() => {
        console.log(props.processInstanceId)
        fetchForm(props.processInstanceId);
    }, [fetchForm,props.processInstanceId]);

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
            if(Array.isArray(data.value)) {
                value = data.value.join();
            }

            array.push({fieldId: key, fieldValue: value});
        }
        props.chooseGenres(array, props.taskId);
    }

    if (controls) {
        form = <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />;
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <PlaylistAddCheckIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Choose genres</Typography>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    {form}
                    <Button type="submit" color="primary" className={classes.submit} fullWidth 
                        variant="contained" disabled={!formIsValid}>
                        Confirm
                    </Button>
                </form>
            </div>
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        formFields: state.betaReaderGenres.formFields,
        processInstanceId: state.signUp.processInstanceId,
        taskId: state.betaReaderGenres.taskId,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchForm: (piId) => dispatch(actions.fetchForm(piId)),
        chooseGenres: (genresData, taskId) => actions.chooseGenres(genresData, taskId)
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(BetaReaderGenres);