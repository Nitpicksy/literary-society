import React from 'react';
import { Card, CardContent, Typography, Container, Avatar, CssBaseline } from '@material-ui/core';
import CardGiftcardIcon from '@material-ui/icons/CardGiftcard';
import { useStyles } from './SubscriptionSuccessStyles';
import { Link } from 'react-router-dom';

const CancelSubscriptionSuccess = () => {
    const classes = useStyles();

    return (
        <Container component="main" maxWidth="sm">
            <CssBaseline />
            <Card className={classes.card}>
                <CardContent className={classes.cardContent}>
                    <Avatar className={classes.avatar}>
                        <CardGiftcardIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5" className={classes.message}>
                        You have successfully unsubscribed from membership.
                    </Typography>
                    <Link to="/">Back to Homepage</Link>
                </CardContent>
            </Card>
        </Container>
    );
};

export default CancelSubscriptionSuccess;