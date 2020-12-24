import React from 'react';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './AddPaymentMethodSuccessStyles';
import ThumbUpIcon from '@material-ui/icons/ThumbUp';
import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';

export default function AddPaymentMethodSuccess() {
    const classes = useStyles();
    return (
        <Container component="main" maxWidth="sm">
            <CssBaseline />
            <Card className={classes.card}>
                <CardContent className={classes.cardContent}>
                    <Avatar className={classes.avatar}>
                        <ThumbUpIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5" className={classes.message}>
                        You will be notified when the administrator approves your registration request.
                        {/* Successfully registered to Payment Gateway. You have to wait administrator approves your registration.  */}
                    </Typography>
                </CardContent>
            </Card>
        </Container>
    );
}