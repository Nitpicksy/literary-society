import React, { useEffect } from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import Grid from "@material-ui/core/Grid";
import Container from "@material-ui/core/Container";
import Typography from "../Typography";
import Paypal from "../../images/paypal2.png";
import CreditCard from "../../images/credit-card.png";
import Bitcoin from "../../images/bitcoin.png";
import OtherMethods from "../../images/other-methods.png";

import * as actions from "./PaymentHomeActionsExport";
import { connect } from "react-redux";

const styles = (theme) => ({
  root: {
    display: "flex",
    backgroundColor: theme.palette.secondary.light,
    overflow: "hidden",
  },
  container: {
    marginTop: theme.spacing(10),
    marginBottom: theme.spacing(10),
    position: "relative",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  item: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    padding: theme.spacing(0, 5),
  },
  title: {
    marginBottom: theme.spacing(14),
  },
  number: {
    fontSize: 24,
    fontFamily: theme.typography.fontFamily,
    color: theme.palette.secondary.main,
    fontWeight: theme.typography.fontWeightMedium,
  },
  image: {
    height: 250,
    marginTop: theme.spacing(4),
    marginBottom: theme.spacing(4),
  },
  curvyLines: {
    pointerEvents: "none",
    position: "absolute",
    top: -180,
    opacity: 0.05,
  },
  button: {
    marginTop: theme.spacing(8),
  },
});

const PaymentHome = (props) => {
  const { classes } = props;
  const { fetchPaymentMethods } = props;

  useEffect(() => {
    fetchPaymentMethods(props.match.params[0]);
  }, [fetchPaymentMethods]);

  return (
    <section className={classes.root}>
      <Container className={classes.container}>
        <Typography
          variant="h4"
          marked="center"
          className={classes.title}
          component="h2"
        >
          Choose your payment method
        </Typography>
        <div>
          <Grid container spacing={5}>
            <Grid item xs={12} md={3}>
              <div className={classes.item}>
                <div className={classes.number}>1.</div>
                <img src={Paypal} alt="suitcase" className={classes.image} />
                <Typography variant="h5" align="center" component="h2">
                  Paypal
                </Typography>
              </div>
            </Grid>
            <Grid item xs={12} md={3}>
              <div className={classes.item}>
                <div className={classes.number}>2.</div>
                <img src={CreditCard} alt="graph" className={classes.image} />
                <Typography variant="h5" align="center">
                  Card transfer
                </Typography>
              </div>
            </Grid>
            <Grid item xs={12} md={3}>
              <div className={classes.item}>
                <div className={classes.number}>3.</div>
                <img
                  src={Bitcoin}
                  alt="clock"
                  className={classes.image}
                  onClick={activate}
                />
                <Typography variant="h5" align="center">
                  Bitcoin
                </Typography>
              </div>
            </Grid>
            <Grid item xs={12} md={3}>
              <div className={classes.item}>
                <div className={classes.number}>4.</div>
                <img src={OtherMethods} alt="clock" className={classes.image} />
                <Typography variant="h5" align="center">
                  Different payment methods
                </Typography>
              </div>
            </Grid>
          </Grid>
        </div>

        <Typography
          variant="h4"
          marked="center"
          className={classes.title}
          component="h2"
        >
          Payment details
          <div className={classes.item}>
            <Typography variant="h5" align="center">
              More details
            </Typography>
          </div>
        </Typography>
      </Container>
    </section>
  );
};

PaymentHome.propTypes = {
  classes: PropTypes.object.isRequired,
};

const mapDispatchToProps = (dispatch) => {
  return {
    fetchPaymentMethods: (transactionId) =>
      dispatch(actions.fetchPaymentMethods(transactionId)),
  };
};

export default connect(
  null,
  mapDispatchToProps
)(withStyles(styles)(PaymentHome));
