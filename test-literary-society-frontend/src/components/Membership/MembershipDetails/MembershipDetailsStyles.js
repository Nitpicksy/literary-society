import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles((theme) => ({
    paper: {
        padding: theme.spacing(2)
    },
    bookData: {
        margin: theme.spacing(10, 0),
    },
    bookInfo: {
        paddingLeft: 60
    },
    title: {
        fontFamily: 'Barlow Condensed, Roboto',
        fontWeight: 500,
        fontSize: '2rem',
        lineHeight: 'normal',
        marginBottom: theme.spacing(0.5),
    },
    genre: {
        fontFamily: 'Bitter, Roboto',
        fontWeight: 500,
        fontSize: '1.1rem',
        marginBottom: theme.spacing(2),
    },
    isbn: {
        fontFamily: 'Bitter, Roboto',
        fontWeight: 500,
        fontSize: '0.9rem',
        marginBottom: theme.spacing(0.4),
    },
    publicationDate: {
        fontFamily: 'Bitter, Roboto',
        fontWeight: 500,
        fontSize: '0.9rem',
        marginBottom: theme.spacing(0.4),
    },
    publisherCity: {
        fontFamily: 'Bitter, Roboto',
        fontWeight: 500,
        fontSize: '0.9rem',
    },
    writer: {
        // fontFamily: 'Bitter, Roboto',
        fontWeight: 500,
        fontSize: '1.1rem',
        marginBottom: theme.spacing(0.3),
        marginTop: theme.spacing(2),
    },
    publisher: {
        // fontFamily: 'Bitter, Roboto',
        fontWeight: 500,
        fontSize: '1.1rem',
        marginBottom: theme.spacing(0.3),
    },
    merchant: {
        //fontFamily: 'Bitter, Roboto',
        fontWeight: 500,
        fontSize: '1.1rem',
        marginBottom: theme.spacing(2.5),
    },
    synopsis: {
        fontWeight: 400,
        fontSize: '1rem',
        textAlign: 'justify',
        paddingRight: 4
    },
    imageDiv: {
        position: 'relative',
    },
    tooltip: {
        position: 'absolute',
        bottom: 10,
        right: 10,
    },
    discount: {
        backgroundColor: '#d32f2f',
        color: 'white',
        fontWeight: 500,
        fontSize: '1.8rem',
        padding: theme.spacing(3, 0)
    },
    price: {
        fontSize: '2rem',
        fontWeight: 500,
        color: '#d32f2f',
    },
    priceGrid: {
        textAlign: 'right',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center'
    }
}));