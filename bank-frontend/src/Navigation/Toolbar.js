import { AppBar, Button, Toolbar, Typography } from '@material-ui/core';
import React from 'react';
import { useStyles } from './ToolbarStyles';
import { useHistory } from 'react-router-dom';
import Menu from '@material-ui/core/Menu';
import { withStyles } from '@material-ui/core/styles';

const CustomToolbar = () => {
    const classes = useStyles();
    const history = useHistory();
    const redirect = (path) => {
        history.push(path);
    }


    return (
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6" className={classes.title}>
                    Bank
                </Typography>
                <Button className={classes.button} color="inherit" onClick={() => redirect('/merchants')}>Merchants</Button>
            </Toolbar>
        </AppBar>
    );
}

export default CustomToolbar;