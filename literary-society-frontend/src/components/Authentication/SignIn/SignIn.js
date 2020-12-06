import React from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './SignInStyles';

const SignIn = (props) => {
  const classes = useStyles();

  return (
    <Container component="main" maxWidth="xs">
        <CssBaseline />
        <div className={classes.paper}>
            <Avatar className={classes.avatar}>
                <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h4">Sign in</Typography>
            <form className={classes.form} noValidate>
                <TextField variant="outlined" margin="normal" required fullWidth
                    id="username" name="username" label="Username" autoComplete="username"         
                />
                <TextField variant="outlined" margin="normal" required fullWidth
                    id="password" name="password" label="Password" type="password" autoComplete="current-password"
                />
                <Typography id="errorText" variant="body2" className={classes.errorText}>Placeholder za error text</Typography>
                <Button type="submit" color="primary" className={classes.submit} fullWidth variant="contained">Sign In</Button>
                <Grid container>
                    <Grid item xs>
                        <Link href="#" variant="body2">Forgot password?</Link>
                    </Grid>
                    <Grid item>
                        <Link href="#" variant="body2">Don't have an account? Sign Up</Link>
                    </Grid>
                </Grid>
            </form>
        </div>
    </Container>
  );
};

export default SignIn;