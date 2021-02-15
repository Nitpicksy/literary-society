import React, { useEffect, useState } from 'react';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import MenuBookIcon from '@material-ui/icons/MenuBook';
import { useStyles } from './SearchResultsStyles';
import { connect } from 'react-redux';
import { Avatar, Card, CardContent, CssBaseline, Grid, Container, Typography, Box, IconButton } from '@material-ui/core';
import SearchResultItem from './SearchResultItem/SearchResultItem';
import * as actions from '../BaseSearch/BaseSearchAction';
import { useHistory } from 'react-router';

const SearchResults = (props) => {
    const classes = useStyles();
    const history = useHistory();
    let [pageNumber, setPageNumber] = useState(1);
    let [searchText, setSearchText] = useState('');
    const pageSize = 4;
    let isLastPage = false;
    let resultItems = null;

    useEffect(() => {
        const params = new URLSearchParams(props.location.search);
        setSearchText(params.get('q'));
        setPageNumber(parseInt(localStorage.getItem('pageNumber')));
    }, [props.location.search]);

    const nextPage = () => {
        let nextPageNum = parseInt(localStorage.getItem('pageNumber')) + 1;
        localStorage.setItem('pageNumber', nextPageNum);
        props.baseSearch(history, searchText, nextPageNum, pageSize);
        setPageNumber(nextPageNum);
    };

    const previousPage = () => {
        let prevPageNumber = parseInt(localStorage.getItem('pageNumber')) - 1;
        localStorage.setItem('pageNumber', prevPageNumber);
        props.baseSearch(history, searchText, prevPageNumber, pageSize);
        setPageNumber(prevPageNumber);
    };

    if (props.results && Array.isArray(props.results) && props.results.length) {
        resultItems = props.results.map(item => {
            return <SearchResultItem key={item.id} resultItem={item} />
        });
        props.results.length < pageSize ? isLastPage = true : isLastPage = false;
    } else {
        resultItems =
            <Card className={classes.card}>
                <CardContent className={classes.cardContent}>
                    <Typography component="h3" variant="h5">No results for specified search query.</Typography>
                </CardContent>
            </Card>;
        isLastPage = true;
    }

    return (
        <Container component="main" maxWidth="lg">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <MenuBookIcon />
                </Avatar>
                <Typography component="h1" variant="h4" className={classes.title}>
                    Search results for: <span className={classes.searchText}>{searchText}</span>
                </Typography>
            </div>
            <Grid container justify="center">
                {resultItems}
            </Grid>
            <Box display="flex" justifyContent="center" alignItems="center" className={classes.pagination}>
                <IconButton aria-label="prevoius" component="span" onClick={previousPage} disabled={pageNumber === 1}>
                    <ChevronLeftIcon />
                </IconButton>
                <Typography variant="subtitle1" className={classes.pageNum}>{pageNumber}</Typography>
                <IconButton aria-label="next" component="span" onClick={nextPage} disabled={isLastPage}>
                    <ChevronRightIcon />
                </IconButton>
            </Box>
        </Container>
    );
};


const mapStateToProps = state => {
    return {
        results: state.searchResults.results
    }
};

const mapDispatchToProps = dispatch => {
    return {
        baseSearch: (history, searchText, pageNum = 1, pageSize = 5) => dispatch(actions.baseSearch(history, searchText, pageNum, pageSize)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(SearchResults);