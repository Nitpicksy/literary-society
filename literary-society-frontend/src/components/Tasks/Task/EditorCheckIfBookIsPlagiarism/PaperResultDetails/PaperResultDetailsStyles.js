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
    grid: {
        marginBottom: theme.spacing(2)
    },
    cardContent: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'flex-start',
    },
    header: {
        fontWeight: 500,
        color: 'crimson'
    },
    boldFont: {
        fontSize: '1.1rem',
    },
    similarity: {
        fontWeight: 600
    }, 
    red: {
        fontWeight: 600,
        color: 'red'
    }
}));