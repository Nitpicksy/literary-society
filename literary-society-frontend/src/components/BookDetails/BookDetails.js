import React, { useEffect, useState } from 'react';
import * as actions from './BookDetailsActions';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import MenuBookIcon from '@material-ui/icons/MenuBook';
import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';
import { useStyles } from './BookDetailsStyles';
import { connect } from 'react-redux';
import { useHistory } from 'react-router';
import Image from 'material-ui-image';
import { Button, Chip, Paper, Tooltip } from '@material-ui/core';
import { toastr } from 'react-redux-toastr';

const BookDetails = (props) => {
    const history = useHistory();
    const [loading, setLoading] = useState(true);
    const classes = useStyles();
    const { fetchBook } = props;
    let discount = null;
    let bookData = null;
    let button = null;
    let price = null;

    useEffect(() => {
        const { id } = props.match.params;
        if (id) {
            fetchBook(id, history);
            setLoading(false);
        }

    }, [fetchBook, props.match.params, history]);

    const addToCart = () => {
        let shoppingCart = new Map(JSON.parse(localStorage.getItem('shoppingCart')));

        if (!shoppingCart) {
            shoppingCart = new Map();
        }

        let merchantBooks = shoppingCart.get(props.bookDetails.bookDTO.merchantName);
        if (!merchantBooks) {
            merchantBooks = [];
        }

        const found = merchantBooks.find(element => element.id === props.bookDetails.bookDTO.id);
        if (!found) {
            merchantBooks.push(props.bookDetails.bookDTO);
            shoppingCart.set(props.bookDetails.bookDTO.merchantName, merchantBooks);
            toastr.success('Shopping cart', 'Successfully added book in shopping cart');
            localStorage.setItem('shoppingCart', JSON.stringify(Array.from(shoppingCart.entries())));
        } else {
            toastr.error('Shopping cart', 'This book is already added');
        }
    }

    const download = () => {
        props.download(props.bookDetails.bookDTO.id, props.bookDetails.bookDTO.title);
    }

    if (!loading && props.bookDetails) {

        button = <Button size="large" color="primary" type="submit" variant="contained" onClick={addToCart}>Add to cart</Button>;

        price =
            <Typography className={classes.price}>
                {props.bookDetails.bookDTO.price.toFixed(2)} din.
            </Typography>;

        if (props.bookDetails.bookDTO.price <= 0) {
            button = <Button size="large" color="secondary" onClick={download} variant="contained">Get for free</Button>;
            price = null;
        }

        if (props.bookDetails.bookDTO.discount > 0) {
            discount =
                <Tooltip className={classes.tooltip} placement="top-end"
                    title={<span>Discount is applied to the shown<br />price if membership is payed.</span>}>
                    <Chip label={props.bookDetails.bookDTO.discount + '%'} classes={{ root: classes.discount }} />
                </Tooltip>
        }

        bookData =
            <Paper className={classes.paper}>
                <Grid container>
                    <Grid item md={4} sm={3} xs={12} >
                        <div className={classes.imageDiv} >
                            <Image aspectRatio={0.70} src={props.bookDetails.bookDTO.imageData} />
                            {discount}
                        </div>
                    </Grid>
                    <Grid item md={8} sm={9} xs={12} className={classes.bookInfo}>
                        <Typography className={classes.title}>{props.bookDetails.bookDTO.title}</Typography>
                        <Grid container>
                            <Grid item md={8} sm={6} xs={12}>
                                <Typography className={classes.genre}>{props.bookDetails.genreName.toUpperCase()}</Typography>
                                <Typography className={classes.isbn}>ISBN:&nbsp; {props.bookDetails.isbn}</Typography>
                                <Typography className={classes.publicationDate}>Publication Date:&nbsp; {props.bookDetails.publicationDate}</Typography>
                                <Typography className={classes.publisherCity}>Publication City:&nbsp; {props.bookDetails.publisherCity}</Typography>
                            </Grid>
                            <Grid item md={4} sm={6} xs={12} className={classes.priceGrid}>
                                {price}
                                {button}
                            </Grid>
                        </Grid>
                        <Typography className={classes.writer}>Writer:&nbsp; {props.bookDetails.bookDTO.writersNames}</Typography>
                        <Typography className={classes.publisher}>Publisher:&nbsp; {props.bookDetails.publisher}</Typography>
                        <Typography className={classes.merchant}>Merchant:&nbsp; {props.bookDetails.bookDTO.merchantName}</Typography>
                        <Typography className={classes.synopsis}>{props.bookDetails.synopsis}</Typography>
                    </Grid>
                </Grid>
            </Paper>;
    } else {
        bookData =
            <div>
                <Avatar className={classes.avatar}>
                    <MenuBookIcon />
                </Avatar>
                <Typography component="h1" variant="h4">Loading...</Typography>
            </div>;
    }



    return (
        <Container component="main" maxWidth="md" spacing={2}>
            <CssBaseline />
            <div className={classes.bookData}>
                {bookData}
            </div>

        </Container>
    );
};


const mapStateToProps = state => {
    return {
        bookDetails: state.book.book
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchBook: (id, history, shouldAddToCart = false) => dispatch(actions.fetchBook(id, history, shouldAddToCart)),
        download: (id, title) => dispatch(actions.download(id, title))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(BookDetails);