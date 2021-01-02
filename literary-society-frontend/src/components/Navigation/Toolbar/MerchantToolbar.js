import { Button } from '@material-ui/core';
import React from 'react';
import { useStyles } from './ToolbarStyles';
import { useHistory } from 'react-router-dom';

const MerchantToolbar = (props) => {
    const classes = useStyles();
    const history = useHistory();

    const redirect = (path) => {
        history.push(path);
    }

    return (
        <React.Fragment>
            <Button className={classes.button} color="inherit" onClick={() =>redirect('/payment-data') }> Insert Payment Data </Button>
        </React.Fragment>
    );
}

export default MerchantToolbar;