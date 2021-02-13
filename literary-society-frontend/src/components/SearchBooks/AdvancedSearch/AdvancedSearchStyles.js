import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles((theme) => ({
    paper: {
        margin: theme.spacing(3, 0),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    grid: {
        alignItems: 'center',
    },
    header:{
        marginTop: theme.spacing(2)
    },
    headerValues:{
        textAlign:'left',
        marginRight:  theme.spacing(1),
    },
    values:{
        paddingRight:  theme.spacing(2),
    },
    button:{
        float:'left', 
        marginLeft: theme.spacing(2),
        paddingTop: theme.spacing(1.25),
        paddingBottom: theme.spacing(1.25),
    },
    advancedSearch: {
        marginTop: theme.spacing(2),
        padding: theme.spacing(2),
    },
    avatar: {
        margin: theme.spacing(1),
        backgroundColor: theme.palette.secondary.main,
    },
    button: {  
        // marginTop: theme.spacing(4),
        // alignItems: 'right'
        marginTop: theme.spacing(2),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    submit: {
        marginTop: theme.spacing(3),
        paddingRight: theme.spacing(12),
        paddingLeft: theme.spacing(12),
    },
}));