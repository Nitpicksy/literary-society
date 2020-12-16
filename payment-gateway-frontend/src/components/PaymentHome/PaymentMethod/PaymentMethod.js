import React from "react";

import Grid from "@material-ui/core/Grid";
import Typography from "../../Typography";

import paypal from "../../../images/paypal.png";
import bank from "../../../images/bank.png";
import bitcoin from "../../../images/bitcoin.png";
import otherMethods from "../../../images/other-methods.png";

const PaymentMethod = (props) => {
  const { classes } = props;

  function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
  }

  let img = null;

  switch (props.method.commonName) {
    case "paypal":
      img = (
        <img
          src={paypal}
          alt="suitcase"
          className={classes.image}
          onClick={() => props.clicked(props.method)}
        />
      );
      break;
    case "bank":
      img = (
        <img
          src={bank}
          alt="suitcase"
          className={classes.image}
          onClick={() => props.clicked(props.method)}
        />
      );
      break;
    case "bitcoin":
      img = (
        <img
          src={bitcoin}
          alt="suitcase"
          className={classes.image}
          onClick={() => props.clicked(props.method)}
        />
      );
      break;
    default:
      img = (
        <img
          src={otherMethods}
          alt="suitcase"
          className={classes.image}
          onClick={() => props.clicked(props.method)}
        />
      );
  }

  return (
    <Grid item xs={12} md={Math.round(12 / props.length)}>
      <div className={classes.item}>
        <div className={classes.number}>{props.number + 1}</div>
        {img}
        <Typography variant="h5" align="center" component="h2">
          {capitalizeFirstLetter(props.method.commonName)}
        </Typography>
      </div>
    </Grid>
  );
};

export default PaymentMethod;
