import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles((theme) => ({
    paper: {
        marginTop: theme.spacing(3),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        padding: '5px'
    },
    avatar: {
        margin: theme.spacing(2),
        backgroundColor: theme.palette.secondary.main,
    },
    title: {
        textAlign: 'center',
    },
    cardContent: {
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        padding: theme.spacing(2, 4),
        minWidth: 450
      },
}));