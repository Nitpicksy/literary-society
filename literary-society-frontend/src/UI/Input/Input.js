import React from 'react';
import TextField from '@material-ui/core/TextField';
import MenuItem from '@material-ui/core/MenuItem';

const input = (props) => {
    let inputElement = null;
    switch (props.elementType) {
        case ('input'):
            inputElement = <TextField variant="outlined" margin="normal" fullWidth
                error={props.error} helperText={props.error ? props.errorMessage : ''}
                {...props.elementConfig} onChange={props.changed} />;
            break;
        case ('textarea'):
            inputElement = <TextField variant="outlined" margin="normal" fullWidth
                error={props.error} helperText={props.error ? props.errorMessage : ''}
                {...props.elementConfig} onChange={props.changed}
                multiline />;
            break;
        case ('select'):
            inputElement = <TextField variant="outlined" margin="normal" fullWidth
                label={props.elementConfig.label} value={props.value} onChange={props.changed}
                select >
                {props.elementConfig.options.map(option => (
                    <MenuItem key={option.value} value={option.value}>{option.displayValue}</MenuItem>
                ))}
            </TextField>;
            break;
        default:
            inputElement = <TextField variant="outlined" margin="normal" fullWidth
                error={props.error} helperText={props.error ? props.errorMessage : ''}
                {...props.elementConfig} onChange={props.changed} />;
    }
    return (
        <div>
            {inputElement}
        </div>
    );
}

export default input;