import { AppBar, Button, IconButton, Toolbar, Typography } from '@material-ui/core';
import React from 'react';
import { useStyles } from './ToolbarStyles';
import { useMenuItemStyles } from './ToolbarStyles';
import { useHistory } from 'react-router-dom';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import PowerSettingsNewIcon from '@material-ui/icons/PowerSettingsNew';
import ListItemText from '@material-ui/core/ListItemText';
import { withStyles } from '@material-ui/core/styles';

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
    const menuItemClasses = useMenuItemStyles();
    const history = useHistory();
    const [accountEl, setAccountEl] = React.useState(null);

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

    return (
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6" className={classes.title}> Literary Society </Typography>
                <Button aria-controls="customized-menu" aria-haspopup="true" variant="contained" color="primary" onClick={openMenuAccount}> Account </Button>
                <StyledMenu id="customized-menu" anchorEl={accountEl} keepMounted open={Boolean(accountEl)} onClose={handleCloseAccount}>
                    <MenuItem className={menuItemClasses.root}>
                        <ListItemText onClick={() => handleCloseAccount("/change-password")} primary="Change password" />
                    </MenuItem>
                    <MenuItem className={menuItemClasses.root}>
                        <ListItemText primary="My account" />
                    </MenuItem>
                </StyledMenu>
                <IconButton onClick={() => routeChange("/sign-out")} edge="start" className={classes.menuButton} color="inherit" aria-label="menu">
                    <PowerSettingsNewIcon />
                </IconButton>
            </Toolbar>
        </AppBar>
    );
}

export default CustomToolbar;