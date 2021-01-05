import { AppBar, Button, IconButton, Toolbar, Typography } from '@material-ui/core';
import React from 'react';
import { useStyles } from './ToolbarStyles';
import { useHistory } from 'react-router-dom';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import PowerSettingsNewIcon from '@material-ui/icons/PowerSettingsNew';
import ListItemText from '@material-ui/core/ListItemText';
import Tooltip from '@material-ui/core/Tooltip';
import { withStyles } from '@material-ui/core/styles';
import WriterToolbar from './WriterToolbar';
import MerchantToolbar from './MerchantToolbar';
import AdminToolbar from './AdminToolbar';
import ReaderToolbar from './ReaderToolbar';

const StyledMenu = withStyles({
    paper: {
        border: '1px solid #d3d4d5',
    },
})((props) => (
    <Menu
        elevation={0}
        getContentAnchorEl={null}
        anchorOrigin={{
            vertical: 'bottom',
            horizontal: 'center',
        }}
        transformOrigin={{
            vertical: 'top',
            horizontal: 'center',
        }}
        {...props}
    />
));


const CustomToolbar = (props) => {
    const classes = useStyles();
    const history = useHistory();
    const [accountEl, setAccountEl] = React.useState(null);

    const roleWriter = "ROLE_WRITER";
    const roleReader = "ROLE_READER";
    const roleEditor = "ROLE_EDITOR";
    const roleAdmin = "ROLE_ADMIN";
    const roleLecturer = "ROLE_LECTURER";
    const roleMerchant = "ROLE_MERCHANT";

    const openMenuAccount = (event) => {
        setAccountEl(event.currentTarget);
    };

    const handleCloseAccount = (path) => {
        setAccountEl(null);
        if (path) {
            history.push(path);
        }

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
        history.push(path);
    }
    let tasks = null;
    if(props.isAuthenticated && (checkRole(roleWriter) || checkRole(roleEditor) || checkRole(roleReader) || checkRole(roleLecturer))){
        tasks = <Button className={classes.button} color="inherit" onClick={() => redirect('/tasks')}>Tasks</Button>;
    }

    let toolbarItems = (
        <React.Fragment>
            <Button className={classes.button} color="inherit" onClick={() => redirect('/sign-in')}>Sign in</Button>
            <Button className={classes.button} color="inherit" onClick={() => redirect('/sign-up-options')}>Sign up</Button>
        </React.Fragment>
    );

    if (props.isAuthenticated) {

        toolbarItems = (
            <React.Fragment>
                {tasks}

                {checkRole(roleWriter) ? <WriterToolbar /> : null}

                { checkRole(roleMerchant) ? <MerchantToolbar /> : null }

                { checkRole(roleAdmin) ? <AdminToolbar /> : null }

                { checkRole(roleReader) ? <ReaderToolbar /> : null }
                
                <Button className={classes.button} color="inherit" onClick={openMenuAccount}> Account </Button>
                <StyledMenu id="customized-menu" anchorEl={accountEl} keepMounted open={Boolean(accountEl)} onClose={handleCloseAccount}>
                    <MenuItem>
                        <ListItemText onClick={() => handleCloseAccount("/change-password")} primary="Change password" />
                    </MenuItem>
                </StyledMenu>

                <Tooltip title="Sign Out">
                    <IconButton onClick={() => routeChange("/sign-out")} edge="start" className={classes.menuButton} color="inherit" aria-label="sign-out">
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