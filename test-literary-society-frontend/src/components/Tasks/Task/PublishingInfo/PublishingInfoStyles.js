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
    upload: {
        margin: theme.spacing(1, 0),
    },
    fileNameGrid: {
        display: 'flex',
        alignItems: 'center',
    },
    fileName: {
        color: '#707070',
        fontWeight: 500,
        marginLeft: theme.spacing(1)
    }
}));