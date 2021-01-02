import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles((theme) => ({
    mainPaper: {
        marginTop:theme.spacing(3),
        marginBottom:theme.spacing(3),
        padding: theme.spacing(2, 3),
        backgroundColor: '#f5f5f5',
    },
    centered: {
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
        fontSize: 30
    },
    form: {
        width: '100%',
        marginTop: theme.spacing(1),
        marginBottom: theme.spacing(3),
    },
    submit: {
        margin: theme.spacing(3, 0, 2),
    },
    titlePaymentMethod: {
        margin: theme.spacing(3, 0, 0)
    }
}));