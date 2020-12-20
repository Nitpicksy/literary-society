import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles((theme) => ({
  root: {
    maxWidth: 170,
    backgroundColor: '#F5F5F5',
  },
  media: {
    height: 260,
  },
  imageDiv: {
    position: 'relative',
  },
  tooltip: {
    position: 'absolute',
    bottom: 5,
    right: 5,
  },
  discount: {
    backgroundColor: '#d32f2f !important',
    color: 'white !important',
    fontWeight: '500 !important',
    fontSize: '1rem !important',
  },
  cardcontent: {
    height: 120,
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    paddingTop: 10,
    paddingBottom: 2,
  },
  title: {
    fontFamily: 'Barlow Condensed, Roboto',
    fontWeight: 500,
    fontSize: '1.2rem',
    lineHeight: 'normal',
    marginBottom: theme.spacing(0.4),
  },
  writersNames: {
    fontFamily: 'Bitter, Roboto',
    fontSize: '0.85rem',
    fontWeight: 500,
  },
  price: {
    fontSize: '1.1rem',
    fontWeight: 500,
    color: '#d32f2f',
  },
  merchantName: {
    fontSize: '0.75rem',
    fontWeight: 500,
    fontStyle: 'italic',
  },
}));