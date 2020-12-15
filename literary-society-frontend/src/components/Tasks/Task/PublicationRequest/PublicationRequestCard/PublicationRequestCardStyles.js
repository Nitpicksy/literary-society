import { makeStyles } from '@material-ui/core/styles';

export const useStyles = makeStyles((theme) => ({
  cardContent: {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    padding: theme.spacing(2, 4),
    minWidth: 555
  },
  title: {
    fontFamily: 'Barlow Condensed, Roboto',
    fontWeight: 500,
    lineHeight: 'normal',
    marginBottom: theme.spacing(3),
    textAlign: 'center',
  },
  synopsis: {
    fontSize: '1.1rem',
    lineHeight: 'normal',
    marginBottom: theme.spacing(0.4),
    textAlign: 'justify',
  },
  genre: {
    fontSize: '1.1rem',
    lineHeight: 'normal',
    marginBottom: theme.spacing(1),
  },
}));