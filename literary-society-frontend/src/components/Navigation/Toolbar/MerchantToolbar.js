import { Button } from '@material-ui/core';
import React from 'react';
import { useHistory } from 'react-router-dom';

const MerchantToolbar = (props) => {
    const history = useHistory();

    const redirect = (path) => {
        history.push(path);
    }

    return (
        <React.Fragment>
            <Button color="inherit" onClick={() =>redirect('/payment-data') }>Insert Payment Data</Button>
            <Button color="inherit" onClick={() =>redirect('/merchant-books') }>Your books</Button>
        </React.Fragment>
    );
}

export default MerchantToolbar;