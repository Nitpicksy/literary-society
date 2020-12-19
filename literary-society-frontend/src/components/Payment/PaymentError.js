import React from 'react';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './PaymentStatusStyles';
import ErrorOutlineIcon from '@material-ui/icons/ErrorOutline';
import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';

export default function PaymentError() {
    const classes = useStyles();

    return (
        <Container component="main" maxWidth="sm">
            <CssBaseline />
            <Card className={classes.card}>
                <CardContent className={classes.cardContent}>
                    <Avatar className={classes.avatar}>
                        <ErrorOutlineIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5" className={classes.message}>
                        Payment could not be completed due to unexpected error. Please try again later.
                 </Typography>
                </CardContent>
            </Card>
        </Container>
    );
}