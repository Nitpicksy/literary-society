import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles((theme) => ({
    root: {
        backgroundColor: '#F5F5F5',
    },
    cardcontent: {
        height: 150,
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        paddingTop: 10,
        paddingBottom: 2,
    },
    title: {
        fontSize: '1.35rem',
        fontWeight: 500,
        lineHeight: 'normal',
        marginBottom: theme.spacing(0.4),
    },
    writersNames: {
        fontFamily: 'Bitter, Roboto',
        fontSize: '0.85rem',
        fontWeight: 500,
    },
    content: {
        paddingTop: theme.spacing(0.5),
        fontSize: '0.85rem',
        fontWeight: 500,
        lineHeight: 'normal',
        color: '#596270'
    },
}));