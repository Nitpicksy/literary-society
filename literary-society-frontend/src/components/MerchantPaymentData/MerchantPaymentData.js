import React, { useEffect } from 'react';
import CssBaseline from '@material-ui/core/CssBaseline';
import * as actions from './MerchantPaymentDataExport';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './MerchantPaymentDataStyles';
import { connect } from 'react-redux';
import { useHistory } from 'react-router';
import { responseInterceptor } from '../../responseInterceptor';
import * as signInActions from '../Authentication/SignIn/SignInExport';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';

const MerchantPaymentData = (props) => {
    const history = useHistory();
    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);
    const classes = useStyles();

    const { onInsertPaymentData } = props;

    useEffect(() => {
        onInsertPaymentData();
    }, [onInsertPaymentData]);


    return (
        <Container component="main" maxWidth="sm">
            <CssBaseline />
            <Card className={classes.card}>
                <CardContent className={classes.cardContent}>
                    <Typography component="h1" variant="h5" className={classes.message}>
                        You already support all available payment methods.
                    </Typography>
                </CardContent>
            </Card>
        </Container>
    );
};

const mapDispatchToProps = dispatch => {
    return {
        onInsertPaymentData: () => dispatch(actions.insertPaymentData()),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history))
    }
};

export default connect(null, mapDispatchToProps)(MerchantPaymentData);