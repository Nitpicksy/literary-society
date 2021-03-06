import { Button } from '@material-ui/core';
import React from 'react';
import { useStyles } from './ToolbarStyles';
import { useHistory } from 'react-router-dom';

const AdminToolbar = (props) => {
    const classes = useStyles();
    const history = useHistory();

    const redirect = (path) => {
        history.push(path);
    }

    return (
        <React.Fragment>
            <Button className={classes.button} color="inherit" onClick={() =>redirect('/payment-methods') }>Payment Methods</Button>
            <Button className={classes.button} color="inherit" onClick={() =>redirect('/companies') }>Companies</Button>
        </React.Fragment>
    );
}

export default AdminToolbar;