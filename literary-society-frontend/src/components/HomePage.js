import { React } from 'react';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';

const HomePage = (props) => {
    return (
        <Container component="main" maxWidth="xs">
            <Typography component="h1" variant="h4">Home page</Typography>
        </Container>
    );
};


export default HomePage;