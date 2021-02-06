import React from 'react';
import { Checkbox, FormControlLabel, MenuItem, TextField, Button, makeStyles, Typography } from '@material-ui/core';

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
        case ('file'):
            inputElement = <React.Fragment>
                <input type="file" accept="application/pdf" hidden id="upload-file"
                    onChange={props.changed}
                />
                <label htmlFor="upload-file">
                    <Button color="primary" variant="contained" component="span">
                        Choose file
                    </Button>
                </label>
                <Typography variant="body2" component="span" style={{marginLeft: '5px', color: '#707070', fontWeight: 500}}>
                    {props.additionalData.fileName ? props.additionalData.fileName : ''}
                </Typography>
            </React.Fragment>;
            break;
        case ('date'):
            inputElement = <TextField variant="outlined" margin="normal" fullWidth type="date"
                error={props.error} {...props.elementConfig} onChange={props.changed} className={classes.textField}
                InputLabelProps={{ shrink: true, }} />;
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