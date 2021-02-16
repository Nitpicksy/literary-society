import React from 'react';
import { useStyles } from './ResultStyles';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import { useHistory } from 'react-router';

const Result = (props) => {
    const classes = useStyles();
    const history = useHistory();


    const redirectToDetailsPage = () => {
        history.push(`/plagiarism-details?piId=${props.piId}&id=${props.result.id}`);
    }

    return (
        <Grid item md={4}>
            <Card className={classes.root}>
                <CardActionArea onClick={() => redirectToDetailsPage()}>
                    <CardContent className={classes.cardcontent}>
                        <Typography className={classes.title}>
                            {props.result.title.slice(20, -4)}
                        </Typography>
                        {
                            props.result.similarProcent >= 0.25 ?
                                <Typography className={classes.similarProcent}>
                                    Similarity:&nbsp;
                                    <span className={classes.similarProcentRed}>
                                        {(props.result.similarProcent * 100).toFixed(2)} %
                                    </span>
                                </Typography> :
                                <Typography className={classes.similarProcent}>
                                    Similarity:&nbsp;
                                    <span>
                                        {(props.result.similarProcent * 100).toFixed(2)} %
                                    </span>
                                </Typography>
                        }

                    </CardContent>
                </CardActionArea>
            </Card>
        </Grid>
    );
}


// const mapDispatchToProps = dispatch => {
//     return {
//         fetchBook: (id, history,addToCart) => dispatch(actions.fetchBook(id, history,addToCart)),
//         download: (id,title) => dispatch(actions.download(id,title))
//     }
// };

// export default connect(null, mapDispatchToProps)(ShowResults);

export default Result;