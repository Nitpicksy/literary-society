import React, { useEffect, useState } from 'react';
import { Typography, Container, LinearProgress, CssBaseline, Button, Grid } from '@material-ui/core';
import { connect } from 'react-redux';
import { DataGrid } from '@material-ui/data-grid';
import { useStyles } from './CompanyListStyles';
import * as actions from './CompanyListExport';
import * as signInActions from "../../Authentication/SignIn/SignInExport";
import { responseInterceptor } from "../../../responseInterceptor";
import { useHistory } from "react-router";

const CompanyList = (props) => {
    const history = useHistory();

    const [loading, setLoading] = useState(true);
    const classes = useStyles();
    const { fetchCompanies } = props;

    responseInterceptor.setupInterceptor(
        history,
        props.refreshTokenRequestSent,
        props.onRefreshToken
    );

    const columns = [
        { field: 'companyName', headerName: 'Company Name', width: 200 },
        { field: 'websiteURL', headerName: 'Website URL', width: 260 },
        { field: 'supportedPaymentMethods', headerName: 'Supported Payment Methods', width: 260 },
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
        fetchCompanies();
        setLoading(false);
    }, [fetchCompanies]);

    const approveHandler = (choosenCompany) => {
        props.changeCompanyStatus(choosenCompany.id, 'approve');
    }

    const rejectHandler = (choosenCompany) => {
        props.changeCompanyStatus(choosenCompany.id, 'reject');
    }

    if (!loading) {
        if (props.companies) {
            for (var i in props.companies) {
                let supportedPaymentMethodsStr = props.companies[i].supportedPaymentMethods.map(method => method.name).join(', ');
                rows.push({
                    "id": props.companies[i].id, "companyName": props.companies[i].companyName, "websiteURL": props.companies[i].websiteURL,
                    "supportedPaymentMethods": supportedPaymentMethodsStr, "status": props.companies[i].status
                })
            }
            data = <Grid container className={classes.table}>
                <DataGrid rows={rows} columns={columns} pageSize={5} autoPageSize />
            </Grid>;
        }
    } else {
        <LinearProgress />
    }

    return (
        <Container component="main" maxWidth="md">
            <CssBaseline />
            <div className={classes.paper}>
                <Typography component="h1" variant="h4">Companies</Typography>
            </div>
            {data}
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        companies: state.companyList.companies,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchCompanies: () => dispatch(actions.fetchCompanies()),
        changeCompanyStatus: (id, status) => dispatch(actions.changeCompanyStatus(id, status)),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(CompanyList);