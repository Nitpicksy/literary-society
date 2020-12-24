import React, { useState } from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './AddPaymentMethodStyles';
import Form from '../../../UI/Form/Form';
import { connect } from 'react-redux';
import { Redirect, useHistory } from 'react-router';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import { Paper } from '@material-ui/core';
import LocalAtmIcon from '@material-ui/icons/LocalAtm';

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
                label:'Api'
            },
            value: '',
            validation: {
                required: true,
                pattern: '^(http(s)?:\/\/)?((www\.)|(localhost:))[(\/)?a-zA-Z0-9@:%._\+~#=-]{1,256}$'
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: 'API is not valid',
        }
    })

    const [formDataIsValid, setFormDataIsValid] = useState(false);
    const [controlsData, setControlsData] = useState({
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
    })

    const [rows, setRows] = useState([]);

    const submitHander = (event) => {
        event.preventDefault();
        //proveri da li je uneo barem jedan paymentData
        //posalji i podatke sa form i sa formData
        props.onSignIn(controls.username.value, controls.password.value);
    }

    const submitDataHander = (event) => {
        event.preventDefault();
        var newRows = {...rows};
        newRows.push(createData(controls.attributeName.value,controls.attributeType.value,controls.attributeJSONName.value));
        setRows(newRows)
    }

    function createData(attributeName, attributeType,attributeJSONName ) {
        return {attributeName, attributeType,attributeJSONName};
    }

    return (
        <Container component="main" maxWidth="sm">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <LocalAtmIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Add your Payment Method</Typography>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />
                    <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained"
                        onClick={submitHander} disabled={!formIsValid}>Submit</Button>
                </form>
                <Paper>
                    <Typography component="h1" variant="h4">Payment Data</Typography>
                    <form className={classes.form} noValidate onSubmit={submitDataHander}>
                        <Form controls={controlsData} setControls={setControlsData} setFormIsValid={setFormDataIsValid} />
                        <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained"
                            onClick={submitDataHander} disabled={!formDataIsValid}>Submit</Button>
                    </form>
                    <TableContainer component={Paper}>
                        <Table aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell>Attribute name</TableCell>
                                    <TableCell align="right">Attribute  type</TableCell>
                                    <TableCell align="right">Attribute JSON name</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {rows.map((row) => (
                                    <TableRow key={row.name}>
                                        <TableCell component="th" scope="row">
                                            {row.attributeName}
                                        </TableCell>
                                        <TableCell align="right">{row.attributeType}</TableCell>
                                        <TableCell align="right">{row.attributeJSONName}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Paper>

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
export default AddPaymentMethod;