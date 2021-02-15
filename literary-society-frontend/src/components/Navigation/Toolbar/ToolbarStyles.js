import { makeStyles } from "@material-ui/core";

export const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
    },
    grow: {
        flexGrow: 1,
    },
    titleLink: {
        textDecoration: 'none',
        color: 'white'
    },
    menuBtn: {
        color: 'white'
    },  
    signOut: {
        marginLeft: theme.spacing(1)
    }
}));