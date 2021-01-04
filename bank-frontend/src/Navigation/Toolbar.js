import { AppBar, Button, Toolbar, Typography } from '@material-ui/core';
import React from 'react';
import { useStyles } from './ToolbarStyles';
import { useHistory } from 'react-router-dom';

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
                <Button className={classes.button} color="inherit" onClick={() => redirect('/clients')}>Clients</Button>
            </Toolbar>
        </AppBar>
    );
}

export default CustomToolbar;