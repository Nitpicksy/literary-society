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
    form: {
        width: '100%',
        marginTop: theme.spacing(1),
        marginBottom: theme.spacing(3),
    },
    submit: {
        marginTop: theme.spacing(2),
    },
    card: {
        marginTop: theme.spacing(1),
        display: 'flex',
        alignItems: 'center',
    },
    paperPercentage: {
        marginTop: theme.spacing(2),
        padding: theme.spacing(2, 2),
        minWidth: 555,
    },
    similarBooks: {
        textAlign: 'center',
        fontSize: 28,
        marginBottom: theme.spacing(1)
    },
    cellFont: {
        fontSize: '1.05rem',
        paddingTop: theme.spacing(1),
        paddingBottom: theme.spacing(1),     
    },
    cell: {
        fontSize: '1.05rem',  
        paddingTop: theme.spacing(0),
        paddingBottom: theme.spacing(0),  
    },
    cellPadding: {
        paddingTop: theme.spacing(0),
        paddingBottom: theme.spacing(0),
    },
    cellBtn: {        
        paddingTop: theme.spacing(0),
        paddingBottom: theme.spacing(0),
        paddingRight: theme.spacing(0),
    },
    cellIcon: {
        padding: theme.spacing(0),
    }
}));