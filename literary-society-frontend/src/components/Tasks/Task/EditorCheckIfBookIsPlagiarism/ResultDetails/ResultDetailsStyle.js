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
    subTitle: {
        fontSize: '1.20rem',
        fontWeight: 450,
        lineHeight: 'normal',
        marginBottom: theme.spacing(0.4),
        textAlign: 'left',
        color: 'crimson',
    },
    content: {
        fontSize: '1.30rem',
        color: 'black',
    },
    card: {
        marginTop: theme.spacing(1),
        display: 'flex',
        alignItems: 'center',
    },
    similarProcentRed: {
        fontSize: '1.30rem',
        color: 'red',
    },
}));