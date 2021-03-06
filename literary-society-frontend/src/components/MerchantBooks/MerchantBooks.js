import React, { useEffect, useState } from 'react';
import * as actions from './MerchantBooksActions';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import BookCard from './MerchantBookCard/MerchantBookCard';
import Grid from '@material-ui/core/Grid';
import LinearProgress from '@material-ui/core/LinearProgress';
import MenuBookIcon from '@material-ui/icons/MenuBook';
import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import { useStyles } from './MerchantBooksStyles';
import { connect } from 'react-redux';
import { Button } from '@material-ui/core';
import AddIcon from '@material-ui/icons/Add';
import { useHistory } from 'react-router';

const MerchantBooks = (props) => {
    const history = useHistory();
    const [loading, setLoading] = useState(true);
    const classes = useStyles();
    const { fetchBooks } = props;
    const {onFetchGenres} = props;
    
    let bookCards = null;

    useEffect(() => {
        fetchBooks();
        onFetchGenres();
        setLoading(false);
    }, [fetchBooks,onFetchGenres]);

    const showForm = () => {
        history.push('/add-book');
    }

    if (!loading) {
        if (props.books && Array.isArray(props.books) && props.books.length) {
            bookCards = props.books.map(book => {
                return <BookCard key={book.id} book={book} forShoppingCart={false} />
            });
        } else {
            bookCards =
                <Card className={classes.card}>
                    <CardContent className={classes.cardContent}>
                        <Typography component="h3" variant="h5">No available books for sale at the moment.</Typography>
                    </CardContent>
                </Card>;
        }
    } else {
        <LinearProgress />
    }

    return (
        <Container component="main" maxWidth="lg">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <MenuBookIcon />
                </Avatar>
                <Typography component="h1" variant="h4"  className={classes.title}>Your books</Typography>
                <Button variant="contained" color="primary"
                    startIcon={<AddIcon />} onClick={showForm}> Create </Button>
            </div>
            <Grid container spacing={3} align="center" justify="center">
                {bookCards}
            </Grid>
        </Container>
    );
};


const mapStateToProps = state => {
    return {
        books: state.merchantBookList.books
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchBooks: () => dispatch(actions.fetchBooks()),
        onFetchGenres: () => dispatch(actions.fetchGenres())
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(MerchantBooks);