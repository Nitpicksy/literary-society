import React, { useEffect } from 'react';
import { Card, CardContent, Typography, Container, Avatar, CssBaseline, Button } from '@material-ui/core';
import CardGiftcardIcon from '@material-ui/icons/CardGiftcard';
import * as actions from './SubscriptionAction';
import { connect } from 'react-redux';
import { useStyles } from './SubscriptionStyles';

const Subscription = (props) => {
    const classes = useStyles();
    const { fetchSubscriptionPlan } = props;

    const roleWriter = "ROLE_WRITER";
    const roleReader = "ROLE_READER";

    let forUser = null;
    if (props.role === roleWriter) {
        forUser = "writer";
    } else if (props.role === roleReader) {
        forUser = "reader";
    }

    useEffect(() => {
        fetchSubscriptionPlan(forUser);
    }, [forUser, fetchSubscriptionPlan]);

    const handleSubscribe = () => {
        props.subscribe(props.subscriptionPlan.id);
    }

    let plan = null;
    if (props.subscriptionPlan) {
        plan =
            <React.Fragment>
                <Typography component="h1" variant="h4" className={classes.message}>
                    {props.subscriptionPlan.planName}
                </Typography>
                <Typography component="h1" variant="h6" className={classes.message && classes.product}>
                    {props.subscriptionPlan.productName}
                </Typography>
                <Typography variant="body1" className={classes.message}>
                    {props.subscriptionPlan.planDescription}
                </Typography>
                <Typography component="h1" variant="h5" className={classes.message && classes.price}>
                    {props.subscriptionPlan.price} din. every {props.subscriptionPlan.frequencyCount} {props.subscriptionPlan.frequencyUnit.toLowerCase()}
                </Typography>
                <Button variant="contained" color="primary" fullWidth onClick={() => handleSubscribe()}>
                    Subscribe
                </Button>
            </React.Fragment>;
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <Card className={classes.card}>
                <CardContent className={classes.cardContent}>
                    <Avatar className={classes.avatar}>
                        <CardGiftcardIcon />
                    </Avatar>
                    {plan}
                </CardContent>
            </Card>
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        role: state.signIn.role,
        subscriptionPlan: state.subscription.subscriptionPlan,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchSubscriptionPlan: (forUser) => dispatch(actions.fetchSubscriptionPlan(forUser)),
        subscribe: (planId) => actions.subscribe(planId),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Subscription);