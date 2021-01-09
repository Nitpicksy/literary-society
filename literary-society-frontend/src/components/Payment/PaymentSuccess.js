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
import { Grid } from '@material-ui/core';
import { Link, useHistory } from 'react-router-dom';

const PaymentSuccess = (props) => {
    const classes = useStyles();
    const history = useHistory();
    
    const {fetchTransaction} = props;
    var download = null;
    var token = null;

    useEffect(() => {
        const { id } = props.match.params;
        if(id){
            fetchTransaction(id);
        }
    }, [props.match.params, fetchTransaction]);

    const onDownload = (event) => {
        event.preventDefault();
        props.download(token,history);
    }

    if(props.transaction){
        const params = props.transaction.url.split('?t=');
        console.log(props.transaction)
        token = params[1];
        if(token){
            download = <Link  to="#" onClick = {onDownload} 
             variant="body2">Click this link to find the book and download it</Link>
        }else {
            download = <Link to="/purchased-books" variant="body2">Click this link to find the book and download it</Link>;
        }
    }
    return (
           <Container component="main" maxWidth="sm">
            <CssBaseline />
            <Card className={classes.card}>
                <CardContent className={classes.cardContent}>
                    <Avatar className={classes.avatar}>
                        <ThumbUpIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5" className={classes.message}>
                        Payment was successful. 
                    </Typography>
                    <Grid item xs>
                            {download}
                    </Grid>
                </CardContent>
            </Card>
        </Container>
    );
};

const mapStateToProps = state => {
    return {
        transaction: state.transaction.transaction
    }
};

const mapDispatchToProps = dispatch => {
    return {
        fetchTransaction: (id) => dispatch(actions.fetchTransaction(id)),
        download: (token,history) => dispatch(actions.download(token,history)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(PaymentSuccess);