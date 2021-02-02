import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles((theme) => ({
    paper: {
        margin: theme.spacing(3, 0),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    avatar: {
        margin: theme.spacing(1),
        backgroundColor: theme.palette.secondary.main,
    },
    card: {
        marginTop: theme.spacing(4),
        padding: theme.spacing(1),
    },
    cardContent: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    title: {
        marginBottom: theme.spacing(2),
    },
}));