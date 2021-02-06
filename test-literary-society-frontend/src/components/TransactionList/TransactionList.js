import React, { useEffect, useState } from 'react';
import { Typography, Container, LinearProgress, CssBaseline, Grid } from '@material-ui/core';
import { connect } from 'react-redux';
import { DataGrid } from '@material-ui/data-grid';
import { useStyles } from './TransactionListStyles';
import * as actions from './TransactionListActions';
import * as signInActions from '../Authentication/SignIn/SignInActions';
import { responseInterceptor } from "../../responseInterceptor";
import { useHistory } from "react-router";

const CompanyList = (props) => {
    const history = useHistory();

    const [loading, setLoading] = useState(true);
    const classes = useStyles();
    const { fetchTransactions } = props;

    responseInterceptor.setupInterceptor(
        history,
        props.refreshTokenRequestSent,
        props.onRefreshToken
    );

    const columns = [
        { field: 'type', headerName: 'Type', width: 140 },
        { field: 'status', headerName: 'Status', width: 100 },
        { field: 'date', headerName: 'Date', width: 100 },
        { field: 'amount', headerName: 'Amount', width: 100 },
        { field: 'merchant', headerName: 'Merchant', width: 200 },
        { field: 'buyer', headerName: 'Buyer', width: 200 },
        { field: 'books', headerName: 'Books', width: 260 },
    ];

    let rows = [];
    let data = null;

    useEffect(() => {
        fetchTransactions();
        setLoading(false);
    }, [fetchTransactions]);

    if (!loading) {
        if (props.transactions) {
            for (var i in props.transactions) {
                let books = props.transactions[i].orderedBooks.map(book => book.title).join(', ');
                rows.push({
                    "id": props.transactions[i].id, 
                    "type": props.transactions[i].transactionType,
                    "status": props.transactions[i].transactionStatus, 
                    "date": new Date(props.transactions[i].timestamp).toLocaleDateString(),
                    "amount": props.transactions[i].amount + ' din.', 
                    "merchant": props.transactions[i].merchant,
                    "buyer": props.transactions[i].buyerName,
                    "books": books === '' ? '-' : books,
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
                <Typography component="h1" variant="h4">Transactions</Typography>
            </div>
            {data}
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        transactions: state.transactionList.transactions,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchTransactions: () => dispatch(actions.fetchTransactions()),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(CompanyList);