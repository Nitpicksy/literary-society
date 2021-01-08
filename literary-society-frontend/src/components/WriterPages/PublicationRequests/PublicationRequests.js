import React, { useEffect } from 'react';
import DescriptionIcon from '@material-ui/icons/Description';
import AddIcon from '@material-ui/icons/Add';
import { Container, CssBaseline, Grid, Typography, Button, Avatar } from '@material-ui/core/';
import { DataGrid } from '@material-ui/data-grid';
import { useStyles } from './PublicationRequestsStyles';
import { useHistory } from 'react-router';
import { connect } from 'react-redux';
import * as actions from './PublicationRequestsExport';
import * as signInActions from '../../Authentication/SignIn/SignInExport';
import { responseInterceptor } from '../../../responseInterceptor';

const PublicationRequests = (props) => {

    const classes = useStyles();
    const history = useHistory();
    const { fetchPublicationRequests, clearProcessState } = props;

    responseInterceptor.setupInterceptor(
        history,
        props.refreshTokenRequestSent,
        props.onRefreshToken
    );

    const columns = [
        { field: 'title', headerName: 'Title', width: 240 },
        { field: 'genre', headerName: 'Genre', width: 160 },
        { field: 'synopsis', headerName: 'Synopsis', width: 250 },
        { field: 'editorName', headerName: 'Editor', width: 210 },
        { field: 'status', headerName: 'Current status', width: 190 },
        {
            field: 'buttons', headerName: " ", width: 180,
            renderCell: (params) => {
                var complainBtn = <strong> </strong>;
                if (params.row.status === "In stores") {
                    complainBtn = <Button
                        variant="contained"
                        color="primary"
                        size="small"
                        onClick={() => complaintHandler(params.row)}>Create complaint</Button>
                }

                return (
                    <strong>
                        {complainBtn}
                    </strong>
                );
            }
        },
    ];

    let rows = [];
    let data = null;

    useEffect(() => {
        fetchPublicationRequests();
        clearProcessState();
    }, [fetchPublicationRequests, clearProcessState]);

    const showForm = () => {
        props.startProcess();
        history.push('./create-publication-request');
    }

    const complaintHandler = (choosenPublicationRequest) => {
        props.startPlagiarismProcess(choosenPublicationRequest);
        history.push('./plagiarism-complaint');
    }

    if (props.publicationRequests) {
        for (var i in props.publicationRequests) {
            let statusLowerCase = props.publicationRequests[i].status.toLowerCase().split('_').join(' ');
            let statusSentenceCase = statusLowerCase.charAt(0).toUpperCase() + statusLowerCase.slice(1);
            rows.push({
                "id": props.publicationRequests[i].id, "title": props.publicationRequests[i].title,
                "genre": props.publicationRequests[i].genre, "synopsis": props.publicationRequests[i].synopsis,
                "editorName": props.publicationRequests[i].editorName, "status": statusSentenceCase
            })
        }
        data = <Grid container className={classes.table}>
            <DataGrid rows={rows} columns={columns} autoPageSize />
        </Grid>;
    }

    return (
        <Container component="main" maxWidth="lg">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <DescriptionIcon />
                </Avatar>
                <Typography component="h1" variant="h4" className={classes.title}>Publications</Typography>
                <Button variant="contained" color="primary"
                    startIcon={<AddIcon />} onClick={showForm}>
                    Create
            </Button>
            </div>
            {data}
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        processInstanceId: state.publRequests.processInstanceId,
        taskId: state.publRequests.taskId,
        publicationRequests: state.publRequests.publicationRequests,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchPublicationRequests: () => dispatch(actions.fetchPublicationRequests()),
        startProcess: () => dispatch(actions.startProcess()),
        startPlagiarismProcess: (choosenPublicationRequest) => dispatch(actions.startPlagiarismProcess(choosenPublicationRequest)),
        clearProcessState: () => dispatch(actions.clearProcessState()),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PublicationRequests);