import React, { useEffect, useState } from 'react';
import * as actions from './HomePageActions';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import BookCard from '../BookCard/BookCard';
import Grid from '@material-ui/core/Grid';
import LinearProgress from '@material-ui/core/LinearProgress';
import MenuBookIcon from '@material-ui/icons/MenuBook';
import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import { useStyles } from './HomePageStyles';
import { connect } from 'react-redux';

const HomePage = (props) => {

    const [loading, setLoading] = useState(true);
    const classes = useStyles();
    const { fetchBooks } = props;
    let bookCards = null;

    useEffect(() => {
        fetchBooks();
        setLoading(false);
    }, [fetchBooks]);

    if (!loading) {
        if (props.books && Array.isArray(props.books) && props.books.length) {
            bookCards = props.books.map(book => {
                return <BookCard key={book.id} book={book} forShoppingCart = {false}/>
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
                <Typography component="h1" variant="h4">Available books</Typography>
            </div>
            <Grid container spacing={3} align="center" justify="center">
                {bookCards}
            </Grid>
        </Container>
    );
};


const mapStateToProps = state => {
    return {
        books: state.homePage.books
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchBooks: () => dispatch(actions.fetchBooks()),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(HomePage);