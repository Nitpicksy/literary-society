import React, { Fragment, useEffect, useState } from 'react';
import * as actions from './MembershipActions';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import CssBaseline from '@material-ui/core/CssBaseline';
import { useStyles } from './MembershipStyles';
import { connect } from 'react-redux';
import { useHistory } from 'react-router';
import { Button } from '@material-ui/core';
import CardMembershipIcon from '@material-ui/icons/CardMembership';
import Avatar from '@material-ui/core/Avatar';
import AddIcon from '@material-ui/icons/Add';
import { responseInterceptor } from '../../responseInterceptor';
import * as signInActions from '../Authentication/SignIn/SignInActions';
import MembershipDetails from './MembershipDetails/MembershipDetails';
import SockJS from './SockJS/SockJS';

const Membership = (props) => {

    const history = useHistory();
    const classes = useStyles();

    const [displayPrice, setDisplayPrice] = useState(false);

    const { fetchMembership, membership } = props;
    const { fetchPriceList, priceList } = props;
    const { selectedTask } = props;
    const { onPay, completePayTask } = props;

    responseInterceptor.setupInterceptor(history, props.refreshTokenRequestSent, props.onRefreshToken);

    let data = null;
    let price = null;
    let buttons = null;

    useEffect(() => {
        fetchMembership(history);
    }, [fetchMembership, history]);

    useEffect(() => {
        fetchPriceList();
    }, [fetchPriceList]);

    const handleDisplayPrice = () => {
        setDisplayPrice(true)
    }

    const handlePay = () => {
        if(selectedTask.piId != null && selectedTask.taskId != null) {
            completePayTask(selectedTask);
        } else {
            onPay();
        }
    }

    console.log('Current task', selectedTask)
    console.log('mem', membership)

    if(membership == null) {
        buttons = (
            <Fragment> 
            <Grid item xs={2}>
               <Button variant="contained" color="primary" startIcon={<AddIcon />} onClick={() => handleDisplayPrice()}>
                   Yes
               </Button>
             </Grid>

             <Grid item xs={2}>
             <Button variant="contained" color="default" onClick={() => history.push("/")}>
                 Not right now     
             </Button>
             </Grid>
            </Fragment>
        )
        data = (<Grid container
            alignItems="center"
            justify="center" 
            spacing={3} align="center">          
               <Grid item xs={12}>
                <Typography component="h6" variant="h6">No active membership, make one?</Typography>
               </Grid>
               {buttons}
          </Grid>) 
    } else {
        let expirationDate = new Date(membership.expirationDate);
        let now = new Date();

        buttons = (
            <Fragment>
                <Grid container
            alignItems="center"
            justify="center" 
            spacing={3} align="center">   
              <Grid item xs={2}>
               <Button variant="contained" color="primary" startIcon={<AddIcon />} onClick={() => handlePay()}>
                   Purchase
               </Button>
             </Grid> 
            </Grid>
            </Fragment>
        )

        if(now > expirationDate) {
            data = (<Fragment> 
                <MembershipDetails user={membership}/>
                {buttons}
                </Fragment>
                )
        } else {
            data = (<Fragment> 
                <MembershipDetails user={membership}/>
                </Fragment>
                )
        }
    }

    if(priceList) {
        let displayPrice = 0;

        if(props.role === 'ROLE_WRITER') {
            displayPrice = priceList.membershipForWriter
        } else {
            displayPrice = priceList.membershipForReader
        }

        price = (
            <Fragment> 
            <Grid item xs={2}>
            <Typography> Total price: </Typography>
                <Typography className={classes.price}>
             {displayPrice.toFixed(2)} din.
                </Typography>
            </Grid>
            </Fragment>);
    }

    if(displayPrice) {
        buttons = (
            <Fragment> 
            <Grid item xs={2}>
               <Button variant="contained" color="primary" startIcon={<AddIcon />} onClick={() => handlePay()}>
                   Purchase
               </Button>
             </Grid>
            </Fragment>
        )
        data = (<Grid container
            alignItems="center"
            justify="center" 
            spacing={3} align="center">          
               <Grid item xs={12}>
                <Typography component="h6" variant="h6">No active membership, make one?</Typography>
               </Grid>
               {price}
               {buttons}
          </Grid>) 
    }


    return (
        <Container component="main" maxWidth="lg">
        <CssBaseline />
        <div className={classes.paper}>
            <Avatar className={classes.avatar}>
                <CardMembershipIcon />
            </Avatar>
            <Typography component="h1" variant="h4">Your membership</Typography>
        </div>
        {data}
        <br/>

        <SockJS />
    </Container>
    )
}

const mapStateToProps = state => {
    return {
        selectedTask: state.tasks.selectedTask,
        refreshTokenRequestSent: state.signIn.refreshTokenRequestSent,
        membership: state.memberships.membership,
        priceList: state.memberships.priceList,
        role: state.signIn.role,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onRefreshToken: (history) => dispatch(signInActions.refreshToken(history)),
        fetchMembership: (history) => dispatch(actions.fetchMembership(history)),
        fetchPriceList: () => dispatch(actions.fetchPriceList()),
        onPay: () => dispatch(actions.onPay()),
        completePayTask: (selectedTask) => dispatch(actions.completePayTask(selectedTask))
    }
};


export default connect(mapStateToProps, mapDispatchToProps)(Membership);