import React from 'react';
import { useStyles } from './ErrorStyles';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Link from '@material-ui/core/Link';
import Container from '@material-ui/core/Container';
import BlockIcon from '@material-ui/icons/Block';
import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';

export default function NonAuthenticated() {

    const classes = useStyles();

    return (
        <Container component="main" maxWidth="sm">
            <CssBaseline />
            <Card className={classes.card}>
                <CardContent className={classes.cardContent}>
                    <Avatar className={classes.avatar}>
                        <BlockIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5" className={classes.message}>
                        As a non-authenticated user, you are not allowed to enter any page besides login or register
                        page.
                    </Typography>
                    <Typography component="h2">
                        If you want to have access to the system, click here to&nbsp;
                        <Link href="/sign-up">sign up</Link>.
                    </Typography>
                    <Typography component="h2">
                        If you already have an account, click here to&nbsp;
                        <Link href="/sign-in">sign in</Link>.
                    </Typography>
                </CardContent>
            </Card>
        </Container>
    );
}