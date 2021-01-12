import React, { useEffect, useState } from 'react';
import * as actions from './ChoosePaymentMethodsActions';
import { Avatar, Button, CssBaseline, Typography, Container, Grid, Paper, FormControlLabel, Checkbox } from '@material-ui/core';
import BusinessIcon from '@material-ui/icons/Business';
import { useStyles } from './ChoosePaymentMethodsStyles';
import { connect } from 'react-redux';
import { useHistory } from 'react-router';
import { toastr } from 'react-redux-toastr';

const ChoosePaymentMethods = (props) => {

    const history = useHistory();
    const classes = useStyles();
    const { fetchPaymentMethods } = props;
    const [token, setToken] = useState(null);
    const [changed, setChanged] = useState(false);

    const [supportedPaymentMethods, setSupportedPaymentMethods] = useState([]);

    useEffect(() => {
        if (props.location) {
            const params = new URLSearchParams(props.location.search);
            const token = params.get('token');
            if (!token) {
                history.push('/');
            } else {
                setToken(token);
                fetchPaymentMethods(token);
            }
        }
    }, [fetchPaymentMethods,props.location ,history]);

    useEffect(() => {
        setSupportedPaymentMethods(props.supportedMethods);
    }, [props.supportedMethods]);

    const handleChecked = ({ target }) => {
        if(!changed){
            setChanged(true);
        }
        if (target.checked) {
            let paymentMethod = props.paymentMethods.find(method => method.id === parseInt(target.name));
            setSupportedPaymentMethods(supportedPayments => [...supportedPayments, paymentMethod]);
        } else {
            setSupportedPaymentMethods(supportedPayments => supportedPayments.filter(method => method.id !== parseInt(target.name)));
        }
    }

    const submitHander = (event) => {
        event.preventDefault();
        if (!(supportedPaymentMethods && Array.isArray(supportedPaymentMethods) && supportedPaymentMethods.length)) {
            toastr.info("Choose Payment Methods", "You need to support at least one payment method.");
            return;
        }
        props.onChoosePaymentMethods(supportedPaymentMethods,token);
    };

    let paymentMethodCards = null;

    if (props.paymentMethods && Array.isArray(props.paymentMethods) && props.paymentMethods.length) {
  
        paymentMethodCards = props.paymentMethods.map(paymentMethod => {
            const found = props.supportedMethods.filter(method => method.id === paymentMethod.id)[0];
            let checked = false;
            if(found){
                checked = true;
            }
            return <Paper key={paymentMethod.id} className={classes.paymentMethodPaper}>
                <FormControlLabel label={paymentMethod.name} classes={{ label: classes.chkLabel }}
                    control={
                        <Checkbox name={'' + paymentMethod.id} onChange={handleChecked} color="primary" defaultChecked= {checked}/>
                    }
                />
            </Paper>;
        });
       
    } else {
        paymentMethodCards =
            <Typography component="h3" variant="h6">No available payment methods at the moment.</Typography>;
    }

    return (
        <Container component="main" maxWidth="md">
            <CssBaseline />
            <Paper className={classes.mainPaper}>
                <div className={classes.centered}>
                    <Avatar className={classes.avatar}>
                        <BusinessIcon />
                    </Avatar>
                    <Typography component="h1" variant="h4">Support payment methods</Typography>
                </div>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    <Grid container align="center" spacing={4} justify="center">
                        <Grid item md={12}>
                            {paymentMethodCards}
                        </Grid>
                    </Grid>
                    <div className={classes.centered}>
                        <Button type="submit" color="primary" className={classes.submit} variant="contained">Submit</Button>
                    </div>
                </form>
            </Paper>
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        paymentMethods: state.choosePaymentMethods.paymentMethods, 
        supportedMethods : state.choosePaymentMethods.supportedMethods, 
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchPaymentMethods: (token) => dispatch(actions.fetchPaymentMethods(token)),
        onChoosePaymentMethods: (paymentMethods,token) => dispatch(actions.choosePaymentMethods(paymentMethods,token))
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(ChoosePaymentMethods);