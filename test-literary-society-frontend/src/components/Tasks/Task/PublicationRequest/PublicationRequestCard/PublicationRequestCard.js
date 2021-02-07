import React from 'react';
import { useStyles } from './PublicationRequestCardStyles';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';


export default function PublicationRequestCard(props) {
    const classes = useStyles();
    return (
        <Card>
            <CardContent className={classes.cardContent}>
                <Typography component="h1" variant="h4" className={classes.title}>  {props.book.title}</Typography>
                <Grid container justify="center" className={classes.grid}>
                    <Grid item xs={2}>
                        <Typography className={classes.genre} style={{fontWeight: 500}}>
                            Genre:
                        </Typography>
                    </Grid>
                    <Grid item xs={10}>
                        <Typography className={classes.genre}>
                            {props.book.genre}
                        </Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <Typography className={classes.synopsis} style={{fontWeight: 500}}>
                            Synopsis:
                        </Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <Typography className={classes.synopsis}>
                            {props.book.synopsis}
                        </Typography>
                    </Grid>
                </Grid>
                {/* <Grid container justify="center" className={classes.grid}>
                    <Grid item xs={3}>
                        <Typography className={classes.synopsis} style={{fontWeight: 500}}>
                            Synopsis:
                            </Typography>
                    </Grid>
                    <Grid item xs={9}>
                        <Typography className={classes.synopsis}>
                            {props.book.synopsis}
                        </Typography>
                    </Grid>
                </Grid> */}
            </CardContent>
        </Card>
    );
}
