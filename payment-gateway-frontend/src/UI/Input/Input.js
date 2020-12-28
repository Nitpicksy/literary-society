import React from 'react';
import TextField from '@material-ui/core/TextField';
import MenuItem from '@material-ui/core/MenuItem';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';

const input = (props) => {
    let inputElement = null;
    switch (props.elementType) {
        case ('input'):
            inputElement = <TextField margin="normal" fullWidth value ={props.value}
                error={props.error} helperText={props.error ? props.errorMessage : ''}
                {...props.elementConfig} onChange={props.changed} />;
            break;
        case ('textarea'):
            inputElement = <TextField margin="normal" fullWidth
                error={props.error} helperText={props.error ? props.errorMessage : ''}
                {...props.elementConfig} onChange={props.changed}
                multiline />;
            break;
        case ('selectOne'):
            inputElement = <TextField margin="normal" fullWidth
                label={props.elementConfig.label} value={props.value} onChange={props.changed}
                error={props.error} helperText={props.error ? props.errorMessage : ''}
                select>
                {props.elementConfig.options.map(option => (
                    <MenuItem key={option.value} value={option.value}>{option.displayValue}</MenuItem>
                ))}
            </TextField>;
            break;
        case ('selectMultiple'):
            let multipleSelect = props.additionalData.multipleSelect === 'true' ? true : false;
            
            inputElement = <TextField margin="normal" fullWidth
                label={props.elementConfig.label} value={props.value} onChange={props.changed}
                error={props.error} helperText={props.error ? props.errorMessage : ''}
                select SelectProps={{ multiple: multipleSelect }}>
                {props.elementConfig.options.map(option => (
                    <MenuItem key={option.value} value={option.value}>{option.displayValue}</MenuItem>
                ))}
            </TextField>;
            break;
        case ('checkbox'):
            inputElement = <FormControlLabel  label={props.elementConfig.label} style={{float: "left"}}
                control={
                    <Checkbox onChange={props.changed} />
                }
            />;
            break;
        default:
            inputElement = <TextField margin="normal" fullWidth
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