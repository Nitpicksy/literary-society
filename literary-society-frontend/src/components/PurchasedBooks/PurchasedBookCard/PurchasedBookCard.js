import React from 'react';
import { useStyles } from './PurchasedBookCardStyles';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import { connect } from 'react-redux';
import * as actions from '../../BookDetails/BookDetailsActions';

const BookCard = (props) => {
    const classes = useStyles();

    const download = () => {
        props.download(props.book.id, props.book.title);
    }

    return (
        <Grid item xs={12} sm={4} md={3} lg={2} >
            <Card className={classes.root}>
                <CardActionArea >
                    <div className={classes.imageDiv} >
                        <CardMedia className={classes.media} image={props.book.imageData} />
                    </div>
                    <CardContent className={classes.cardcontent}>
                        <Typography className={classes.title}>
                            {props.book.title}
                        </Typography>
                        <Typography gutterBottom color="textSecondary" className={classes.writersNames}>
                            {props.book.writersNames}
                        </Typography>
                        <Typography color="textSecondary" className={classes.merchantName}>
                            {props.book.merchantName}
                        </Typography>
                    </CardContent>
                </CardActionArea>
                <CardActions>
                    <Button size="small" color="secondary" onClick={download}>Download</Button>
                </CardActions>
            </Card>
        </Grid>
    );
}

const mapDispatchToProps = dispatch => {
    return {
        download: (id, title) => dispatch(actions.download(id, title))
    }
};

export default connect(null, mapDispatchToProps)(BookCard);