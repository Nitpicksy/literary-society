import React, { useEffect, useState } from 'react';
import { Container, Typography, Grid, Avatar, CssBaseline, Card, CardContent } from '@material-ui/core';
import ListAltIcon from '@material-ui/icons/ListAlt';
import { useStyles } from './PaperResultDetailsStyles';
import { connect } from 'react-redux';
import * as actions from './PaperResultDetailsActions';

const PaperResultDetails = (props) => {
    const classes = useStyles();

    const { fetchPaperResultDetails } = props;
    const [paperId, setPaperId] = useState(null);
    const wordsPerPart = 950;
    let resultDetails = null;

    useEffect(() => {
        const params = new URLSearchParams(props.location.search);
        setPaperId(parseInt(params.get('paperId')));
        fetchPaperResultDetails(params.get('piId'));
    }, [fetchPaperResultDetails, props.location.search]);

    const calculateSimilarity = (papers) => {
        let similarity = 0;
        let chosenPaper = papers.find(paper => paper.id === paperId);
        if (chosenPaper) {
            similarity = (chosenPaper.searchHits / papers[0].searchHits) * 100;
        }
        if (similarity >= 50) {
            return <span className={classes.red}>{similarity.toFixed(2)}%</span>;
        } else {
            return <span className={classes.similarity}>{similarity.toFixed(2)}%</span>;
        }
    }

    if (props.paperResultDetails && paperId) {
        resultDetails = props.paperResultDetails.map(detailsPart => {
            return <Grid item sm={12} key={detailsPart.partOfPage}>
                <Card>
                    <CardContent className={classes.cardContent}>
                        <Typography className={classes.boldFont}>
                            <span className={classes.header}>Document section: </span>
                            {(detailsPart.partOfPage - 1) * wordsPerPart + 1}&nbsp;-&nbsp;{(detailsPart.partOfPage * wordsPerPart)} word
                        </Typography>
                        <Typography className={classes.boldFont}>
                            <span className={classes.header}>Text: </span>
                            ... {detailsPart.textToShow}...
                        </Typography>
                        <Typography className={classes.boldFont}>
                            <span className={classes.header}>Similarity: </span>
                            {calculateSimilarity(detailsPart.papers)}
                        </Typography>
                    </CardContent>
                </Card>
            </Grid>
        });
    }

    return (
        <Container component="main" maxWidth="sm" spacing={1}>
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <ListAltIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Result Details</Typography>
            </div>
            <Grid container spacing={2} align="center" justify="center" className={classes.grid}>
                {resultDetails}
            </Grid>
        </Container>
    );
};


const mapStateToProps = state => {
    return {
        paperResultDetails: state.paperResultDetails.paperResultDetails
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchPaperResultDetails: (piId) => dispatch(actions.fetchPaperResultDetails(piId)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PaperResultDetails);