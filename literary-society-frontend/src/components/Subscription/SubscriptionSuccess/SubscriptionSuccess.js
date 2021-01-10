import React, { useEffect } from 'react';
import axios from '../../../axios-endpoint';
import { toastr } from 'react-redux-toastr';
import { Card, CardContent, Typography, Container, Avatar, CssBaseline } from '@material-ui/core';
import CardGiftcardIcon from '@material-ui/icons/CardGiftcard';
import { useStyles } from './SubscriptionSuccessStyles';
import { Link } from 'react-router-dom';

const SubscriptionSuccess = (props) => {
    const classes = useStyles();
    
    let params = new URLSearchParams(props.location.search);
    let subscriptionId = params.get('subscription_id');

    useEffect(() => {
        axios.post(`/subscriptions/create-membership?subscriptionId=${subscriptionId}`)
            .catch(err => {
                toastr.error('Subscribe', err.response.data.message);
            });
    });

    return (
        <Container component="main" maxWidth="sm">
            <CssBaseline />
            <Card className={classes.card}>
                <CardContent className={classes.cardContent}>
                    <Avatar className={classes.avatar}>
                        <CardGiftcardIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5" className={classes.message}>
                        You have successfully subscribed for membership.
                    </Typography>
                    <Link to="/">Back to Homepage</Link>
                </CardContent>
            </Card>
        </Container>
    );
};

export default SubscriptionSuccess;