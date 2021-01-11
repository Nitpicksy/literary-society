import { makeStyles } from "@material-ui/core/styles";

export const useStyles = makeStyles((theme) => ({
  card: {
    marginTop: theme.spacing(8),
    padding: theme.spacing(1),
    backgroundColor: '#f5f5f5',
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
    fontSize: '1.5rem',
  }
}));
