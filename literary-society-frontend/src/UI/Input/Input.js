import React from 'react';
import TextField from '@material-ui/core/TextField';

const input = (props) => {
    let inputElement = null;
    switch(props.elementType){
        case('input'):
            inputElement = <TextField error= {props.error} variant="outlined" margin="normal" fullWidth 
            {...props.elementConfig} helperText={props.error ? props.errorMessage: ''} onChange = {props.changed}/>
            break;
        case ('textarea'):
            inputElement = <textarea  
           
                     {...props.elementConfig} 
                     value = {props.value}
                     onChange = {props.changed}/>;
            break;
        case ('select'):
            inputElement = 
                    <select  
                        
                        value = {props.value}
                        onChange = {props.changed}> 
                        {props.elementConfig.options.map(option => (
                            <option key={option.value} value = {option.value}>{option.displayValue}</option>
                        ))}
                    </select>
            break;
        default:
                inputElement = <input 
                     {...props.elementConfig}
                     value = {props.value}
                     onChange = {props.changed}/>;
    }
    return (
        <div >
            <label  >{props.label}</label>
            {inputElement}
        </div>
    );
}

export default input;