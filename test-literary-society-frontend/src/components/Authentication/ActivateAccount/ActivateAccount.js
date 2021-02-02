import {
  Avatar, Card, CardContent, Container, CssBaseline, Link, Typography,
} from "@material-ui/core";
import { useStyles } from "./ActivateAccountStyles";
import DoneAllIcon from "@material-ui/icons/DoneAll";
import SentimentVeryDissatisfiedIcon from "@material-ui/icons/SentimentVeryDissatisfied";
import React, { useEffect } from "react";
import { connect } from "react-redux";
import { activateAccount } from "./ActivateAccountActions";

const ActivateAccount = (props) => {
  const classes = useStyles();
  const { activateAccount } = props;

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    activateAccount(params.get('t'), params.get('piId'));
  }, [props.location.search, activateAccount]);

  let message = "Account activated, you're all set!";

  if (props.error) {
    message = "Activation link is invalid or expired.";
  }

  return (
    <Container component="main" maxWidth="sm">
      <CssBaseline />
      <Card className={classes.card}>
        <CardContent className={classes.cardContent}>
          <Avatar className={classes.avatar}>
            {props.error ? (
              <SentimentVeryDissatisfiedIcon />
            ) : (
                <DoneAllIcon />
              )}
          </Avatar>
          <Typography component="h1" variant="h5" className={classes.message}>
            {message}
          </Typography>
          <Link href="/sign-in" variant="body1">
            Back to Sign in
          </Link>
        </CardContent>
      </Card>
    </Container>
  );
};

const mapStateToProps = (state) => {
  return {
    error: state.activateAccount.error,
    loading: state.activateAccount.loading,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    activateAccount: (hashId, piId) => dispatch(activateAccount(hashId, piId)),
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(ActivateAccount);
