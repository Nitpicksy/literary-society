import React, { useState } from 'react';
import { CssBaseline, Button, Typography, Container, Avatar, Grid, Paper } from '@material-ui/core';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@material-ui/core';
import { useStyles } from './AddPaymentMethodStyles';
import { connect } from 'react-redux';
import { useHistory } from 'react-router';
import { toastr } from 'react-redux-toastr';
import * as actions from './AddPaymentMethodExport';
import Form from '../../../UI/Form/Form';
import PaymentIcon from '@material-ui/icons/Payment';

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
            additionalData: {
                errorMessage:  '',
            }
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
            errorMessage: '',
            additionalData: {
                errorMessage:  'API is not valid',
            }
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
                errorMessage:  'Invalid e-mail address entered.',
            }
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
            additionalData: {
                errorMessage:  '',
            }
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

        if (!(rows && Array.isArray(rows) && rows.length)) {
            toastr.info("Register payment method", "You need to enter at least one payment data.");
            return;
        }

        if (!certificate) {
            toastr.info("Register payment method", "You need to upload your certificate.");
            return;
        }

        const mainData = {
            name: controls.name.value, 
            api: controls.api.value, 
            commonName: controls.commonName.value,
            subscription: controls.subscription.value, 
            email: controls.email.value
        }

        const certFormData = new FormData();
        certFormData.append('certificate', certificate);
        certFormData.append('mainData', new Blob([JSON.stringify(mainData)], { type: "application/json" }));
        certFormData.append('paymentDataList', new Blob([JSON.stringify(rows)], { type: "application/json" }));

        props.onRegisterPaymentMethod(certFormData, history);
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
                <div className={classes.centered}>
                    <Avatar className={classes.avatar}>
                        <PaymentIcon />
                    </Avatar>
                    <Typography component="h1" variant="h4">Register Payment Method</Typography>
                </div>
                <Grid container align="center" spacing={4} justify="center" className={classes.gridData}>
                    <Grid item md={6}>
                        <Paper className={classes.mainData}>
                            <Typography variant="h6">Main Data</Typography>
                            <form className={classes.form} noValidate onSubmit={submitHander}>
                                <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />
                            </form>
                            <div className={classes.chooseCertificate}>
                                <input type="file" accept=".crt" hidden id="upload-file"
                                    onChange={handleChooseFile} />
                                <label htmlFor="upload-file">
                                    <Grid container>
                                        <Grid item xs={6}>
                                            <Button color="primary" variant="contained" component="span" style={{ float: "left" }}>
                                                Upload certificate
                                            </Button>
                                        </Grid>
                                        <Grid item xs={6} className={classes.fileNameGrid} >
                                            <Typography component="span" className={classes.fileName}>
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
                <div className={classes.centered}>
                    <Button type="submit" color="primary" className={classes.submitForm} variant="contained"
                        onClick={submitHander} disabled={!formIsValid}>Submit</Button>
                </div>
            </Paper>
        </Container>
    );
};


const mapDispatchToProps = dispatch => {
    return {
        onRegisterPaymentMethod: (paymentMethod, history) => dispatch(actions.registerPaymentMethod(paymentMethod, history)),
    }
};
export default connect(null, mapDispatchToProps)(AddPaymentMethod);;