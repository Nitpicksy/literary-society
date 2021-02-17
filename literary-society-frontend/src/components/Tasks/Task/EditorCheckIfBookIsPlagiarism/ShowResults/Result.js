import React from 'react';
import { useStyles } from './ResultStyles';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import { useHistory } from 'react-router';
import * as actions from '../EditorCheckIfBookIsPlagiarismActions';
import GetAppIcon from '@material-ui/icons/GetApp';
import { CardActions, IconButton } from '@material-ui/core';
import { connect } from 'react-redux';

const Result = (props) => {
    const classes = useStyles();
    const history = useHistory();


    const redirectToDetailsPage = () => {
        history.push(`/plagiarism-details?piId=${props.piId}&id=${props.result.id}`);
    }

    const download = (id, title) => {
        props.onDownload(id, title);
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
                <CardActions>
                    <IconButton onClick={() => download(props.result.id, props.result.title)} >
                        <GetAppIcon style={{
                            transform: "scale(1.3)",
                            padding: 0
                        }} color="primary" />
                    </IconButton>

                </CardActions>
            </Card>
        </Grid>
    );
}


const mapDispatchToProps = dispatch => {
    return {
        onDownload: (id, title) => dispatch(actions.download(id, title)),
    }
};

export default connect(null, mapDispatchToProps)(Result);