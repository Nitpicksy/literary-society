import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';

const useStyles = makeStyles((theme) => ({
    root: {
        minWidth: 700,
        margin: theme.spacing(10, 0, 0, 0),
    },
    bullet: {
        display: 'inline-block',
        margin: '0 2px',
        transform: 'scale(0.8)',
    },
    title: {
        fontSize: 14,
    },
    pos: {
        marginBottom: 12,
    },
}));

export default function PaymentError() {
    const classes = useStyles();
    return (
        <Container component="main" maxWidth="sm">
            <Card className={classes.root}>
                <CardContent>
                    <Typography variant="h5" component="h1">
                        Something went wrong. Please try again.
                     </Typography>
                    <br />
                </CardContent>
            </Card>
        </Container>
    );
}