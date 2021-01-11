import React, { useEffect, useState } from 'react';
import { Typography, Container, LinearProgress, CssBaseline, Button, Grid } from '@material-ui/core';
import { connect } from 'react-redux';
import { DataGrid } from '@material-ui/data-grid';
import { useStyles } from './ClientAccountListStyles';
import * as actions from './ClientAccountListExport';
import { useHistory } from "react-router";

const ClientAccountList = (props) => {
    const history = useHistory();

    const [loading, setLoading] = useState(true);
    const classes = useStyles();
    const { fetchClients } = props;

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
        fetchClients();
        setLoading(false);
    }, [fetchClients]);

    const create = () => {
        history.push('/add-client');
    }

    if (!loading) {
        if (props.clients) {
            for (var i in props.clients) {
                rows.push({
                    "id": props.clients[i].id, "name": props.clients[i].firstName + ' ' + props.clients[i].lastName,
                    "city": props.clients[i].city, "country": props.clients[i].country,
                    "email": props.clients[i].email, "balance": props.clients[i].balance
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
                <Typography component="h1" variant="h4">Clients</Typography>
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
        clients: state.clientList.clients,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchClients: () => dispatch(actions.fetchClients()),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ClientAccountList);