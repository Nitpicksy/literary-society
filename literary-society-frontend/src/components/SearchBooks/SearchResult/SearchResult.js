import React from 'react';
import { useStyles } from './SearchResultStyles';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import { useHistory } from 'react-router';
import { connect } from 'react-redux';
import * as actions from '../../BookDetails/BookDetailsActions';
import Parser from 'html-react-parser'

const SearchResult = (props) => {
    const classes = useStyles();
    const history = useHistory();
    const { fetchBook } = props;

    const addToCart = () => {
        fetchBook(props.book.id, history, true)
    }

    const download = () => {
        props.download(props.book.id,props.book.title);
    }

    const redirectToDetailsPage = () => {
        localStorage.setItem('searchAllValue', props.searchAllValue);
        localStorage.setItem('searchValue', JSON.stringify(props.searchValue));
        localStorage.setItem('advanceSearchValues', JSON.stringify(Array.from(props.advanceSearchValues)));
        localStorage.setItem('page', JSON.stringify(props.page));
        history.push(`/book/${props.book.id}`);
    } 
    
    let button = <Button size="small" color="primary" onClick={addToCart}>Add to cart</Button>;

    if (props.book.openAccess) {
        button = <Button size="small" color="secondary" onClick={download}>Get for free</Button>;
    }

    return (
        <Grid item md={6}>
            <Card className={classes.root}>
                <CardActionArea onClick={()=> redirectToDetailsPage()}>
                    <CardContent className={classes.cardcontent}>
                        <Typography className={classes.title}>
                            {props.book.title}
                        </Typography>
                        <Typography gutterBottom color="textSecondary" className={classes.writersNames}>
                            {props.book.writers}  <span>&#8226;</span> {props.book.genreInfo.name}
                        </Typography>
            
                        <Typography color="textSecondary" className={classes.content}>
                            <span>{Parser(props.book.content)}</span>
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


const mapDispatchToProps = dispatch => {
    return {
        fetchBook: (id, history,addToCart) => dispatch(actions.fetchBook(id, history,addToCart)),
        download: (id,title) => dispatch(actions.download(id,title))
    }
};

export default connect(null, mapDispatchToProps)(SearchResult);