import React, { useEffect, useState } from 'react';
import * as actions from './SearchBooksActions';
import MenuBookIcon from '@material-ui/icons/MenuBook';
import { useStyles } from './SearchBooksStyles';
import { connect } from 'react-redux';
import SearchBar from "material-ui-search-bar";
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import { CssBaseline, Button, Typography, Container, Avatar, Grid, Paper, IconButton, Box } from '@material-ui/core';
import AdvancedSearch from './AdvancedSearch/AdvancedSearch';
import SearchResult from './SearchResult/SearchResult';

const SearchBooks = (props) => {

    const classes = useStyles();
    const [page, setPage] = useState(0);
    const [searchAllValue, setSearchAllValue] = useState(false);

    const [searchValue, setSearchValue] = useState("");
    const [enableAdvancedSearch, setEnableAdvancedSearch] = useState(false);
    const [advanceSearchValues, setAdvanceSearchValues] = useState([
        {
            "id": 0,
            "attributeName": "title",
            "searchValue": "",
            "phraseQuery": false,
            "type": "AND"
        }
    ]);

    const { books } = props;
    let bookCards = null;

    useEffect(() => {
        const search = JSON.parse(localStorage.getItem("searchValue"))
        const advancedSearch = JSON.parse(localStorage.getItem("advanceSearchValues"))
        const pageNum = JSON.parse(localStorage.getItem("page"))
        if (search) {
            setSearchValue(search);
        }
        if (advancedSearch && advancedSearch.length > 0) {
            setAdvanceSearchValues(advancedSearch);
        }

        if(pageNum){
            setPage(pageNum);
        }
        localStorage.removeItem("searchValue")
        localStorage.removeItem("advanceSearchValues")
        localStorage.removeItem("page")
    }, []);

    if (books && Array.isArray(books) && books.length) {
        bookCards = books.map(book => {
            return <SearchResult key={book.id} book={book} forShoppingCart={false} 
            searchValue={searchValue} advanceSearchValues={advanceSearchValues} page = {page} />
        });
    }

    const search = (type) => {
        setPage(0);
        if (type === "searchAllValue") {
            setSearchAllValue(true);
            props.searchAll(page, searchValue);
        } else {
            props.combineSearchParams(page, advanceSearchValues);
            setSearchAllValue(false);
            setSearchValue("");
            setEnableAdvancedSearch(false);
        }
    }

    const next = () => {
        setPage(page + 1);
        if (searchAllValue) {
            props.searchAll(page + 1, searchValue);
        } else {
            props.combineSearchParams(page + 1, advanceSearchValues);
        }
    }

    const previous = () => {
        if (searchAllValue) {
            props.searchAll(page - 1, searchValue);
        } else {
            props.combineSearchParams(page - 1, advanceSearchValues);
        }
    }
    let pagination = null;
    if (books && books.length > 0) {
        pagination = <Box display="flex" justifyContent="center" alignItems="center" style={{ marginTop: 10 }}>
            {page === 0 ? null : <IconButton aria-label="prevoius" component="span"
             onClick={() => previous()}>
                <ChevronLeftIcon />
            </IconButton>}

            <Typography variant="subtitle1" className={classes.pageNum}>{page + 1}</Typography>
            {books && books.length < 4 ? null : <IconButton aria-label="next" component="span"
             onClick={() => next()}>
                <ChevronRightIcon />
            </IconButton>}

        </Box>;
    }

    return (
        <Container component="main" maxWidth="lg">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <MenuBookIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Available books</Typography>
            </div>
            <Grid container align="center" className={classes.grid}>
                <Grid item md={2}></Grid>
                <Grid item md={6}>
                    <SearchBar
                        value={searchValue}
                        onChange={(newValue) => setSearchValue(newValue)}
                        onRequestSearch={() => search("searchAllValue")} />
                </Grid>
                <Grid item md={3}>
                    <Button type="submit" className={classes.button} variant="outlined" startIcon={<ExpandMoreIcon />}
                        onClick={() => setEnableAdvancedSearch(!enableAdvancedSearch)}>
                        Advanced search</Button>
                </Grid>
                <Grid item md={1}></Grid>
            </Grid>
            {enableAdvancedSearch ? <AdvancedSearch advanceSearchValues={advanceSearchValues}
                setAdvanceSearchValues={setAdvanceSearchValues} search={() => search("combineSearch")} /> : null}

            <Grid container spacing={3} align="center" justify="center" style={{ marginTop: 10 }} >
                {bookCards}
            </Grid>
            {pagination}
        </Container>
    );
};


const mapStateToProps = state => {
    return {
        books: state.searchBooks.books
    }
};

const mapDispatchToProps = dispatch => {
    return {
        searchAll: (page, searchValue) => dispatch(actions.searchAll(page, searchValue)),
        combineSearchParams: (page, searchParams) => dispatch(actions.combineSearchParams(page, searchParams)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(SearchBooks);
