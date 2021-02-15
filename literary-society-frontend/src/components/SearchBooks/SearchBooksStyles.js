import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles((theme) => ({
    paper: {
        margin: theme.spacing(3, 0),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    grid: {
        // margin: theme.spacing(3, 0),
        alignItems: 'center',
    },
    button:{
        float:'left', 
        marginLeft: theme.spacing(2),
        paddingTop: theme.spacing(1.25),
        paddingBottom: theme.spacing(1.25),
    },
    advancedSearch: {
        marginTop: theme.spacing(2),
        padding: theme.spacing(2),
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
    pageNum: {
        color: 'rgba(0, 0, 0, 0.70)',
        margin: theme.spacing(1)
    }
}));