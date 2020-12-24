import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles((theme) => ({
    paper: {
        marginTop: theme.spacing(8),
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
        paddingRight: theme.spacing(6),
        paddingLeft: theme.spacing(6),
        marginTop:  theme.spacing(2),
    },
    submitForm: {
        paddingRight: theme.spacing(8),
        paddingLeft: theme.spacing(8)
    },
    table: {
        minWidth: 100,
    },
    mainPaper: {
        marginTop:theme.spacing(2),
        padding: theme.spacing(2),
        backgroundColor: '#fafafa',
    },
    paymentData: {
        padding: theme.spacing(2),
        paddingBottom: theme.spacing(2),
    },
    mainData: {
        padding: theme.spacing(2),
    },
    gridData: {
        paddingTop: theme.spacing(3)
    },
    tablecell: {
        fontSize: '14pt'
    },
    fileName: {
        color: '#707070',
        fontWeight: 700,
        fontSize: '12px',
    },
    chooseCertificate:{
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
}));