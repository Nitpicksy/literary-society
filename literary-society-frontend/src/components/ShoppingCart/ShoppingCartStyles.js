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
    selectMerchant: {
        minWidth: 350,
        margin: theme.spacing(4, 'auto', 1),
    },
    submit: {
        margin: theme.spacing(3, 0, 2),
    },
    price: {
        margin: theme.spacing(2.5, 0, 1),
    },
    priceValue: {
       fontWeight:500
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
}));