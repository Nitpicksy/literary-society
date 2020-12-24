import React, { useEffect, useState } from 'react';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import LinearProgress from '@material-ui/core/LinearProgress';
import CssBaseline from '@material-ui/core/CssBaseline';
import { connect } from 'react-redux';
import { DataGrid } from '@material-ui/data-grid';
import { useStyles } from './PaymentMethodListStyles';
import { Button, Grid } from '@material-ui/core';
import * as actions from './PaymentMethodListExport';

const PaymentMethodList = (props) => {
    const [loading, setLoading] = useState(true);
    const classes = useStyles();
    const { fetchPaymentMethods } = props;

    const columns = [
        { field: 'name', headerName: 'Name', width: 200 },
        { field: 'email', headerName: 'Email', width: 250 },
        { field: 'subscription', headerName: 'Subscription', width: 150 },
        {
            field: 'buttons', headerName: " ", width: 250,
            renderCell: (params) => {
                var showApprove = <strong> </strong>;
                var showReject = <strong> </strong>;
                if (params.row.status === "WAITING_APPROVAL") {
                    showApprove = <Button
                        variant="contained"
                        color="primary"
                        size="small"
                        style={{ marginLeft: 16 }} onClick={() => approveHandler(params.row)}> Approve</Button>
                    showReject = <Button
                        variant="contained"
                        color="secondary"
                        size="small"
                        style={{ marginLeft: 16 }}  onClick={() => rejectHandler(params.row)}> Reject</Button>
                }

                return (
                    <strong>
                        {showApprove}
                        {showReject}
                    </strong>
                );
            }
        },
    ];

    const columnsPaymentData = [
        { field: 'attributeName', headerName: 'Attribute name', width: 300 },
        { field: 'attributeType', headerName: 'Attribute type', width: 200 },
    ];
    const [paymentData, setPaymentData] = useState(null);

    let rows = [];
    let data = null;
    let rowsPaymentData = [];

    useEffect(() => {
        fetchPaymentMethods();
        setLoading(false);
    }, [fetchPaymentMethods]);

    const selectRow = (properties) => {
        rowsPaymentData = properties.row.listPaymentData;
        setPaymentData(
            <div className={classes.paymentDataTable}>
                <Typography component="h1" variant="h4">Payment data</Typography>
                <Grid container className={classes.tablePaymentData}>
                    <DataGrid rows={rowsPaymentData} columns={columnsPaymentData} pageSize={5} autoPageSize />
                </Grid>
            </div>
        );
    }

    const approveHandler = (choosenPaymentMethod) => {
        props.changePaymentMethodStatus(choosenPaymentMethod.id, 'approve');
    }

    const rejectHandler = (choosenPaymentMethod) => {
        props.changePaymentMethodStatus(choosenPaymentMethod.id, 'reject');
    }

    if (!loading) {
        if (props.paymentMethods) {

            for (var i in props.paymentMethods) {
                var subsriptionValue = "Enabled";
                if (props.paymentMethods[i].subscription === false) {
                    subsriptionValue = "Disabled";
                }


                rows.push({
                    "id": props.paymentMethods[i].id, "name": props.paymentMethods[i].name,
                    "api": props.paymentMethods[i].api, "email": props.paymentMethods[i].email, "subscription": subsriptionValue,
                    "listPaymentData": props.paymentMethods[i].listPaymentData, "status": props.paymentMethods[i].status
                })
            }
            data = <Grid container className={classes.table}>
                <DataGrid rows={rows} columns={columns} pageSize={5} onRowClick={(properties) => selectRow(properties)}
                    autoPageSize />
            </Grid>;
        }
    } else {
        <LinearProgress />
    }

    return (
        <Container component="main" maxWidth="md">
            <CssBaseline />
            <div className={classes.paper}>
                <Typography component="h1" variant="h4">Payment methods</Typography>
            </div>
            {data}
            {paymentData}
        </Container>
    );
};


const mapStateToProps = state => {
    return {
        paymentMethods: state.paymentMethodList.paymentMethods,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchPaymentMethods: () => dispatch(actions.fetchPaymentMethods()),
        changePaymentMethodStatus: (id, status) => dispatch(actions.changePaymentMethodStatus(id, status)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PaymentMethodList);