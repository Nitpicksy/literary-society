import React from 'react';
import TextField from '@material-ui/core/TextField';
import MenuItem from '@material-ui/core/MenuItem';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import { makeStyles } from '@material-ui/core';

const input = (props) => {
    let inputElement = null;
    const classes = makeStyles((theme) => ({
        container: {
            display: 'flex',
            flexWrap: 'wrap',
        },
        textField: {
            marginLeft: theme.spacing(1),
            marginRight: theme.spacing(1),
            width: 200,
        },
    }));

    switch (props.elementType) {
        case ('input'):
            inputElement = <TextField variant="outlined" margin="normal" fullWidth
                error={props.error} helperText={props.error ? props.errorMessage : ''}
                {...props.elementConfig} onChange={props.changed} />;
            break;
        case ('date'):
            inputElement = <TextField variant="outlined" margin="normal" fullWidth   type="date"
                error={props.error} {...props.elementConfig} onChange={props.changed} className={classes.textField}
                InputLabelProps={{ shrink: true, }}/>;
            break;
        case ('textarea'):
            inputElement = <TextField variant="outlined" margin="normal" fullWidth
                error={props.error} helperText={props.error ? props.errorMessage : ''}
                {...props.elementConfig} onChange={props.changed}
                multiline />;
            break;
        case ('selectOne'):
            inputElement = <TextField variant="outlined" margin="normal" fullWidth
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

            inputElement = <TextField variant="outlined" margin="normal" fullWidth
                label={props.elementConfig.label} value={props.value} onChange={props.changed}
                error={props.error} helperText={props.error ? props.errorMessage : ''}
                select SelectProps={{ multiple: multipleSelect }}>
                {props.elementConfig.options.map(option => (
                    <MenuItem key={option.value} value={option.value}>{option.displayValue}</MenuItem>
                ))}
            </TextField>;
            break;
        case ('checkbox'):
            inputElement = <FormControlLabel label={props.elementConfig.label}
                control={
                    <Checkbox onChange={props.changed} />
                } />;
            break;
        case ('long'):
            inputElement = null;
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