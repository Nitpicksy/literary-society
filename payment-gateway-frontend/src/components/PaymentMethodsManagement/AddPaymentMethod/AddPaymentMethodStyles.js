import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles((theme) => ({    
    mainPaper: {
        marginTop:theme.spacing(3),
        marginBottom:theme.spacing(3),
        padding: theme.spacing(2),
        backgroundColor: '#f5f5f5',
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
        marginTop: theme.spacing(6),
        paddingRight: theme.spacing(8),
        paddingLeft: theme.spacing(8)
    },
    table: {
        minWidth: 100,
    },
    tablecell: {
        fontSize: '13pt'
    },
    paymentData: {
        padding: theme.spacing(2),
        paddingBottom: theme.spacing(16),
    },
    mainData: {
        padding: theme.spacing(2),
    },
    gridData: {
        paddingTop: theme.spacing(3)
    },
    fileName: {
        color: '#707070',
        fontWeight: 600,
    },
    chooseCertificate:{
        marginTop: theme.spacing(2),
    },
    uploadBtn: {
        textAlign: 'right',
    },
    fileNameGrid: {
        display: 'flex',
        alignItems: 'center',
    },    
    centered: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
}));