import React, { useState } from 'react';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './AddPaymentMethodStyles';
import Form from '../../../UI/Form/Form';
import { connect } from 'react-redux';
import {  useHistory } from 'react-router';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import { Grid, Paper } from '@material-ui/core';
import { toastr } from 'react-redux-toastr';
import * as actions from './AddPaymentMethodExport';

const AddPaymentMethod = (props) => {
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
        api: {
            elementType: 'input',
            elementConfig: {
                label: 'API'
            },
            value: '',
            validation: {
                required: true,
                pattern: '^(http(s)?:\\/\\/)?((www\\.)|(localhost:))[(\\/)?a-zA-Z0-9@:%._\\+~#=-]{1,256}$'
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: 'API is not valid',
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
            errorMessage: 'Invalid e-mail address entered.',
        },
        subscription: {
            elementType: 'checkbox',
            elementConfig: {
                label: 'Support subscription'
            },
            value: false,
            valid: true,
            error: false,
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
    })

    const [formDataIsValid, setFormDataIsValid] = useState(false);
    const initialData = {
        attributeName: {
            elementType: 'input',
            elementConfig: {
                label: 'Attribute name'
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
        attributeType: {
            elementType: 'selectOne',
            elementConfig: {
                label: 'Attribute type',
                options: [
                    {
                        "value": "text",
                        "displayValue": "text"
                    },
                    {
                        "value": "password",
                        "displayValue": "password"
                    }
                ]
            },
            value: '',
            validation: {
                requiredSelect: true,
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                multipleSelect: false
            }
        },
        attributeJSONName: {
            elementType: 'input',
            elementConfig: {
                label: 'Attribute JSON name'
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
    }
    const [controlsData, setControlsData] = useState({ ...initialData })

    const [rows, setRows] = useState([]);
    const [certificate, setCertificate] = useState(null);

    const submitHander = (event) => {
        event.preventDefault();
        //proveri da li je uneo barem jedan paymentData
        //posalji i podatke sa form i sa formData
        //posalji i sertificate
        if(rows.length === 0){
            toastr.error("Register payment method", "You have to enter at least one payment data.");
            return;
        }

        if(!certificate){
            toastr.error("Register payment method", "You have to upload your certificate.");
            return;
        }
        const certificateFormData = new FormData();
        certificateFormData.append('certificate', certificate);
        console.log(certificateFormData)
        props.onRegisterPaymentMethod({'name': controls.name.value, 'api': controls.api.value, 'commonName': controls.commonName.value, 
        'subscription': controls.subscription.value, 'email':controls.email.value },certificateFormData, rows, history );
    }

    const submitDataHander = (event) => {
        event.preventDefault();
        var newRows = [...rows];
        if (newRows.some(e => e.attributeName === controlsData.attributeName.value)) {
            toastr.error('Payment Data', 'Payment Data with same attribute name already exist.');
        } else {
            newRows.push(createData(controlsData.attributeName.value, controlsData.attributeType.value, controlsData.attributeJSONName.value));
            setRows(newRows);
            clearForm();
        }

    }

    const clearForm = () => {
        setControlsData({ ...initialData })
        setFormDataIsValid(false);
    }

    const createData = (attributeName, attributeType, attributeJSONName) => {
        return { 'attributeName': attributeName, 'attributeType': attributeType, 'attributeJSONName': attributeJSONName };
    }

    const handleChooseFile = ({ target }) => {
        setCertificate(target.files[0]);
    }


    return (
        <Container component="main" maxWidth="md">
            <CssBaseline />
            <Paper className={classes.mainPaper}>
                <Typography component="h1" variant="h4">Register Payment Method</Typography>
                <Grid container align="center" spacing={4} justify="center" className={classes.gridData}>
                    <Grid item md={6}>
                        <Paper className={classes.mainData}>
                            <Typography variant="h6">Main Data</Typography>
                            <form className={classes.form} noValidate onSubmit={submitHander}>
                                <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />
                            </form>
                            <div  className={classes.chooseCertificate}>
                                <input type="file" accept=".crt, .p12" hidden id="upload-file"
                                    onChange={handleChooseFile} />
                                <label htmlFor="upload-file">
                                    <Grid container>
                                        <Grid item xs={5}>
                                            <Button color="primary" variant="contained" component="span" style={{float: "left"}}>
                                                Choose certificate
                                    </Button>
                                        </Grid>
                                        <Grid item xs={7} className={classes.fileNameGrid} >
                                            <Typography component="span"  className={classes.fileName}>
                                                {certificate ? certificate.name : ''}
                                            </Typography>
                                        </Grid>
                                    </Grid>
                                </label>
                            </div>

                        </Paper>
                    </Grid>
                    <Grid item md={6}>
                        <Paper className={classes.paymentData}>
                            <Typography variant="h6">Payment Data</Typography>
                            <form className={classes.form} noValidate onSubmit={submitDataHander} id="create-data">
                                <Form controls={controlsData} setControls={setControlsData} setFormIsValid={setFormDataIsValid} />
                                <Button type="submit" color="primary" className={classes.submit} variant="contained"
                                    onClick={submitDataHander} disabled={!formDataIsValid}>Add</Button>
                            </form>
                        </Paper>
                    </Grid>
                    <Grid item md={12}>
                        <TableContainer component={Paper} className={classes.table}>
                            <Table aria-label="simple table">
                                <TableHead>
                                    <TableRow>
                                        <TableCell className={classes.tablecell}>Attribute name</TableCell>
                                        <TableCell align="right" className={classes.tablecell}>Attribute  type</TableCell>
                                        <TableCell align="right" className={classes.tablecell}>Attribute JSON name</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {rows.map((row) => (
                                        <TableRow key={row.attributeName}>
                                            <TableCell component="th" scope="row" className={classes.tablecell}>
                                                {row.attributeName}
                                            </TableCell>
                                            <TableCell align="right" className={classes.tablecell}>{row.attributeType}</TableCell>
                                            <TableCell align="right" className={classes.tablecell}>{row.attributeJSONName}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </Grid>
                </Grid>
                <div className={classes.paper}>
                    <Button type="submit" color="primary" className={classes.submitForm} variant="contained"
                        onClick={submitHander} disabled={!formIsValid}>Submit</Button>
                </div>
            </Paper>
        </Container>
    );
};


const mapDispatchToProps = dispatch => {
    return {
        onRegisterPaymentMethod: (mainData, certificateData, paymentData,history) => dispatch(actions.registerPaymentMethod(mainData, certificateData, paymentData,history)),
    }
};
export default connect(null, mapDispatchToProps)(AddPaymentMethod);;