import { Button } from '@material-ui/core';
import React from 'react';
import { useStyles } from './ToolbarStyles';
import { useHistory } from 'react-router-dom';

const EditorToolbar = (props) => {
    const classes = useStyles();
    const history = useHistory();

    const redirect = (path) => {
        history.push(path);
    }

    return (
        <React.Fragment>
            <Button color="inherit" onClick={() =>redirect('/publication-requests') }>Hi</Button>
        </React.Fragment>
    );
}

export default EditorToolbar;