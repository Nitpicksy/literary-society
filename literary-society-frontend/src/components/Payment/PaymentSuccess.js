import React, { useEffect } from 'react';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import { useStyles } from './PaymentStatusStyles';
import ThumbUpIcon from '@material-ui/icons/ThumbUp';
import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';
import * as actions from './Transaction/TransactionExport';
import { connect } from 'react-redux';

const PaymentSuccess = (props) => {
    const classes = useStyles();
    const {fetchTransaction} = props;

    useEffect(() => {
        const { id } = props.match.params;
        if(id){
            fetchTransaction(id);
        }
    }, [props.match.params, fetchTransaction]);

    return (
           <Container component="main" maxWidth="sm">
            <CssBaseline />
            <Card className={classes.card}>
                <CardContent className={classes.cardContent}>
                    <Avatar className={classes.avatar}>
                        <ThumbUpIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5" className={classes.message}>
                        Payment was successful. Click this link to find the book and download it.
                    </Typography>
                </CardContent>
            </Card>
        </Container>
    );
};

const mapDispatchToProps = dispatch => {
    return {
        fetchTransaction: (id) => dispatch(actions.fetchTransaction(id)),
    }
};

export default connect(null, mapDispatchToProps)(PaymentSuccess);