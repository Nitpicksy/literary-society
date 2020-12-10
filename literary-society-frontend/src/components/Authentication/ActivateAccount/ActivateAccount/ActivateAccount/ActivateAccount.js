import { Avatar, Container, CssBaseline, Typography } from "@material-ui/core";
import { useStyles } from "./ActivateAccountStyles";
import DoneAllRoundedIcon from "@material-ui/icons/DoneAllRounded";
import React, { useEffect } from "react";
import { connect } from "react-redux";

const ActivateAccount = (props) => {
  const classes = useStyles();

  useEffect(() => {
    console.log(props);
  }, []);

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <DoneAllRoundedIcon />
        </Avatar>
        <Typography component="h1" variant="h4">
          You're all set!
        </Typography>
      </div>
    </Container>
  );
};

const mapStateToProps = (state) => {
  return {};
};

const mapDispatchToProps = (dispatch) => {
  return {
    activateAccount: dispatch(),
  };
};

export default connect(mapStateToProps, mapDispatchToProps)(ActivateAccount);
