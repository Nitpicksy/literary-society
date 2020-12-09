import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Link from '@material-ui/core/Link';
import Container from '@material-ui/core/Container';

const useStyles = makeStyles((theme) => ({
    root: {
        minWidth: 700,
        margin: theme.spacing(10, 0, 0, 0),
    },
    bullet: {
        display: 'inline-block',
        margin: '0 2px',
        transform: 'scale(0.8)',
    },
    title: {
        fontSize: 14,
    },
    pos: {
        marginBottom: 12,
    },
}));

export default function NonAuthenticated() {
    const classes = useStyles();
    return (
        <Container component="main" maxWidth="sm">
            <Card className={classes.root}>
                <CardContent>
                    <Typography variant="h5" component="h1">
                        As a non-authenticated user, you are not allowed to enter any page besides login or register
                    page.</Typography>
                    <br/>
                    <Typography component="h2">
                        If you want to have access to the system, click here to&nbsp;
                    <Link href="/sign-up">sign up</Link>
                    </Typography>
                    <Typography component="h2">
                        If you already have an account, click here to&nbsp;
                    <Link href="/sign-in">sign in</Link>
                    </Typography>
                </CardContent>
            </Card>
        </Container>
    );
}