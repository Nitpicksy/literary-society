import { makeStyles } from "@material-ui/core/styles";

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
    title: {
        fontSize: 30
    },
    searchText: {
        color: theme.palette.primary.main,
        fontStyle: 'italic'
    },
    pagination: {
        marginBottom: theme.spacing(2)
    },
    pageNum: {
        color: 'rgba(0, 0, 0, 0.70)',
        margin: theme.spacing(1)
    }
}));