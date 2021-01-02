import React from 'react';
import { useStyles } from './ErrorStyles';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import Link from '@material-ui/core/Link';
import Avatar from '@material-ui/core/Avatar';
import BlockIcon from '@material-ui/icons/Block';
import CssBaseline from '@material-ui/core/CssBaseline';

export default function NonAuthorized() {
    
    const classes = useStyles();

    return (
        <Container component="main" maxWidth="sm">
            <CssBaseline />
            <Card className={classes.card}>
                <CardContent className={classes.cardContent}>
                    <Avatar className={classes.avatar}>
                        <BlockIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5" className={classes.message}>
                        As a non-authorized user, you are not allowed to enter this page.
                    </Typography>
                    <Link href="/" variant="body1">
                        Back to Homepage
                    </Link>
                </CardContent>
            </Card>
        </Container>
    );
}