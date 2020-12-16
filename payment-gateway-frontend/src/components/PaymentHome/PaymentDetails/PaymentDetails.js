import React from "react";

import Typography from "../Typography";

const PaymentDetails = (props) => {
  return (
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
  );
};

export default PaymentDetails;
