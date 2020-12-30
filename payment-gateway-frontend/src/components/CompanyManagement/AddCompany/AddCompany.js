import React, { useEffect, useState } from 'react';
import * as actions from './AddCompanyActions';
import { Avatar, Button, CssBaseline, Typography, Container, TextField, Grid, Paper, FormControlLabel, Checkbox } from '@material-ui/core';
import BusinessIcon from '@material-ui/icons/Business';
import { useStyles } from './AddCompanyStyles';
import { connect } from 'react-redux';
import { useHistory } from 'react-router';
import { useForm } from "react-hook-form";
import { toastr } from 'react-redux-toastr';

const AddCompany = (props) => {

    const history = useHistory();
    const classes = useStyles();
    const { register, handleSubmit, errors } = useForm({ mode: 'all' });
    const { fetchPaymentMethods } = props;

    const [certificate, setCertificate] = useState(null);
    const [supportedPaymentMethods, setSupportedPaymentMethods] = useState([]);

    useEffect(() => {
        fetchPaymentMethods();
    }, [fetchPaymentMethods]);

    const handleChooseFile = ({ target }) => {
        setCertificate(target.files[0]);
    }

    const handleChecked = ({ target }) => {
        if (target.checked) {
            let paymentMethod = props.paymentMethods.find(method => method.id === parseInt(target.name));
            setSupportedPaymentMethods(supportedPayments => [...supportedPayments, paymentMethod]);
        } else {
            setSupportedPaymentMethods(supportedPayments => supportedPayments.filter(method => method.id !== parseInt(target.name)));
        }
    }

    const onSubmit = (companyData) => {
        if (!certificate) {
            toastr.info("Add Company", "You need to upload your certificate.");
            return;
        }

        if (!(supportedPaymentMethods && Array.isArray(supportedPaymentMethods) && supportedPaymentMethods.length)) {
            toastr.info("Add Company", "You need to support at least one payment method.");
            return;
        }

        const certFormData = new FormData();
        certFormData.append('certificate', certificate);
        certFormData.append('companyData', new Blob([JSON.stringify(companyData)], { type: "application/json" }));
        certFormData.append('supportedPaymentMethods', new Blob([JSON.stringify(supportedPaymentMethods)], { type: "application/json" }));

        props.onAddCompany(certFormData, history);
    };

    const createTextField = (name, label) => {
        const textField =
            <TextField margin="normal" fullWidth name={name}
                error={errors[name] ? true : false} helperText={errors[name] ? errors[name].message : ''}
                label={label} inputRef={register({
                    required: {
                        value: true,
                        message: label + ' is required'
                    }
                })} />
        return textField;
    }

    const createURLField = (name, label) => {
        const urlField =
            <TextField margin="normal" fullWidth name={name}
                error={errors[name] ? true : false} helperText={errors[name] ? errors[name].message : ''}
                label={label} inputRef={register({
                    required: {
                        value: true,
                        message: label + ' is required'
                    },
                    pattern: {
                        value: new RegExp('((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+(:[0-9]+)?|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)'),
                        message: 'Invalid URL address'
                    }
                })} />
        return urlField;
    }

    let paymentMethodCards = null;

    if (props.paymentMethods && Array.isArray(props.paymentMethods) && props.paymentMethods.length) {
        paymentMethodCards = props.paymentMethods.map(paymentMethod => {
            return <Paper key={paymentMethod.id} className={classes.paymentMethodPaper}>
                <FormControlLabel label={paymentMethod.name} classes={{ label: classes.chkLabel }}
                    control={
                        <Checkbox name={'' + paymentMethod.id} onChange={handleChecked} color="primary" />
                    }
                />
            </Paper>;
        });
    } else {
        paymentMethodCards =
            <Typography component="h3" variant="h6">No available payment methods at the moment.</Typography>;
    }

    return (
        <Container component="main" maxWidth="md">
            <CssBaseline />
            <Paper className={classes.mainPaper}>
                <div className={classes.centered}>
                    <Avatar className={classes.avatar}>
                        <BusinessIcon />
                    </Avatar>
                    <Typography component="h1" variant="h4">Add your Company</Typography>
                </div>
                <form className={classes.form} noValidate onSubmit={handleSubmit(onSubmit)}>
                    <Grid container align="center" spacing={4} justify="center">
                        <Grid item md={6}>
                            {createTextField('companyName', 'Company Name')}
                            {createURLField('websiteURL', 'Website URL')}
                            <TextField margin="normal" fullWidth name='email' type='email'
                                error={errors.email ? true : false} helperText={errors.email ? errors.email.message : ''}
                                label='Email Address' inputRef={register({
                                    required: {
                                        value: true,
                                        message: 'Email address is required'
                                    },
                                    pattern: {
                                        value: new RegExp('^[a-zA-Z0-9_+&*-]+(?:.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+.)+[a-zA-Z]{2,7}$'),
                                        message: 'Invalid email address'
                                    }
                                })}
                            />
                            {createTextField('commonName', 'Common Name')}
                        </Grid>
                        <Grid item md={6}>
                            {createURLField('successURL', 'Success URL')}
                            {createURLField('failedURL', 'Failed URL')}
                            {createURLField('errorURL', 'Error URL')}
                            <div className={classes.chooseCertificate}>
                                <input type="file" accept=".crt" hidden id="upload-file"
                                    onChange={handleChooseFile} />
                                <label htmlFor="upload-file">
                                    <Grid container>
                                        <Grid item xs={5}>
                                            <Button color="primary" variant="contained" component="span" style={{ float: "left" }}>
                                                Upload certificate
                                            </Button>
                                        </Grid>
                                        <Grid item xs={7} className={classes.fileNameGrid} >
                                            <Typography component="span" className={classes.fileName}>
                                                {certificate ? certificate.name : ''}
                                            </Typography>
                                        </Grid>
                                    </Grid>
                                </label>
                            </div>
                        </Grid>
                        <Grid item md={12}>
                            <Typography component="h2" variant="h5">Support payment methods:</Typography>
                            {paymentMethodCards}
                        </Grid>
                    </Grid>
                    <div className={classes.centered}>
                        <Button type="submit" color="primary" className={classes.submit} variant="contained">Submit</Button>
                    </div>
                </form>
            </Paper>
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        paymentMethods: state.addCompany.paymentMethods
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchPaymentMethods: () => dispatch(actions.fetchPaymentMethods()),
        onAddCompany: (company, history) => dispatch(actions.addCompany(company, history)),
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(AddCompany);