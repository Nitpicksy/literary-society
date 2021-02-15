import React, { useEffect, useState } from 'react';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import MenuBookIcon from '@material-ui/icons/MenuBook';
import { useStyles } from './SearchResultsStyles';
import { connect } from 'react-redux';
import { Avatar, Card, CardContent, CssBaseline, Grid, Container, Typography, Box, IconButton } from '@material-ui/core';
import SearchResultItem from './SearchResultItem/SearchResultItem';
import * as baseActions from '../BaseSearch/BaseSearchAction';
import * as advancedActions from '../AdvancedSearch/AdvancedSearchAction';
import { useHistory } from 'react-router';

const SearchResults = (props) => {
    const classes = useStyles();
    const history = useHistory();

    let [pageNumber, setPageNumber] = useState(1);
    let [searchText, setSearchText] = useState('');

    const pageSize = 4;
    let isLastPage = false;
    let resultItems = null;
    let header = null;

    const getSearchText = () => {
        const params = new URLSearchParams(props.location.search);
        setSearchText(params.get('q'));
    };

    useEffect(() => {
        if (!props.isAdvancedSearch) {
            getSearchText();
        }
        setPageNumber(parseInt(localStorage.getItem('pageNumber')));
    }, [props.isAdvancedSearch, getSearchText]);

    const nextPage = () => {
        let nextPageNum = parseInt(localStorage.getItem('pageNumber')) + 1;
        localStorage.setItem('pageNumber', nextPageNum);
        if (props.isAdvancedSearch) {
            props.advancedSearch(props.queryParams, nextPageNum, pageSize);
        } else {
            props.baseSearch(history, searchText, nextPageNum, pageSize);
        }
        setPageNumber(nextPageNum);
    };

    const previousPage = () => {
        let prevPageNumber = parseInt(localStorage.getItem('pageNumber')) - 1;
        localStorage.setItem('pageNumber', prevPageNumber);
        if (props.isAdvancedSearch) {
            props.advancedSearch(props.queryParams, prevPageNumber, pageSize);
        } else {
            props.baseSearch(history, searchText, prevPageNumber, pageSize);
        }
        setPageNumber(prevPageNumber);
    };

    if (props.results && Array.isArray(props.results) && props.results.length) {
        resultItems = props.results.map(item => {
            return <SearchResultItem key={item.id} resultItem={item}
                queryParams={props.isAdvancedSearch ? props.queryParams : null} />
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

    if (props.isAdvancedSearch) {
        header = <div className={classes.paper}>
            <Typography component="h1" variant="h4" className={classes.title}>Search results</Typography>
        </div>
    } else {
        header = <div className={classes.paper}>
            <Avatar className={classes.avatar}>
                <MenuBookIcon />
            </Avatar>
            <Typography component="h1" variant="h4" className={classes.title}>
                Search results for: <span className={classes.searchText}>{searchText}</span>
            </Typography>
        </div>
    }

    return (
        <Container component="main" maxWidth="lg">
            <CssBaseline />
            {header}
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
        baseSearch: (history, searchText, pageNum = 1, pageSize = 4) => dispatch(baseActions.baseSearch(history, searchText, pageNum, pageSize)),
        advancedSearch: (queryParams, pageNum = 1, pageSize = 4) => dispatch(advancedActions.advancedSearch(queryParams, pageNum, pageSize)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(SearchResults);