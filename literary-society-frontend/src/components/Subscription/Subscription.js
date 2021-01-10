import React, { useEffect, useRef, useState } from 'react';
import { Card, CardContent, Typography, Container, Avatar, CssBaseline, Button, CircularProgress } from '@material-ui/core';
import CardGiftcardIcon from '@material-ui/icons/CardGiftcard';
import * as actions from './SubscriptionAction';
import { connect } from 'react-redux';
import { useStyles } from './SubscriptionStyles';
import { useHistory } from 'react-router';

const Subscription = (props) => {
    const classes = useStyles();
    const history = useHistory();
    const [loading, setLoading] = useState(false);
    const timer = useRef();
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

    useEffect(() => {
        return () => {
            clearTimeout(timer.current);
        };
    }, []);

    const handleSubscribe = () => {
        if (!loading) {
            setLoading(true);
            timer.current = window.setTimeout(() => {
                setLoading(false);
            }, 2000);
        }
        props.subscribe(props.subscriptionPlan.id);
    }

    const handleUnsubscribe = () => {
        props.unsubscribe(props.subscriptionPlan.id, history);
    }

    let button = null;
    let plan = null;
    if (props.subscriptionPlan) {
        if (props.subscriptionPlan.membershipStatus === "NOT_SUBSCRIBED") {
            button =
                <div className={classes.wrapper}>
                    <Button variant="contained" color="primary" fullWidth
                        disabled={loading} onClick={() => handleSubscribe()}>
                        Subscribe
                    </Button>
                    {loading && <CircularProgress size={24} className={classes.buttonProgress} />}
                </div>;
        } else if (props.subscriptionPlan.membershipStatus === "SUBSCRIBED") {
            button =
                <Button variant="contained" fullWidthonClick={() => handleUnsubscribe()}>Unsubscribe</Button>;
        }

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
                {button}
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
        unsubscribe: (planId, history) => actions.unsubscribe(planId, history),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Subscription);