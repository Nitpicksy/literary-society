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
    form: {
        width: '100%',
        marginTop: theme.spacing(1),
    },
    submit: {
        marginTop: theme.spacing(2),
    },
    card: {
        marginTop: theme.spacing(1),
        display: 'flex',
        alignItems: 'center',
    },
    cardContent: {
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        padding: theme.spacing(2, 4),
        minWidth: 450
      },
    title: {
        fontFamily: 'Barlow Condensed, Roboto',
        fontWeight: 500,
        lineHeight: 'normal',
        marginBottom: theme.spacing(3),
        textAlign: 'center',
    },
}));