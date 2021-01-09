import { makeStyles } from "@material-ui/core/styles";

export const useStyles = makeStyles((theme) => ({
  card: {
    marginTop: theme.spacing(8),
    padding: theme.spacing(1),
  },
  cardContent: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  avatar: {
    marginTop: theme.spacing(1),
    marginBottom: theme.spacing(2),
    backgroundColor: theme.palette.secondary.main,
  },
  message: {
    textAlign: 'center',
    marginBottom: theme.spacing(1),
  },
  product: {    
    margin: theme.spacing(1, 0),
    color: '#d32f2f',
  },
  price: {
    marginTop: theme.spacing(2),
    marginBottom: theme.spacing(4),
    color: '#d32f2f',
    fontWeight: 500,
    fontSize: 26
  },
}));
