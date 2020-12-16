import React from 'react';
import { useStyles } from './BookCardStyles';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import { toastr } from 'react-redux-toastr';
import Chip from '@material-ui/core/Chip';
import Tooltip from '@material-ui/core/Tooltip';


export default function BookCard(props) {
    const classes = useStyles();

    const addToCart = () => {
        let shoppingCart = new Map(JSON.parse(localStorage.getItem('shoppingCart')));

        if (!shoppingCart) {
            shoppingCart = new Map();
        }

        let merchantBooks = shoppingCart.get(props.book.merchantName);
        if (!merchantBooks) {
            merchantBooks = [];
        }

        const found = merchantBooks.find(element => element.id === props.book.id);
        if (!found) {
            merchantBooks.push(props.book);
            shoppingCart.set(props.book.merchantName, merchantBooks);

            localStorage.setItem('shoppingCart', JSON.stringify(Array.from(shoppingCart.entries())));
        } else {
            toastr.success('Shopping cart', 'This book is already added');
        }
    }

    let button = <Button size="small" color="primary" onClick={addToCart}>Add to cart</Button>;

    if(props.forShoppingCart){
        button = <Button size="small" color="primary"  onClick={()=> props.removeItem(props.book)}>Remove item</Button>;
    }

    let price =
        <Typography className={classes.price}>
            {props.book.price.toFixed(2)} din.
        </Typography>;

    if (props.book.price <= 0 && !props.forShoppingCart) {
        button = <Button size="small" color="secondary">Get for free</Button>;
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
                <CardActionArea>
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
                        <Typography color="textSecondary" className={classes.merchantName}>
                            {props.book.merchantName}
                        </Typography>
                    </CardContent>
                </CardActionArea>
                <CardActions>
                    {button}
                </CardActions>
            </Card>
        </Grid>
    );
}
