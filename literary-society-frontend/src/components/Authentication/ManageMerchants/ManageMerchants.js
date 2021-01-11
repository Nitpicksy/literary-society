import React, { useEffect, useState } from 'react';
import { Typography, Container, LinearProgress, CssBaseline, Button, Grid } from '@material-ui/core';
import { connect } from 'react-redux';
import { DataGrid } from '@material-ui/data-grid';
import { useStyles } from './ManageMerchantsStyles';
import * as actions from './ManageMerchantsExport';
import * as signInActions from "../../Authentication/SignIn/SignInExport";
import { responseInterceptor } from "../../../responseInterceptor";
import { useHistory } from "react-router";

const ManageMerchants = (props) => {
    const history = useHistory();

    const [loading, setLoading] = useState(true);
    const classes = useStyles();
    const { fetchMerchants } = props;

    responseInterceptor.setupInterceptor(
        history,
        props.refreshTokenRequestSent,
        props.onRefreshToken
    );

    const columns = [
        { field: 'name', headerName: 'Name', width: 200 },
        { field: 'city', headerName: 'City', width: 150 },
        { field: 'country', headerName: 'Country', width: 150 },
        { field: 'email', headerName: 'Email', width: 200 },
        {
            field: 'buttons', headerName: " ", width: 190,
            renderCell: (params) => {
                var showApprove = <strong> </strong>;
                var showReject = <strong> </strong>;
                if (params.row.status === "WAITING_APPROVAL") {
                    showApprove = <Button
                        variant="contained"
                        color="primary"
                        size="small"
                        onClick={() => approveHandler(params.row)}>Approve</Button>
                    showReject = <Button
                        variant="contained"
                        color="secondary"
                        size="small"
                        style={{ marginLeft: 16 }} onClick={() => rejectHandler(params.row)}>Reject</Button>
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

    let rows = [];
    let data = null;

    useEffect(() => {
        fetchMerchants();
        setLoading(false);
    }, [fetchMerchants]);

    const approveHandler = (choosenUser) => {
        props.changeMerchantStatus(choosenUser.id, 'approve');
    }

    const rejectHandler = (choosenUser) => {
        props.changeMerchantStatus(choosenUser.id, 'reject');
    }

    if (!loading) {
        if (props.merchants) {
            for (var i in props.merchants) {
                rows.push({
                    "id": props.merchants[i].id, "name": props.merchants[i].name,
                    "city": props.merchants[i].city, "country": props.merchants[i].country,"email": props.merchants[i].email,
                    "role": props.merchants[i].role,"status": props.merchants[i].status
                })
            }
            data = <Grid container className={classes.table}>
                <DataGrid rows={rows} columns={columns} autoPageSize />
            </Grid>;
        }
    } else {
        <LinearProgress />
    }

    return (
        <Container component="main" maxWidth="md">
            <CssBaseline />
            <div className={classes.paper}>
                <Typography component="h1" variant="h4">Merchants</Typography>
            </div>
            {data}
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        merchants: state.merchantList.merchants,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchMerchants: () => dispatch(actions.fetchMerchants()),
        changeMerchantStatus: (id, status) => dispatch(actions.changeMerchantStatus(id, status)),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ManageMerchants);