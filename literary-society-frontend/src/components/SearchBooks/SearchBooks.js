import React, { useEffect, useState } from 'react';
import * as actions from './SearchBooksActions';
import LinearProgress from '@material-ui/core/LinearProgress';
import MenuBookIcon from '@material-ui/icons/MenuBook';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import { useStyles } from './SearchBooksStyles';
import { connect } from 'react-redux';
import SearchBar from "material-ui-search-bar";
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import SearchIcon from '@material-ui/icons/Search';
import { CssBaseline, Button, Typography, Container, Avatar, Grid, Paper } from '@material-ui/core';
import AdvancedSearch from './AdvancedSearch/AdvancedSearch';

const SearchBooks = (props) => {

    const classes = useStyles();
    const [page, setPage] = useState(0);
    const [isLastPage, setIsLastPage] = useState(0);
    const [searchValue, setSearchValue] = useState("");
    const [enableAdvancedSearch, setEnableAdvancedSearch] = useState(false);
    const [advanceSearchValues, setAdvanceSearchValues] = useState([
        {
            "id": 0,
            "attributeName":"title",
            "searchValue":"",
            "phraseQuery":false,
            "type": "AND"
        }
    ]);

    const { fetchBooks } = props;
    let bookCards = null;

    // if (props.books && Array.isArray(props.books) && props.books.length) {
    //     bookCards = props.books.map(book => {
    //         return <BookCard key={book.id} book={book} forShoppingCart={false} />
    //     });
    // } else {
    //     bookCards =
    //         <Card className={classes.card}>
    //             <CardContent className={classes.cardContent}>
    //                 <Typography component="h3" variant="h5">No available books for sale at the moment.</Typography>
    //             </CardContent>
    //         </Card>;
    // }

    const searchAllValue = () => {
        console.log(searchValue)
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
                        onRequestSearch={searchAllValue} />
                </Grid>
                <Grid item md={3}>
                    <Button type="submit" className={classes.button} variant="outlined" startIcon={<ExpandMoreIcon />}
                        onClick={() => setEnableAdvancedSearch(!enableAdvancedSearch)}>
                        Advanced search</Button>
                </Grid>
                <Grid item md={1}></Grid>
            </Grid>
            {enableAdvancedSearch ? <AdvancedSearch  advanceSearchValues = {advanceSearchValues} 
            setAdvanceSearchValues = {setAdvanceSearchValues} /> : null}

            {/* <Grid container spacing={3} align="center" justify="center">
                {bookCards}
            </Grid> */}
        </Container>
    );
};


// const mapStateToProps = state => {
//     return {
//         books: state.homePage.books
//     }
// };

// const mapDispatchToProps = dispatch => {
//     return {
//         fetchBooks: () => dispatch(actions.fetchBooks()),
//     }
// };

// export default connect(mapStateToProps, mapDispatchToProps)(SearchBooks);

export default SearchBooks;