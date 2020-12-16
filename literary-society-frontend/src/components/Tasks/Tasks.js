import React, { useEffect, useState } from 'react';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import LinearProgress from '@material-ui/core/LinearProgress';
import ListIcon from '@material-ui/icons/List';
import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';
import { connect } from 'react-redux';
import { DataGrid } from '@material-ui/data-grid';
import { useStyles } from '../Tasks/TasksStyles';
import { Grid } from '@material-ui/core';
import * as actions from './TasksExport';
import * as signInActions from '../Authentication/SignIn/SignInExport';
import { responseInterceptor } from '../../responseInterceptor';
import { useHistory } from 'react-router';

const Tasks = (props) => {
    const history = useHistory();
    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);
    const [loading, setLoading] = useState(true);
    const classes = useStyles();
    const { fetchTasks } = props;
    const columns = [
        { field: 'name', headerName: 'Name', width: 550 }
    ];

    let rows = [];
    let data = null;

    useEffect(() => {
        fetchTasks();
        setLoading(false);
    }, [fetchTasks]);

    const selectRow = (properties) => {
        props.setSelectedTask(properties.row.processInstanceId, properties.row.id, properties.row.name);

        history.push('/publication-request');
    }

    if (!loading) {
        data = <Grid align="center" style={{ margin: 20 }}>
            <Typography component="h3" variant="h6">You don't have any active tasks.</Typography>
        </Grid>;
        if (props.tasks) {
            rows = props.tasks;
            
            data = <Grid container className={classes.table}>
                <DataGrid rows={rows} columns={columns} pageSize={5} onRowClick={(properties) => selectRow(properties)} />
            </Grid>;
        }
    } else {
        <LinearProgress />
    }

    return (
        <Container component="main" maxWidth="sm">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <ListIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Tasks</Typography>
            </div>
            {data}
        </Container>
    );
};


const mapStateToProps = state => {
    return {
        tasks: state.tasks.tasks,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchTasks: () => dispatch(actions.fetchTasks()),
        setSelectedTask: (piId, taskId, taskName) => dispatch(actions.setSelectedTask(piId, taskId, taskName)),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Tasks);