import React, { useEffect } from 'react';
import CssBaseline from '@material-ui/core/CssBaseline';
import * as actions from './ChoosePaymentMethodsExport';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './ChoosePaymentMethodsStyles';
import { connect } from 'react-redux';
import { useHistory } from 'react-router';
import { responseInterceptor } from '../../responseInterceptor';
import * as signInActions from '../Authentication/SignIn/SignInExport';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';

const ChoosePaymentMethods = (props) => {
    const history = useHistory();
    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);
    const classes = useStyles();

    const { choosePaymentMethods } = props;

    useEffect(() => {
        choosePaymentMethods();
    }, [choosePaymentMethods]);


    return (
        <Container component="main" maxWidth="sm">
            <CssBaseline />
            <Card className={classes.card}>
                <CardContent className={classes.cardContent}>
                    <Typography component="h1" variant="h5" className={classes.message}>
                       Loading...
                    </Typography>
                </CardContent>
            </Card>
        </Container>
    );
};

const mapDispatchToProps = dispatch => {
    return {
        choosePaymentMethods: () => dispatch(actions.choosePaymentMethods()),
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history))
    }
};

export default connect(null, mapDispatchToProps)(ChoosePaymentMethods);