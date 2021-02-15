import { makeStyles } from "@material-ui/core/styles";

export const useStyles = makeStyles((theme) => ({
    paper: {
        margin: theme.spacing(3, 0),
        padding: theme.spacing(1, 2),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    avatar: {
        margin: theme.spacing(1),
        backgroundColor: theme.palette.secondary.main,
    },
    buttons: {
        margin: theme.spacing(1.5, 0),
        display: 'flex',        
        alignItems: 'center',
    },
    clear: {
        marginLeft: theme.spacing(2)
    },
    header: {
        fontSize: 17,
        marginTop: theme.spacing(1),
        textAlign: 'center'
    },
    divider: {
        height: '2px',
        marginTop: theme.spacing(0.7)
    },
    row: {
        marginTop: theme.spacing(0.5),
        textAlign: 'center'
    },
    remove: {
        color: 'red',
        paddingTop: '9px'
    },
    searchBtn: {
        paddingRight: theme.spacing(6),
        paddingLeft: theme.spacing(6),
        marginTop: theme.spacing(2),
        marginBottom: theme.spacing(1)
    },
}));