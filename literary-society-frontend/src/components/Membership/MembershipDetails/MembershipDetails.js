import React, { useState } from 'react';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import CssBaseline from '@material-ui/core/CssBaseline';
import { useStyles } from './MembershipDetailsStyles';
import { Button } from '@material-ui/core';
import Avatar from '@material-ui/core/Avatar';

const MembershipDetails = (props) => {
    const classes = useStyles();

    const { user } = props;

    let date = new Date(user.expirationDate)

    let isExpired = () => {

        let expirationDate = new Date(user.expirationDate);
        let now = new Date();

        if(now > expirationDate) {
            return false;
        }

        return true;
    }
    return (
        <Grid container alignItems="center"
        justify="center" 
        spacing={3} align="center">
            <Grid item md={12} sm={12} xs={12} className={classes.bookInfo}>
                <Typography className={classes.title}>Membership details</Typography>
                <Grid container>
                    <Grid item md={12} sm={12} xs={12}>
                        <Typography className={classes.genre}>{user.user}</Typography>
                        <Typography className={classes.isbn}>Price:&nbsp; {user.price.toFixed(2)} din.</Typography>
                        <Typography className={classes.publicationDate}>Subscribed:&nbsp; {user.subscribed ? 'Yes' : 'No'}</Typography>
                        <Typography className={classes.publisherCity}>Expires:&nbsp; {date.toDateString()}</Typography>
                        <Typography className={classes.publisherCity}>Status:&nbsp; <strong> {isExpired() ? 'Active' : 'Expired'}</strong></Typography>
                    </Grid>

                </Grid>
            </Grid>
        </Grid>
        
    )
}

export default MembershipDetails;