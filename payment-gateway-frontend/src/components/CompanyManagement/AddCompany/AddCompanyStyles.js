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
    form: {
        width: '100%',
        marginTop: theme.spacing(1),
    },
    submit: {
        marginTop: theme.spacing(3),
        paddingRight: theme.spacing(12),
        paddingLeft: theme.spacing(12),
    },
    fileName: {
        color: '#707070',
        fontWeight: 600
    },
    chooseCertificate: {
        minWidth: 555,
        marginTop: theme.spacing(2),
    },
    uploadBtn: {
        textAlign: 'right',
    },
    fileNameGrid: {
        display: 'flex',
        alignItems: 'center',
        paddingLeft: theme.spacing(2),
    },
    paymentMethodPaper: {
        margin: theme.spacing(1.2),
        padding: theme.spacing(0.5, 2),
        display: 'inline-block',
    },
    chkLabel: {
        fontSize: '1.2rem',
        fontWeight: 600
    }
}));