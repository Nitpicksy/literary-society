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

        if (properties.row.name === "Obradite zahtev za izdavanje knjige"
            || properties.row.name === "Prihvatite ili odbijte rukopis"
            || properties.row.name === "Definišite da li se rukopis šalje skupu beta čitaoca"
            || properties.row.name === "Ostavljanje komentara"
            || properties.row.name === "Definišite da li se rukopis šalje lektoru ili treba još nešto da se izmeni"
            || properties.row.name === "Definisite da li je potrebno izmeniti rukopis"
            || properties.row.name === "Posaljite knjigu na stampu ili dajte sugestije za izmenu") {
            history.push('/publication-request');
        } else if (properties.row.name === "Urednik preuzimanje dokumenta"
            || properties.row.name === "Preuzimanje PDF verzije rukopisa"
            || properties.row.name === "Preuzimanje dokumenta"
            || properties.row.name === "Preuzimanje PDF verzije rukopisa sa lektorovim izmenama") {
            history.push('/download-document');
        } else if (properties.row.name === "Slanje PDF verzije rukopisa"
            || properties.row.name === "Slanje rukopisa sa zabelezenim greskama") {
            history.push('/writer-upload-document');
        } else if(properties.row.name === 'Membership - submit your work' || properties.row.name === "Membership-more documents needed") {
            history.push('/writer-membership-upload')
        } else if(properties.row.name === 'Vote on a writer') {
            history.push('/voting')
        } else if(properties.row.name === 'Pay your membership') {
            history.push('/membership')
        } else if (properties.row.name === "Odaberite beta-citaoce") {
            history.push('/editor-choose-beta-readers');
        } else if (properties.row.name === "Izmena rukopisa prema komentarima beta-citalaca") {
            history.push('/opinions-of-beta-readers');
        } else if (properties.row.name === "Izmena rukopisa prema sugestijama urednika") {
            history.push('/opinion-of-editor');
        } else if (properties.row.name === "Definisite informacije o knjizi koja je poslata na stampu") {
            history.push('/publishing-info');
        } else if (properties.row.name === "Unos podataka o rukopisu") {
            history.push('/create-publication-request');
        } else if (properties.row.name === "Choose plagiarism review board"
        || properties.row.name === 'Submit your review report'
        || properties.row.name === 'Assign another editor') {
            history.push('/plagiarism');
        } else if (properties.row.name === "Download books to submit a report") {
            history.push('/plagiarism-review-download');
        } else if(properties.row.name === 'Plagiarism report-cast your vote') {
            history.push('/plagiarism-vote')
        }else if(properties.row.name === "Proverite da li je delo originalno"){
            history.push('/check-if-book-is-plagiarism')
        } else if(properties.row.name === 'Unesi podatke o knjizi i autoru') {
            history.push('/plagiarism-complaint')
        }
        
    }

    if (!loading) {
        data = <Grid align="center" style={{ margin: 20 }}>
            <Typography component="h3" variant="h6">You don't have any active tasks.</Typography>
        </Grid>;
        if (props.tasks) {
            rows = props.tasks;

            data = <Grid container className={classes.table}>
                <DataGrid rows={rows} columns={columns} pageSize={5} onRowClick={(properties) => selectRow(properties)} autoPageSize />
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