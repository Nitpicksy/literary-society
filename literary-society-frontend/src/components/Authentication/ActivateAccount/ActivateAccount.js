import { Avatar, Container, CssBaseline, Typography } from "@material-ui/core";
import { useStyles } from "./ActivateAccountStyles";
import DoneAllRoundedIcon from "@material-ui/icons/DoneAllRounded";
import React from "react";

const ActivateAccount = (props) => {
  const classes = useStyles();
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

export default ActivateAccount;
