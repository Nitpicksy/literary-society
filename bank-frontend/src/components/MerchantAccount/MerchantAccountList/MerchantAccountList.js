import React, { useEffect, useState } from 'react';
import { Typography, Container, LinearProgress, CssBaseline, Button, Grid } from '@material-ui/core';
import { connect } from 'react-redux';
import { DataGrid } from '@material-ui/data-grid';
import { useStyles } from './MerchantAccountListStyles';
import * as actions from './MerchantAccountListExport';
import { useHistory } from "react-router";

const MerchantAccountList = (props) => {
    const history = useHistory();

    const [loading, setLoading] = useState(true);
    const classes = useStyles();
    const { fetchMerchants } = props;

    const columns = [
        { field: 'name', headerName: 'Name', width: 180 },
        { field: 'city', headerName: 'City', width: 150 },
        { field: 'country', headerName: 'Country', width: 150 },
        { field: 'email', headerName: 'Email', width: 220 },
        { field: 'balance', headerName: 'Balance', width: 200 },
    ];

    let rows = [];
    let data = null;

    useEffect(() => {
        fetchMerchants();
        setLoading(false);
    }, [fetchMerchants]);

    const create = () => {
        history.push('/add-merchant');
    }

    if (!loading) {
        if (props.merchants) {
            for (var i in props.merchants) {
                rows.push({
                    "id": props.merchants[i].id, "name": props.merchants[i].name,
                    "city": props.merchants[i].city, "country": props.merchants[i].country,
                    "email": props.merchants[i].email, "balance": props.merchants[i].balance
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
            <Button color="primary" className={classes.submit} variant="contained"
                onClick={create}>
                Create
                </Button>
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
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(MerchantAccountList);