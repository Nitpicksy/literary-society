import { createMuiTheme } from '@material-ui/core/styles';
import { teal } from '@material-ui/core/colors';

const theme = createMuiTheme({
    palette: {
        primary: teal,
        secondary: {
            main: '#D2B48C'
        }
    },
    typography: {
        fontFamily: 'El Messiri',
        fontSize: 17,
        fontWeightRegular: 500
    }
});

export default theme;