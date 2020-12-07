import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles((theme) => ({
    paper: {
        marginTop: theme.spacing(8),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    avatar: {
        margin: theme.spacing(1),
        backgroundColor: theme.palette.secondary.main,
    },
    form: {
        width: '100%',
        marginTop: theme.spacing(1),
    },
    errorText: {
        color: 'red',
        textAlign: 'center',
        margin: theme.spacing(2, 0, 0, 0),
    },
    submit: {
        margin: theme.spacing(3, 0, 2),
    },
}));