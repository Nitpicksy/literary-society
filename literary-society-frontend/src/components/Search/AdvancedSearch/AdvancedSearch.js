import React, { useEffect, useState } from 'react';
import { Avatar, Button, Checkbox, Container, CssBaseline, Divider, Grid, Icon, IconButton, MenuItem, Paper, TextField, Typography } from '@material-ui/core';
import AdvSearchIcon from '../../../icons/advanced-search.svg';
import RemoveCircleOutlineIcon from '@material-ui/icons/RemoveCircleOutline';
import SearchIcon from '@material-ui/icons/Search';
import ClearIcon from '@material-ui/icons/Clear';
import AddIcon from '@material-ui/icons/Add';
import * as actions from './AdvancedSearchAction';
import { connect } from 'react-redux';
import { useStyles } from './AdvancedSearchStyles';
import SearchResults from '../SearchResults/SearchResults';

const AdvancedSearch = (props) => {
    const classes = useStyles();

    let rows = null;
    let results = null;

    const [showResults, setShowResults] = useState(false);
    const [queryParams, setQueryParams] = useState([
        {
            row: 1,
            name: 'text',
            value: '',
            isPhrase: false,
            boolQueryType: 'AND'
        }
    ]);

    useEffect(() => {
        let params = JSON.parse(localStorage.getItem('queryParams'));
        localStorage.removeItem('queryParams');
        if (params) {
            setQueryParams(params);
            setShowResults(true);
        }
    }, []);

    const inputChangedHandler = (event, row, name) => {
        let value = event.target.value;
        if (name === 'isPhrase') {
            value = event.target.checked;
        }

        const updatedQueryParams = [...queryParams];
        for (let i in updatedQueryParams) {
            if (updatedQueryParams[i].row === row) {
                updatedQueryParams[i][name] = value;
            }
        }
        setQueryParams(updatedQueryParams);
    };

    const handleAdvancedSearch = () => {
        localStorage.setItem('pageNumber', 1);
        props.advancedSearch(queryParams);
        setShowResults(true);
    };

    const addRow = () => {
        let lastRow = queryParams[queryParams.length - 1].row;
        const updatedQueryParams = [...queryParams];
        updatedQueryParams.push(
            {
                row: lastRow + 1,
                name: 'text',
                value: '',
                isPhrase: false,
                boolQueryType: 'AND'
            }
        );
        setQueryParams(updatedQueryParams);
    };

    const removeRow = (row) => {
        if (queryParams.length > 1) {
            let updatedQueryParams = [...queryParams];
            updatedQueryParams = updatedQueryParams.filter(queryParam => queryParam.row !== row);
            setQueryParams(updatedQueryParams);
        }
    };

    const clearFields = () => {
        setQueryParams([
            {
                row: 1,
                name: 'text',
                value: '',
                isPhrase: false,
                boolQueryType: 'AND'
            }
        ]);
        setShowResults(false);
    }

    if (queryParams.length > 0) {
        rows = queryParams.map(queryParam => {
            return <Grid key={queryParam.row} container spacing={2} className={classes.row}>
                <Grid item md={4}>
                    <TextField fullWidth variant="outlined" size="small"
                        value={queryParam.value} onChange={event => inputChangedHandler(event, queryParam.row, 'value')}>
                    </TextField>
                </Grid>
                <Grid item md={2}>
                    <TextField select fullWidth variant="outlined" size="small"
                        value={queryParam.name} onChange={event => inputChangedHandler(event, queryParam.row, 'name')}>
                        <MenuItem key="text" value="text" selected>Text</MenuItem>
                        <MenuItem key="title" value="title">Title</MenuItem>
                        <MenuItem key="writers" value="writers">Writers</MenuItem>
                        <MenuItem key="genre" value="genre">Genre</MenuItem>
                    </TextField>
                </Grid>
                <Grid item md={3}>
                    <TextField select fullWidth variant="outlined" size="small"
                        value={queryParam.boolQueryType} onChange={event => inputChangedHandler(event, queryParam.row, 'boolQueryType')}>
                        <MenuItem key="AND" value="AND" selected>Must contain</MenuItem>
                        <MenuItem key="OR" value="OR">Should contain</MenuItem>
                    </TextField>
                </Grid>
                <Grid item md={2}>
                    <Checkbox checked={queryParam.isPhrase}
                        onChange={event => inputChangedHandler(event, queryParam.row, 'isPhrase')} />
                </Grid>
                <Grid item md={1}>
                    <IconButton aria-label="remove" className={classes.remove} onClick={() => removeRow(queryParam.row)}>
                        <RemoveCircleOutlineIcon />
                    </IconButton>
                </Grid>
            </Grid>;
        });
    }

    if (showResults) {
        results = <SearchResults isAdvancedSearch={true} queryParams={queryParams} />;
    }

    return (
        <React.Fragment>
            <Container component="main" maxWidth="md">
                <CssBaseline />
                <Paper className={classes.paper}>
                    <Avatar className={classes.avatar}>
                        <Icon>
                            <img src={AdvSearchIcon} height={24} width={24} alt="Advanced Search" />
                        </Icon>
                    </Avatar>
                    <Typography component="h1" variant="h4" className={classes.title}>Advanced search</Typography>
                    <div className={classes.buttons}>
                        <Grid container>
                            <Button variant="outlined" color="primary" size="small"
                                startIcon={<AddIcon />} onClick={addRow}>
                                Add row
                        </Button>
                            <Button variant="outlined" size="small" className={classes.clear}
                                startIcon={<ClearIcon />} onClick={clearFields}>
                                Clear
                    </Button>
                        </Grid>
                    </div>
                    <Grid container spacing={2} className={classes.header}>
                        <Grid item md={4}>Query</Grid>
                        <Grid item md={2}>Attribute</Grid>
                        <Grid item md={3}>Contain</Grid>
                        <Grid item md={2}>Is phrase?</Grid>
                        <Grid item md={1}>Remove</Grid>
                    </Grid>
                    <Divider flexItem={true} className={classes.divider} />
                    {rows}
                    <Button variant="contained" color="primary" className={classes.searchBtn}
                        startIcon={<SearchIcon />} onClick={handleAdvancedSearch}>Search books</Button>
                </Paper>
            </Container>
            {results}
        </React.Fragment>
    );
};

const mapDispatchToProps = dispatch => {
    return {
        advancedSearch: (queryParams, pageNum = 1, pageSize = 4) => dispatch(actions.advancedSearch(queryParams, pageNum, pageSize)),
    }
};

export default connect(null, mapDispatchToProps)(AdvancedSearch);