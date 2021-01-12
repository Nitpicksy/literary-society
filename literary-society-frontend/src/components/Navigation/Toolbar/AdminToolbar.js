import { Button } from '@material-ui/core';
import React from 'react';
import { useHistory } from 'react-router-dom';

const AdminToolbar = (props) => {
    const history = useHistory();

    const redirect = (path) => {
        history.push(path);
    }

    return (
        <React.Fragment>
            <Button color="inherit" onClick={() =>redirect('/manage-users') }>Manage Users</Button>
            <Button color="inherit" onClick={() =>redirect('/manage-merchants') }>Manage Merchants</Button>
            <Button color="inherit" onClick={() =>redirect('/choose-payment-methods') }>Choose Payment Methods</Button>
        </React.Fragment>
    );
}

export default AdminToolbar;