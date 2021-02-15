import React from 'react';
import { useStyles } from './SearchResultItemStyles';
import { Button, Typography, Grid, Card, CardContent, Link, CardActions } from '@material-ui/core';
import { toastr } from 'react-redux-toastr';
import { useHistory } from 'react-router';
import { connect } from 'react-redux';
import Parser from 'html-react-parser';
import * as actions from '../../../BookDetails/BookDetailsActions';

const SearchResultItem = (props) => {
    const classes = useStyles();
    const history = useHistory();

    const fetchBook = () => {
        props.fetchBook(props.resultItem.id, history);
    }

    const addToCart = () => {
        fetchBook();
        let shoppingCart = new Map(JSON.parse(localStorage.getItem('shoppingCart')));

        if (!shoppingCart) {
            shoppingCart = new Map();
        }

        let merchantBooks = shoppingCart.get(props.book.merchantName);
        if (!merchantBooks) {
            merchantBooks = [];
        }

        const found = merchantBooks.find(element => element.id === props.book.id);
        if (!found) {
            merchantBooks.push(props.book);
            shoppingCart.set(props.book.merchantName, merchantBooks);
            toastr.success('Shopping cart', 'Successfully added book in shopping cart');
            localStorage.setItem('shoppingCart', JSON.stringify(Array.from(shoppingCart.entries())));
        } else {
            toastr.error('Shopping cart', 'This book is already added');
        }
    }

    const download = () => {
        props.download(props.resultItem.id, props.resultItem.title);
    }

    let button = <Button size="small" color="primary" onClick={addToCart}>Add to cart</Button>;
    if (props.resultItem.openAccess) {
        button = <Button size="small" color="secondary" onClick={download}>Get for free</Button>;
    }

    const showDetails = () => {
        history.push(`/book/${props.resultItem.id}`);
    }

    return (
        <Grid item md={8}>
            <Card className={classes.card}>
                <CardContent>
                    <Typography component="h2" variant="h5">
                        <Link href="" onClick={() => showDetails()}>{props.resultItem.title}</Link>
                    </Typography>
                    <Typography variant="subtitle1" color="textSecondary">
                        {props.resultItem.writers} &nbsp;▪&nbsp; {props.resultItem.genre.name}
                    </Typography>
                    <Typography variant="subtitle1" paragraph className={classes.text}>
                        <span>{Parser(props.resultItem.text)}</span>
                    </Typography>
                </CardContent>
                <CardActions>
                    {button}
                </CardActions>
            </Card>
        </Grid>
    );
}

const mapDispatchToProps = dispatch => {
    return {
        download: (id, title) => dispatch(actions.download(id, title)),
        fetchBook: (id, history) => dispatch(actions.fetchBook(id, history)),
    }
};

export default connect(null, mapDispatchToProps)(SearchResultItem);