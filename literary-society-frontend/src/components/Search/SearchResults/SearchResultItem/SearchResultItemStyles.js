import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles((theme) => ({
    card: {
        marginBottom: theme.spacing(2),
        '& .MuiCardContent-root': {
            paddingBottom: '0px'
        }
    },
    text: {
        fontSize: 15,
        lineHeight: 1.5,
        marginTop: theme.spacing(1),
        marginBottom: theme.spacing(1),
    }
}));