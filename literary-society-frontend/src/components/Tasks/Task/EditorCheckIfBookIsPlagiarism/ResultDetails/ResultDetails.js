import React, { useEffect, useState } from 'react';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import ListIcon from '@material-ui/icons/List';
import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';
import { connect } from 'react-redux';
import { useStyles } from './ResultDetailsStyle';
import * as actions from './ResultDetailsExport';
import * as signInActions from '../../../../Authentication/SignIn/SignInExport';
import { responseInterceptor } from '../../../../../responseInterceptor';
import { useHistory } from 'react-router';
import {  Grid } from '@material-ui/core';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardContent from '@material-ui/core/CardContent';

const ResultDetails = (props) => {
    const history = useHistory();
    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);
    const classes = useStyles();

    const { fetchPlagiarismInfo, resultDetails } = props;
    const [paperId, setPaperId] = useState(null);
    let results = null;
    const wordsPerPart = 950;

    useEffect(() => {
        const params = new URLSearchParams(props.location.search);
        fetchPlagiarismInfo(params.get('piId'));
        setPaperId(params.get('id'));
    }, [fetchPlagiarismInfo, props.location.search]);

    const getSimilarityValue = (papers) => {
        let similarity = null;

        for (let i in papers) {
            if (papers[i].id == paperId) {
                similarity = (papers[i].searchHits / papers[0].searchHits) * 100;
                break;
            }
        }
        return (
            similarity >= 50 ?
                <Typography className={classes.subTitle}>
                    Similarity:&nbsp;
            <span className={classes.similarProcentRed}>
                        {similarity.toFixed(2)}%
            </span>
                </Typography> :
                <Typography className={classes.subTitle}>
                    Similarity:&nbsp;
                    <span className={classes.content}>
                        {similarity.toFixed(2)}%
                    </span>
                </Typography>
        );
    }

    if (resultDetails && paperId) {
        results = resultDetails.items.map(result => {
            return <Grid item md={12}>
                <Card className={classes.root} key={result.id}>
                    <CardActionArea>
                        <CardContent className={classes.cardcontent}>
                            <Typography className={classes.subTitle}>
                                Part of document: &nbsp;
                                <span className={classes.content}>
                                    {(result.partOfPage - 1) * wordsPerPart + 1} -&nbsp;
                                {(result.partOfPage * wordsPerPart)} words
                                </span>

                            </Typography>
                            <Typography className={classes.subTitle}>
                                Text: &nbsp;
                                <span className={classes.content}>
                                    {result.textToShow} ...
                                </span>
                            </Typography>
                            {getSimilarityValue(result.papers)}
                        </CardContent>
                    </CardActionArea>
                </Card>
            </Grid>
        });
    }

    return (
        <Container component="main" maxWidth="sm">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <ListIcon />
                </Avatar>
                <Typography component="h1" variant="h4" className={classes.title}>Results</Typography>
            </div>
            <Grid container spacing={2} align="center" justify="center" style={{ marginTop: 10 }} >
                {results}
            </Grid>
        </Container >
    );
};


const mapStateToProps = state => {
    return {
        resultDetails: state.resultDetails.plagiarismInfo
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchPlagiarismInfo: (piId) => dispatch(actions.fetchPlagiarismInfo(piId)),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(ResultDetails);