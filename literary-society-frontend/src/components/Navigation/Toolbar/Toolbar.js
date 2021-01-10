import { AppBar, Button, IconButton, Toolbar, Typography, Menu, MenuItem, Tooltip } from '@material-ui/core';
import React from 'react';
import { useStyles } from './ToolbarStyles';
import { useHistory } from 'react-router-dom';
import PowerSettingsNewIcon from '@material-ui/icons/PowerSettingsNew';
import WriterToolbar from './WriterToolbar';
import MerchantToolbar from './MerchantToolbar';
import AdminToolbar from './AdminToolbar';
import ReaderToolbar from './ReaderToolbar';

const CustomToolbar = (props) => {
    const classes = useStyles();
    const history = useHistory();

    const roleWriter = "ROLE_WRITER";
    const roleReader = "ROLE_READER";
    const roleEditor = "ROLE_EDITOR";
    const roleAdmin = "ROLE_ADMIN";
    const roleLecturer = "ROLE_LECTURER";
    const roleMerchant = "ROLE_MERCHANT";
    const roleCommitteeMember = "ROLE_COMMITTEE_MEMBER";

    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };  

    const routeChange = (path) => {
        history.push(path);
    }

    const checkRole = (role) => {
        if (props.role === role) {
            return true;
        }
        return false;
    }

    const redirect = (path) => {
        setAnchorEl(null);
        history.push(path);
    }
    let tasks = null;
    if(props.isAuthenticated && (checkRole(roleWriter) || checkRole(roleEditor) || checkRole(roleReader) || checkRole(roleLecturer) || checkRole(roleCommitteeMember))){
        tasks = <Button className={classes.button} color="inherit" onClick={() => redirect('/tasks')}>Tasks</Button>;
    }

    let toolbarItems = (
        <React.Fragment>
            <Button color="inherit" onClick={() => redirect('/sign-in')}>Sign in</Button>
            <Button color="inherit" onClick={() => redirect('/sign-up-options')}>Sign up</Button>
        </React.Fragment>
    );

    if (props.isAuthenticated) {

        toolbarItems = (
            <React.Fragment>
                {tasks}

                {checkRole(roleWriter) ? <WriterToolbar /> : null}

                {checkRole(roleMerchant) ? <MerchantToolbar /> : null}

                {checkRole(roleAdmin) ? <AdminToolbar /> : null}

                {checkRole(roleReader) ? <ReaderToolbar /> : null}

                <Button aria-controls="simple-menu" aria-haspopup="true" onClick={handleClick} className={classes.menuBtn}>Account</Button>
                <Menu anchorEl={anchorEl} keepMounted open={Boolean(anchorEl)} onClose={handleClose} getContentAnchorEl={null}
                    anchorOrigin={{ vertical: "bottom", horizontal: "center" }} transformOrigin={{ vertical: "top", horizontal: "center" }}>
                    <MenuItem onClick={() => redirect('/change-password')}>Change password</MenuItem>
                </Menu>

                <Tooltip title="Sign Out">
                    <IconButton onClick={() => routeChange("/sign-out")} edge="start" className={classes.signOut} color="inherit" aria-label="sign-out">
                        <PowerSettingsNewIcon />
                    </IconButton>
                </Tooltip>
            </React.Fragment>
        );
    }
    return (
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6" className={classes.title} onClick={() => redirect('/')} style={{ cursor: 'pointer' }}>
                    Literary Society
                </Typography>
                <Button className={classes.button} color="inherit" onClick={() => redirect('/shopping-cart')}>Shopping Cart</Button>
                {toolbarItems}

            </Toolbar>
        </AppBar>
    );
}

export default CustomToolbar;