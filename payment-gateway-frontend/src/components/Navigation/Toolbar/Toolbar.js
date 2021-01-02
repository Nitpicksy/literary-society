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
import AdminToolbar from './AdminToolbar';

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

    const roleAdmin = "ROLE_ADMIN";

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
        if(!role){
            return false;
        }
        if (props.role === role) {
            return true;
        }
        return false;
    }

    const redirect = (path) => {
        history.push(path);
    }

    let nonAuthenticated = null;
    let logOut = null;

    if (!props.isAuthenticated) {
        nonAuthenticated = <React.Fragment>
            <Button className={classes.button} color="inherit" onClick={() => redirect('/sign-in')}>Sign in</Button>
        </React.Fragment>
    } else {
        logOut = <Tooltip title="Sign Out">
            <IconButton onClick={() => routeChange("/sign-out")} edge="start" className={classes.menuButton} color="inherit" aria-label="sign-out">
                <PowerSettingsNewIcon />
            </IconButton>
        </Tooltip>;
        
    }
    return (
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6" className={classes.title} onClick={() => redirect('/')}>
                    Payment Gateway
                </Typography>
                <Button className={classes.button} color="inherit" onClick={() => redirect('/add-payment-method')}>Add payment method</Button>
                <Button className={classes.button} color="inherit" onClick={() => redirect('/add-company')}>Add company</Button>

                {checkRole(roleAdmin) ? <AdminToolbar /> : null}
                {nonAuthenticated}
                <Button className={classes.button} color="inherit" onClick={openMenuAccount}> Account </Button>
                <StyledMenu id="customized-menu" anchorEl={accountEl} keepMounted open={Boolean(accountEl)} onClose={handleCloseAccount}>
                    <MenuItem>
                        <ListItemText onClick={() => handleCloseAccount("/change-password")} primary="Change password" />
                    </MenuItem>
                </StyledMenu>
                {logOut}

            </Toolbar>
        </AppBar>
    );
}

export default CustomToolbar;