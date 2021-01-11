import React, { useEffect, useState } from 'react';
import { Typography, Container, LinearProgress, CssBaseline, Button, Grid } from '@material-ui/core';
import { connect } from 'react-redux';
import { DataGrid } from '@material-ui/data-grid';
import { useStyles } from './ManageLecturersAndEditorsStyles';
import * as actions from './ManageLecturersAndEditorsExport';
import * as signInActions from "../../Authentication/SignIn/SignInExport";
import { responseInterceptor } from "../../../responseInterceptor";
import { useHistory } from "react-router";

const ManageLecturersAndEditors = (props) => {
    const history = useHistory();

    const [loading, setLoading] = useState(true);
    const classes = useStyles();
    const { fetchUsers } = props;

    responseInterceptor.setupInterceptor(
        history,
        props.refreshTokenRequestSent,
        props.onRefreshToken
    );

    const columns = [
        { field: 'name', headerName: 'Name', width: 250 },
        { field: 'city', headerName: 'City', width: 170 },
        { field: 'country', headerName: 'Country', width: 170 },
        { field: 'email', headerName: 'Email', width: 250 },
        { field: 'role', headerName: 'Role', width: 150 },
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
        fetchUsers();
        setLoading(false);
    }, [fetchUsers]);

    const approveHandler = (choosenUser) => {
        props.changeUserStatus(choosenUser.id, 'approve');
    }

    const rejectHandler = (choosenUser) => {
        props.changeUserStatus(choosenUser.id, 'reject');
    }

    if (!loading) {
        if (props.users) {
            for (var i in props.users) {
                rows.push({
                    "id": props.users[i].id, "name": props.users[i].firstName + ' ' + props.users[i].lastName,
                    "city": props.users[i].city, "country": props.users[i].country, "email": props.users[i].email,
                    "role": props.users[i].role,"status": props.users[i].status
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
        <Container component="main" maxWidth="lg">
            <CssBaseline />
            <div className={classes.paper}>
                <Typography component="h1" variant="h4">Lecturers and Editors</Typography>
            </div>
            {data}
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        users: state.userList.users,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchUsers: () => dispatch(actions.fetchUsers()),
        changeUserStatus: (id, status) => dispatch(actions.changeUserStatus(id, status)),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ManageLecturersAndEditors);