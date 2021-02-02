import React from 'react';
import { useStyles } from './MerchantBookCardStyles';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import Chip from '@material-ui/core/Chip';
import Tooltip from '@material-ui/core/Tooltip';
import { useHistory } from 'react-router';

const MerchantBookCard = (props) => {
    const classes = useStyles();
    const history = useHistory();
    
    const redirectToDetailsPage = () => {
        history.push(`/merchant-book/${props.book.id}`);
    } 
    

    let price =
        <Typography className={classes.price}>
            {props.book.price.toFixed(2)} din.
        </Typography>;

    if (props.book.price <= 0 && !props.forShoppingCart) {
        price = <Typography className={classes.price}>Free</Typography>;
    }

    let discount = null;
    if (props.book.discount > 0) {
        discount =
            <Tooltip className={classes.tooltip} placement="top-end"
                title={<span>Discount is applied to the shown<br />price if membership is payed.</span>}>
                <Chip label={props.book.discount + '%'} classes={{ root: classes.discount }} />
            </Tooltip>
    }


    return (
        <Grid item xs={12} sm={4} md={3} lg={2} >
            <Card className={classes.root}>
                <CardActionArea onClick={()=> redirectToDetailsPage()}>
                    <div className={classes.imageDiv} >
                        <CardMedia className={classes.media} image={props.book.imageData} />
                        {discount}
                    </div>
                    <CardContent className={classes.cardcontent}>
                        <Typography className={classes.title}>
                            {props.book.title}
                        </Typography>
                        <Typography gutterBottom color="textSecondary" className={classes.writersNames}>
                            {props.book.writersNames}
                        </Typography>
                        {price}
                    </CardContent>
                </CardActionArea>
            </Card>
        </Grid>
    );
}

export default MerchantBookCard;