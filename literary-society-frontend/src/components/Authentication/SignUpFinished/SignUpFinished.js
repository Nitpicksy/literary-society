import React from 'react';
import { useStyles } from './SignUpFinishedStyles';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Link from '@material-ui/core/Link';
import Container from '@material-ui/core/Container';
import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';
import GroupAddIcon from '@material-ui/icons/GroupAdd';

export default function NonAuthenticated() {

    const classes = useStyles();

    return (
        <Container component="main" maxWidth="sm">
            <CssBaseline />
            <Card className={classes.card}>
                <CardContent className={classes.cardContent}>
                    <Avatar className={classes.avatar}>
                        <GroupAddIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5" className={classes.message}>
                        You have successfully signed up. Check you email for a link to activate the account.
                    </Typography>
                    <Link href="/">Back to Homepage</Link>
                </CardContent>
            </Card>
        </Container>
    );
}