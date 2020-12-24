import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles((theme) => ({
    paper: {
        marginTop: theme.spacing(2),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    avatar: {
        margin: theme.spacing(1),
        backgroundColor: theme.palette.secondary.main,
    },
    title: {
        textAlign: 'center',
    },
    submit: {
        marginTop: theme.spacing(2),
        marginLeft: theme.spacing(55),
    },
    card: {
        marginTop: theme.spacing(1),
        display: 'flex',
        alignItems: 'center',
    },
}));