import React from "react";

import Grid from "@material-ui/core/Grid";
import Typography from "../Typography";

import paypal from "../../images/paypal2.png";
import bank from "../../images/credit-card.png";
import bitcoin from "../../images/bitcoin.png";
import otherMethods from "../../images/other-methods.png";

const PaymentMethod = (props) => {
  const { classes } = props;

  return (
    <Grid item xs={12} md={3}>
      <div className={classes.item}>
        <div className={classes.number}>1.</div>
        <img src={Paypal} alt="suitcase" className={classes.image} />
        <Typography variant="h5" align="center" component="h2">
          Paypal
        </Typography>
      </div>
    </Grid>
  );
};

export default PaymentMethod;
