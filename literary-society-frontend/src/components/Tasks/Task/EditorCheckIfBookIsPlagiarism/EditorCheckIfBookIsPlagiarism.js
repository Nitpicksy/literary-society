import React, { useEffect, useState } from 'react';
import { Typography, Container, Avatar, CssBaseline, Button, Paper, IconButton } from '@material-ui/core';
import { Table, TableBody, TableCell, TableHead, TableRow } from '@material-ui/core';
import ListIcon from '@material-ui/icons/List';
import GetAppIcon from '@material-ui/icons/GetApp';
import { toastr } from 'react-redux-toastr';
import { connect } from 'react-redux';
import { useStyles } from './EditorCheckIfBookIsPlagiarismStyles';
import { responseInterceptor } from '../../../../responseInterceptor';
import { useHistory } from 'react-router';
import { extractControls } from '../../../../utility/extractControls';
import * as actions from './EditorCheckIfBookIsPlagiarismExport';
import * as signInActions from '../../../Authentication/SignIn/SignInExport';
import Form from '../../../../UI/Form/Form';
import PublicationRequestCard from '../PublicationRequest/PublicationRequestCard/PublicationRequestCard';

const EditorCheckIfBookIsPlagiarism = (props) => {
    const history = useHistory();
    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);
    const classes = useStyles();

    const { selectedTask } = props;
    const { formFields, fetchForm, fetchPlagiarismInfo } = props;
    const { publicationRequest, plagiarismInfo } = props;

    let [controls, setControls] = useState(null);
    const [formIsValid, setFormIsValid] = useState(false);

    let form = null;
    let publicationRequestCard = null;
    let plagiarismInformation = null;

    useEffect(() => {
        fetchForm(selectedTask.piId, selectedTask.taskId);
        fetchPlagiarismInfo(selectedTask.piId);
    }, [fetchForm, selectedTask.piId, selectedTask.taskId, fetchPlagiarismInfo]);

    useEffect(() => {
        if (formFields) {
            let extractedControls = extractControls(formFields)
            setControls(extractedControls);
        }
    }, [formFields]);

    const submitHander = (event) => {
        event.preventDefault();
        let array = [];
        for (let [key, data] of Object.entries(controls)) {
            let value = data.value
            if (Array.isArray(data.value)) {
                value = data.value.join();
            }
            array.push({ fieldId: key, fieldValue: value });
        }

        if (array[0].fieldValue === 'REQUEST_REJECTED') {
            console.log('REQUEST_REJECTED');
            if (!array[1].fieldValue) {
                toastr.error('Reject Publication Request', 'You have to write a reason for rejecting.');
                return;
            }
        }

        if (array[0].fieldValue === 'NOT_ORIGINAL') {
            if (!array[1].fieldValue) {
                toastr.error('Manuscript', 'You have to write a reason why the manuscript is not original.');
                return;
            }
        }

        if (array[0].fieldValue === 'TO_BE_EDITED' && array[1]) {
            if (!array[1].fieldValue) {
                toastr.error('Manuscript', 'You have to explain what writers need to change.');
                return;
            }
        }
        props.onConfirm(array, selectedTask.taskId, history);
    }

    if (controls) {
        form = <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />;
    }

    if (publicationRequest) {
        publicationRequestCard = <PublicationRequestCard book={publicationRequest} />
    }

    if (plagiarismInfo) {
        plagiarismInformation = <Paper justify="center" className={classes.paperPercentage}>
            <Typography component="h1" variant="h5" className={classes.similarBooks}>Similar books</Typography>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell className={classes.cellFont}>Title</TableCell>
                        <TableCell className={classes.cellFont} align="right">Similarity</TableCell>
                        <TableCell className={classes.cellFont}>&nbsp;</TableCell>
                        <TableCell className={classes.cellFont}>&nbsp;</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {plagiarismInfo.map(paper => {
                        let similarity = <span>{(paper.similarProcent * 100).toFixed(2)}%</span>;
                        if (paper.similarProcent > 0.25) {
                            similarity = <span className={classes.red}>{(paper.similarProcent * 100).toFixed(2)}%</span>;
                        }
                        return <TableRow key={paper.id}>
                            <TableCell className={classes.cell}>{paper.title.slice(20, -4)}</TableCell>
                            <TableCell className={classes.cell} align="right">{similarity}</TableCell>
                            <TableCell className={classes.cellBtn} align="right">
                                <Button color="primary" size="large">Details</Button>
                            </TableCell>
                            <TableCell className={classes.cellIcon}>
                                <IconButton>
                                    <GetAppIcon />
                                </IconButton>
                            </TableCell>
                        </TableRow>
                    })}
                </TableBody>
            </Table>
        </Paper>
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <ListIcon />
                </Avatar>
                <Typography component="h1" variant="h4" className={classes.title}>Publication Request</Typography>
                <div className={classes.card} justify="center">
                    {publicationRequestCard}
                </div>
                {plagiarismInformation}
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    {form}
                    <Button type="submit" color="primary" className={classes.submit}
                        variant="contained" disabled={!formIsValid} fullWidth>
                        Confirm
                    </Button>
                </form>
            </div>
        </Container>
    );
};


const mapStateToProps = state => {
    return {
        selectedTask: state.tasks.selectedTask,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent,
        formFields: state.editorCheckIfBookIsPlagiarism.formFields,
        publicationRequest: state.editorCheckIfBookIsPlagiarism.publicationRequest,
        plagiarismInfo: state.editorCheckIfBookIsPlagiarism.plagiarismInfo,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchForm: (piId, taskId) => dispatch(actions.fetchForm(piId, taskId)),
        fetchPlagiarismInfo: (piId) => dispatch(actions.fetchPlagiarismInfo(piId)),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
        onConfirm: (data, taskId, history) => dispatch(actions.confirm(data, taskId, history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(EditorCheckIfBookIsPlagiarism);