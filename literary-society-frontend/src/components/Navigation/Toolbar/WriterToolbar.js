import { Button, Menu, MenuItem } from '@material-ui/core';
import React from 'react';
import { useStyles } from './ToolbarStyles';
import { useHistory } from 'react-router-dom';

const WriterToolbar = (props) => {
    const classes = useStyles();
    const history = useHistory();

    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const redirect = (path) => {        
        setAnchorEl(null);
        history.push(path);
    }

    return (
        <React.Fragment>
            <Button color="inherit" onClick={() =>redirect('/publication-requests') }>Publication Requests</Button>
            <Button aria-controls="simple-menu" aria-haspopup="true" onClick={handleClick} className={classes.menuBtn}>Membership</Button>
            <Menu anchorEl={anchorEl} keepMounted open={Boolean(anchorEl)} onClose={handleClose} getContentAnchorEl={null}
                anchorOrigin={{ vertical: "bottom", horizontal: "center" }} transformOrigin={{ vertical: "top", horizontal: "center" }}>
                <MenuItem onClick={() => redirect('/membership')}>One time</MenuItem>
                <MenuItem onClick={() => redirect('/subscription')}>Subscription</MenuItem>
            </Menu>
        </React.Fragment>
    );
}

export default WriterToolbar;