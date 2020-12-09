import { AppBar, Button, IconButton,Toolbar, Typography } from '@material-ui/core';
import React from 'react';
import { useStyles } from './ToolbarStyles';
import MenuIcon from '@material-ui/icons/Menu';
import { useHistory } from 'react-router-dom';

const CustomToolbar = (props) => {
    const classes = useStyles();
    const history = useHistory();

    const routeChange = (path) => {
        history.push(path);
    }

    return (
        <AppBar position="static">
            <Toolbar>
                <IconButton edge="start" className={classes.menuButton} color="inherit" aria-label="menu">
                    <MenuIcon />
                </IconButton>
                <Typography variant="h6" className={classes.title}> Literary Society </Typography>
                <Button color="inherit" onClick={() => routeChange("/logout")}>Log out</Button>
            </Toolbar>
        </AppBar>
    );
}

export default CustomToolbar;