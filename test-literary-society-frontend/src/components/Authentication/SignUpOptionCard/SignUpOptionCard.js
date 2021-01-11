import React from 'react';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Grid from '@material-ui/core/Grid';
import { makeStyles } from '@material-ui/core/styles';
import { useHistory } from 'react-router';
import { connect } from 'react-redux';
import * as actions from '../SignUpOptions/SignUpOptionsExport';

const useStyles = makeStyles({
    root: {
        maxWidth: 270,
    },
    content: {
        minHeight: 160,
    }
});

const SignUpOptionCard = (props) => {
    const classes = useStyles();
    const history = useHistory();
    const { path } = props;
    const { setSignUpType, startProcess } = props;

    const chooseOptionHandler = (path) => {
        history.push(path);
        setSignUpType(props.type);
        if(props.type === "readers" || props.type === "writers"){
            startProcess();
        }
    }

    return (
        <Grid item xs={props.xsColumns} sm={props.smColumns}>
            <Card className={classes.root}>
                <CardContent className={classes.content}>
                    <Typography gutterBottom variant="h5" component="h2">{props.title}</Typography>
                    <Typography variant="body2" color="textSecondary" component="p">{props.description}</Typography>
                </CardContent>
                <CardActions>
                    <Button size="small" color="primary"
                        onClick={() => chooseOptionHandler(path)}>Choose</Button>
                </CardActions>
            </Card>
        </Grid>
    );
}

const mapDispatchToProps = dispatch => {
    return {
        setSignUpType: (type) => dispatch(actions.setSignUpType(type)),
        startProcess: () => dispatch(actions.startProcess()),
    }
};

export default connect(null, mapDispatchToProps)(SignUpOptionCard);