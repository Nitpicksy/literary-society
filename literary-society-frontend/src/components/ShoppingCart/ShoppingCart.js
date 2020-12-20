import React, { useCallback, useEffect, useState } from "react";
import * as actions from "./ShoppingCartExport";
import Typography from "@material-ui/core/Typography";
import Container from "@material-ui/core/Container";
import Grid from "@material-ui/core/Grid";
import LinearProgress from "@material-ui/core/LinearProgress";
import MenuBookIcon from "@material-ui/icons/MenuBook";
import Avatar from "@material-ui/core/Avatar";
import CssBaseline from "@material-ui/core/CssBaseline";
import { useStyles } from "./ShoppingCartStyles";
import BookCard from "../BookCard/BookCard";
import { Button, MenuItem, TextField } from "@material-ui/core";
import { connect } from "react-redux";
import { responseInterceptor } from "../../responseInterceptor";
import * as signInActions from "../Authentication/SignIn/SignInExport";
import { useHistory } from "react-router";
import { toastr } from 'react-redux-toastr';

const ShoppingCart = (props) => {
  const history = useHistory();
  const [loading, setLoading] = useState(true);
  const [merchants, setMerchants] = useState(null);
  const [shoppingCart, setShoppingCart] = useState(null);
  const [books, setBooks] = useState(null);
  const [selectedMerchant, setSelectedMerchant] = useState(null);
  const [amount, setAmount] = useState(null);
  const classes = useStyles();

  let bookCards = null;
  let select = null;
  let payButton = null;

  responseInterceptor.setupInterceptor(
    history,
    props.refreshTokenRequestSent,
    props.onRefreshToken
  );

  const setSelectedMerchantAndBooks = useCallback(
    (shoppingCartMap, selectedMerchant) => {
      setBooks(shoppingCartMap.get(selectedMerchant));
      calculatePrice(shoppingCartMap.get(selectedMerchant));
      setSelectedMerchant(selectedMerchant);
    },
    []
  );

  useEffect(() => {
    const map = new Map(JSON.parse(localStorage.getItem("shoppingCart")));
    const merchantsArray = Array.from(map.keys());
    setShoppingCart(map);
    setMerchants(merchantsArray);

    setSelectedMerchantAndBooks(map, merchantsArray[0]);
    setLoading(false);
    toastr.success('Shopping cart', "If you paid this month's membership, discount will be applied when you proceed to payment.");
    
  }, [setSelectedMerchantAndBooks]);

  const inputChangedHandler = (event) => {
    const value = event.target.value;
    setSelectedMerchantAndBooks(shoppingCart, value);
  };

  const calculatePrice = (books) => {
    if (books) {
      let price = 0;
      for (let book of books) {
        price += book.price;
      }
      setAmount(price);
    }
  };

  const removeItem = (book) => {
    const map = new Map(JSON.parse(localStorage.getItem("shoppingCart")));

    let merchantBooks = map.get(book.merchantName);
    for (var count = 0; count < merchantBooks.length; count++) {
      if (merchantBooks[count].id === book.id) {
        break;
      }
    }
    merchantBooks.splice(count, 1);

    if (merchantBooks.length > 0) {
      map.set(book.merchantName, merchantBooks);
      setSelectedMerchantAndBooks(map, selectedMerchant);
    } else {
      map.delete(book.merchantName);
      const merchantsArray = Array.from(map.keys());
      setMerchants(merchantsArray);
      setSelectedMerchant(merchantsArray[0]);
      setSelectedMerchantAndBooks(map, merchantsArray[0]);
    }

    localStorage.setItem(
      "shoppingCart",
      JSON.stringify(Array.from(map.entries()))
    );
    setShoppingCart(map);
  };

  const onProceedToPayment = () => {
    props.onProceedToPayment(books);
  };

  if (!loading) {
    if (books) {
      bookCards = books.map((book, index) => {
        return (
          <BookCard
            key={index}
            book={book}
            forShoppingCart={true}
            removeItem={(book) => removeItem(book)}
          />
        );
      });
    } else {
      bookCards = (
        <Typography component="h3" variant="h6">
          Empty Cart
        </Typography>
      );
    }

    if (merchants.length > 0) {
      select = (
        <TextField
          variant="outlined"
          margin="normal"
          select
          className={classes.selectMerchant}
          label="Merchant"
          value={selectedMerchant}
          onChange={(event) => inputChangedHandler(event)}
        >
          {merchants.map((merchantName) => (
            <MenuItem key={merchantName} value={merchantName}>
              {merchantName}
            </MenuItem>
          ))}
        </TextField>
      );
    }
  } else {
    <LinearProgress />;
  }

  if (books && books.length > 0) {
    payButton = (
      <Grid container>
        <Grid item xs={7}>
          &nbsp;
        </Grid>
        <Grid item xs={3} className={classes.price}>
          <Typography component="h1" variant="h4">
            Total:{" "}
            <span className={classes.priceValue}>{amount.toFixed(2)} din.</span>
          </Typography>
        </Grid>
        <Grid item xs={2}>
          <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained" 
              onClick={onProceedToPayment} title="If you paid this month's membership, discount will be applied when you proceed to payment." >
              Proceed to payment{" "}
          </Button>
        </Grid>
      </Grid>
    );
  }

  return (
    <Container component="main" maxWidth="lg">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <MenuBookIcon />
        </Avatar>
        <Typography component="h1" variant="h4">
          Shopping cart
        </Typography>
        {select}
      </div>
      <Grid container spacing={3} align="center">
        {bookCards}
      </Grid>
      {payButton}
    </Container>
  );
};

const mapDispatchToProps = (dispatch) => {
  return {
    onProceedToPayment: (choosenBooks) =>
      actions.proceedToPayment(choosenBooks),
    onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
  };
};

export default connect(null, mapDispatchToProps)(ShoppingCart);

