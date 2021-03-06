import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import Grid from "@material-ui/core/Grid";
import Container from "@material-ui/core/Container";
import Typography from "../Typography";

import * as actions from "./PaymentHomeActionsExport";
import { connect } from "react-redux";
import PaymentMethod from "./PaymentMethod/PaymentMethod";
import { Paper } from "@material-ui/core";

const styles = (theme) => ({
  root: {
    display: "flex",
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
    marginBottom: theme.spacing(3)
  },
  details: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    marginTop: theme.spacing(3),
    padding: theme.spacing(2, 3),
    backgroundColor: '#f5f5f5',
  },
  detailsText: {
    fontSize: 24,
    fontWeight: 500,
    marginBottom: theme.spacing(1)
  },
  number: {
    fontSize: 24,
    fontFamily: theme.typography.fontFamily,
    color: theme.palette.secondary.main,
    fontWeight: theme.typography.fontWeightMedium,
  },
  image: {
    height: 120,
    marginTop: theme.spacing(4),
    marginBottom: theme.spacing(4),
    cursor: "grabbing",
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
  const { fetchPaymentDetails } = props;
  const { forwardPayment } = props;

  const [loading, setLoading] = useState(true);

  let methods = null;
  let details = null;

  useEffect(() => {
    fetchPaymentMethods(props.match.params.id);
    fetchPaymentDetails(props.match.params.id);
    setLoading(false)
  }, [fetchPaymentMethods, fetchPaymentDetails, props.match.params.id]);

  //this function forwards the request to the appropriate payment service (bank,paypal,bitcoin,etc.)
  const clicked = (method) => {
    const request = {
      orderId: props.match.params.id,
      paymentCommonName: method.commonName,
    };

    forwardPayment(request);
  };

  if (props.paymentMethods) {
    if (props.paymentDetails) {
      details = (
        <Paper className={classes.details} variant="outlined">
          <Typography variant="body1" className={classes.detailsText}>
            Price: {props.paymentDetails.amount} din.
          </Typography>
          <Typography variant="body1" className={classes.detailsText}>
            {props.paymentDetails.merchantName} in{" "}
            {props.paymentDetails.companyName}
          </Typography>
        </Paper>
      );
    }

    const paymentMethod = props.paymentMethods.sort(({ id: previousID }, { id: currentID }) => previousID - currentID).map((method, index) => {
      return (
        <PaymentMethod
          key={index}
          number={index}
          method={method}
          length={props.paymentMethods.length}
          clicked={clicked}
          {...props}
        />
      );
    })

    paymentMethod.sort(function (a, b) {
      if (a.value > b.value) {
        return 1;
      }
      if (a.value < b.value) {
        return -1;
      }
      return 0;
    });

    if (!loading) {
      methods = (
        <Container className={classes.container}>
          <Typography
            variant="h4"
            className={classes.title}
            component="h2"
          >
            Choose your payment method
          </Typography>
          <div>
            <Grid container spacing={5}>
              {paymentMethod}
            </Grid>
          </div>

          {details}
        </Container>
      );
    } else {
      methods = <h1> No payment methods found for given literary society.</h1>;
    }
  }


  return <section className={classes.root}>
    {!loading ? methods : <Typography variant="h4"
      marked="center"
      className={classes.title}
      component="h2"> Loading... </Typography>}
  </section>;
};

PaymentHome.propTypes = {
  classes: PropTypes.object.isRequired,
};

const mapStateToProps = (state) => {
  return {
    paymentMethods: state.paymentHome.paymentMethods,
    paymentDetails: state.paymentHome.paymentDetails,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    fetchPaymentMethods: (transactionId) =>
      dispatch(actions.fetchPaymentMethods(transactionId)),
    fetchPaymentDetails: (transactionId) =>
      dispatch(actions.fetchPaymentDetails(transactionId)),
    forwardPayment: (request) => dispatch(actions.forwardPayment(request)),
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withStyles(styles)(PaymentHome));
