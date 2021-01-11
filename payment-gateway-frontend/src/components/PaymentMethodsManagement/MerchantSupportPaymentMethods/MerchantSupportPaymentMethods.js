import React, { useEffect, useState } from "react";
import { useStyles } from "./MerchantSupportPaymentMethodsStyles";
import Typography from "@material-ui/core/Typography";
import Container from "@material-ui/core/Container";
import Avatar from "@material-ui/core/Avatar";
import CssBaseline from "@material-ui/core/CssBaseline";
import { useHistory } from "react-router";
import * as actions from "./MerchantSupportPaymentMethodsExport";
import { connect } from "react-redux";
import { extractControlsFromPaymentMethodsData } from "../../../utility/extractControlsFromPaymentMethodsData";
import PaymentIcon from "@material-ui/icons/Payment";
import Button from "@material-ui/core/Button";
import MerchantSupportPaymentMethodsForm from "./MerchantSupportPaymentMethodsForm";
import { Paper } from "@material-ui/core";

const MerchantSupportPaymentMethods = (props) => {
  const classes = useStyles();
  const [companyId, setCompanyId] = useState(null);
  const [merchantId, setMerchantId] = useState(null);
  let [controls, setControls] = useState(null);
  const [formIsValid, setFormIsValid] = useState(false);

  const { paymentMethods, fetchPayment } = props;
  const history = useHistory();

  let form = null;

  useEffect(() => {
    if (props.location) {
      const params = new URLSearchParams(props.location.search);
      const companyId = params.get("company");
      const merchantId = params.get("merchant");

      if (!companyId || !merchantId) {
        history.push("/");
      } else {
        setCompanyId(companyId);
        setMerchantId(merchantId);
        fetchPayment(companyId, merchantId, history);
      }
    }
  }, [fetchPayment, history, props.location]);

  console.log("methods", props.paymentMethods);

  useEffect(() => {
    if (paymentMethods) {
      let extractedControls = extractControlsFromPaymentMethodsData(
        paymentMethods
      );
      console.log("ex", extractedControls);

      setControls(extractedControls);
    }
  }, []);



  const submitHander = (event) => {
    event.preventDefault();
    let array = [];
    let paymentData = [];
    let paymentMethod = null;
    for (let [key, data] of Object.entries(controls)) {
      if (!paymentMethod || paymentMethod.id !== data.paymentMethod.id) {
        if (paymentData.length > 0 && paymentMethod) {
          array.push({
            paymentMethod: paymentMethod,
            paymentData: paymentData,
          });
        }
        paymentMethod = data.paymentMethod;
        paymentData = [];
      }
      let value = data.value;
      if (Array.isArray(data.value)) {
        value = data.value.join();
      }
      paymentData.push({ paymentDataId: key, attributeValue: value });
    }
    array.push({ paymentMethod: paymentMethod, paymentData: paymentData });
    console.log(array);
    props.onSubmit(array, companyId, merchantId, history);
  };


  if (controls) {
    form = (
      <MerchantSupportPaymentMethodsForm
        controls={controls}
        setControls={setControls}
        setFormIsValid={setFormIsValid}
      />
    );
  }

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <Paper className={classes.mainPaper}>
        <div className={classes.centered}>
          <Avatar className={classes.avatar}>
            <PaymentIcon />
          </Avatar>
        </div>
        <Typography component="h1" variant="h4" className={classes.title}>
          Support Payment Methods
        </Typography>
        <form className={classes.form} noValidate onSubmit={submitHander}>
          {form}
          <Button
            type="submit"
            color="primary"
            className={classes.submit}
            fullWidth
            variant="contained"
            disabled={!formIsValid}
          >
            Confirm
          </Button>
        </form>
      </Paper>
    </Container>
  );
};

const mapStateToProps = (state) => {
  return {
    paymentMethods: state.merchantSupportPaymentMethods.paymentMethods,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    fetchPayment: (companyId, merchantId, history) =>
      dispatch(actions.fetchPayment(companyId, merchantId, history)),
    onSubmit: (data, companyId, merchantId, history) =>
      dispatch(actions.submitForm(data, companyId, merchantId, history)),
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MerchantSupportPaymentMethods);

