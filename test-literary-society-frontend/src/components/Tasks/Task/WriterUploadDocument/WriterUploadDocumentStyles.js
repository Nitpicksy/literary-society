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
    submit: {
        marginTop: theme.spacing(2),
        marginLeft: theme.spacing(56),
    },
    card: {
        marginTop: theme.spacing(2),
        display: 'flex',
        alignItems: 'center',
    },
    uploadGrid: {
        minWidth: 555,
        marginTop: theme.spacing(2),
    },
    uploadBtn: {
        textAlign: 'right',
    },
    fileNameGrid: {
        display: 'flex',
        alignItems: 'center',
    },
    fileName: {
        color: '#707070',
        fontWeight: 500,
    }
}));