import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles((theme) => ({
    root: {
        backgroundColor: '#F5F5F5',
    },
    cardcontent: {
        height: 110,
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        paddingTop: 4,
        paddingBottom: 2,
    },
    title: {
        fontSize: '1.30rem',
        fontWeight: 500,
        lineHeight: 'normal',
        marginBottom: theme.spacing(0.4),
    },
    similarProcentRed: {
        color: 'red',
    },
    similarProcent: {
        fontFamily: 'Bitter, Roboto',
        fontSize: '1.20rem',
        fontWeight: 500,
        lineHeight: 'normal',
    },
}));