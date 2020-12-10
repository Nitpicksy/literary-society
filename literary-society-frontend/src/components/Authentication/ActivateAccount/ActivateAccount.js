import {
  Avatar,
  Container,
  CssBaseline,
  Grid,
  Link,
  Typography,
} from "@material-ui/core";
import { useStyles } from "./ActivateAccountStyles";
import DoneAllRoundedIcon from "@material-ui/icons/DoneAllRounded";
import SentimentVeryDissatisfiedIcon from "@material-ui/icons/SentimentVeryDissatisfied";
import React, { useEffect } from "react";
import { connect } from "react-redux";
import { activateAccount } from "./ActivateAccountActions";

const ActivateAccount = (props) => {
  const classes = useStyles();

  useEffect(() => {
    props.activateAccount(props.match.params.id);
    console.log("props", props);
  }, [props]);

  let message = "Account activated, you're all set!";

  if (props.error) {
    message = "Activation link is invalid or expired.";
  }

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          {props.error ? (
            <SentimentVeryDissatisfiedIcon />
          ) : (
            <DoneAllRoundedIcon />
          )}
        </Avatar>
        <Typography component="h1" variant="h4">
          {message}
        </Typography>
      </div>
      <br />
      <Grid container spacing={3}>
        <Grid item xs>
          <Link href="/sign-in" variant="body2">
            Back to sign in
          </Link>
        </Grid>
      </Grid>
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
    activateAccount: (hashId) => dispatch(activateAccount(hashId)),
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(ActivateAccount);
