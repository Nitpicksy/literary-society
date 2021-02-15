import React from 'react';
import { InputBase } from '@material-ui/core';
import SearchIcon from '@material-ui/icons/Search';
import * as actions from './BaseSearchAction';
import { connect } from 'react-redux';
import { useStyles } from './BaseSearchStyles';
import { useHistory } from 'react-router';

const BaseSearch = (props) => {
    const classes = useStyles();
    const history = useHistory();

    const handleSearch = (event) => {
        if (event.key === 'Enter' && event.target.value.trim() !== '') {
            localStorage.setItem('pageNumber', 1);
            props.baseSearch(history, event.target.value);
        }
    }    

    return (
        <div className={classes.search}>
            <div className={classes.searchIcon}>
                <SearchIcon />
            </div>
            <InputBase
                placeholder="Search books…"
                classes={{
                    root: classes.inputRoot,
                    input: classes.inputInput,
                }}
                inputProps={{ 'aria-label': 'search' }}
                onKeyPress={handleSearch}
            />
        </div>
    );
};

const mapDispatchToProps = dispatch => {
    return {
        baseSearch: (history, searchText, pageNum = 1, pageSize = 4) => dispatch(actions.baseSearch(history, searchText, pageNum, pageSize)),
    }
};

export default connect(null, mapDispatchToProps)(BaseSearch);