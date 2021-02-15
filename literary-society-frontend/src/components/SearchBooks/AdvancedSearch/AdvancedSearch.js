import React, { useEffect, useState } from 'react';
import { useStyles } from './AdvancedSearchStyles';
import { connect } from 'react-redux';
import { CssBaseline, Button, Typography, Container, Avatar, Grid, Paper, MenuItem, TextField } from '@material-ui/core';
import RemoveCircleIcon from '@material-ui/icons/RemoveCircle';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import AddCircleIcon from '@material-ui/icons/AddCircle';
import SearchIcon from '@material-ui/icons/Search';
import { toastr } from 'react-redux-toastr';
import ClearIcon from '@material-ui/icons/Clear';
import * as actions from '../SearchBooksActions';

const AdvancedSearch = (props) => {

    const classes = useStyles();
    const { advanceSearchValues, setAdvanceSearchValues, search } = props;
    let gridItems = null;

    const inputChangedHandler = (event, advanceSearchValueId, advanceSearchValueType) => {
        let value = event.target.value;

        if (advanceSearchValueType === 'phraseQuery') {
            value = event.target.checked;
        }

        const updatedAdvanceSearchValues = [...advanceSearchValues]

        for (let i in updatedAdvanceSearchValues) {
            if (i == advanceSearchValueId) {
                if (advanceSearchValueType === "attributeName") {
                    updatedAdvanceSearchValues[i].attributeName = value;
                } else if (advanceSearchValueType === "searchValue") {
                    updatedAdvanceSearchValues[i].searchValue = value;
                } else if (advanceSearchValueType === "phraseQuery") {
                    updatedAdvanceSearchValues[i].phraseQuery = value;
                } else if (advanceSearchValueType === "type") {
                    updatedAdvanceSearchValues[i].type = value;
                }
            }
        }
        setAdvanceSearchValues(updatedAdvanceSearchValues);
    }

    if (advanceSearchValues.length > 0) {
        gridItems = advanceSearchValues.map(advanceSearchValue => {
            return <div className={classes.header} key={advanceSearchValue.id} >
                <Grid container align="center" className={classes.grid}>
                    <Grid item md={2}>
                        <TextField fullWidth className={classes.values} onChange={(event) => inputChangedHandler(event, advanceSearchValue.id, "type")}
                            value={advanceSearchValue.type} select variant="outlined" size="small">
                            <MenuItem key="AND" value="AND" selected>Required</MenuItem>
                            <MenuItem key="OR" value="OR" >Optional</MenuItem>
                        </TextField>
                    </Grid>
                    <Grid item md={6}>
                        <TextField fullWidth className={classes.values} onChange={(event) => inputChangedHandler(event, advanceSearchValue.id, "searchValue")}
                            value={advanceSearchValue.searchValue} variant="outlined" size="small">
                        </TextField>
                    </Grid>
                    <Grid item md={2}>
                        <TextField fullWidth className={classes.values} onChange={(event) => inputChangedHandler(event, advanceSearchValue.id, "attributeName")}
                            value={advanceSearchValue.attributeName} select variant="outlined" size="small">
                            <MenuItem key="title" value="title" selected>Title</MenuItem>
                            <MenuItem key="writers" value="writers" >Writers</MenuItem>
                            <MenuItem key="content" value="content" selected>Content</MenuItem>
                            <MenuItem key="genreInfo" value="genreInfo" >Genre</MenuItem>
                        </TextField>
                    </Grid>
                    <Grid item md={1} >
                        <FormControlLabel
                            control={
                                <Checkbox style={{
                                    transform: "scale(1.2)",

                                }} value={advanceSearchValue.phraseQuery} onChange={(event) => inputChangedHandler(event, advanceSearchValue.id, "phraseQuery")}
                                    checked={advanceSearchValue.phraseQuery} />
                            } />
                    </Grid>
                    <Grid item md={1} style={{ paddingTop: 3 }}>
                        <RemoveCircleIcon style={{
                            transform: "scale(1.2)",
                            color: "red"
                        }} onClick={() => removeSearchItem(advanceSearchValue.id)} />
                    </Grid>
                </Grid>
            </div>
        });
    }

    const addSearchItem = () => {
        const updatedAdvanceSearchValues = [...advanceSearchValues,
        {
            "id": advanceSearchValues.length,
            "attributeName": "title",
            "searchValue": "",
            "phraseQuery": false,
            "type": "AND"
        }];
        setAdvanceSearchValues(updatedAdvanceSearchValues);
    }

    const removeSearchItem = (index) => {
        if (advanceSearchValues.length !== 1) {
            let updatedAdvanceSearchValues = [...advanceSearchValues]
            updatedAdvanceSearchValues = updatedAdvanceSearchValues.filter(item => item.id !== index)
            for (let i in updatedAdvanceSearchValues) {
                if (i >= index) {
                    updatedAdvanceSearchValues[i].id = updatedAdvanceSearchValues[i].id - 1;
                }

            }
            setAdvanceSearchValues(updatedAdvanceSearchValues);
        } else {
            toastr.error('Search', "You cannot remove last item.");
        }

    }

    const checkSearchValues = () => {

        for (let i in advanceSearchValues) {
            if (advanceSearchValues[i].searchValue === "") {
                toastr.error('Search', "You have to enter a content value for all items.");
                return;
            }
        }
        search();
    }

    const clearSearch = () => {
        const updatedAdvanceSearchValues = [
            {
                "id": 0,
                "attributeName": "title",
                "searchValue": "",
                "phraseQuery": false,
                "type": "AND"
            }
        ];
        setAdvanceSearchValues(updatedAdvanceSearchValues);
        props.clearState();
    }
    return (
        <Grid container align="center" className={classes.grid}>
            <Grid item md={1}></Grid>
            <Grid item md={10}>
                <Paper className={classes.advancedSearch}>

                    <Grid container align="center" className={classes.grid}>
                        <Grid item md={4} className={classes.headerValues}>
                        </Grid>
                        <Grid item md={3}>
                            <Typography variant="h5">Advanced search</Typography>
                        </Grid>
                        <Grid item md={2} style={{ paddingTop: 2 }}>
                            <AddCircleIcon style={{
                                transform: "scale(1.3)",
                                cursor: 'pointer'
                            }} color="primary" onClick={addSearchItem} />
                             <ClearIcon style={{
                                transform: "scale(1.4)",
                                cursor: 'pointer',
                                marginLeft: 15,
                                color: 'red'
                            }}  onClick={clearSearch} />
                        </Grid>
                        <Grid item md={3}>

                        </Grid>
                    </Grid>
                    <div className={classes.header}>
                        <hr />
                        <Grid container align="center" className={classes.grid}>
                            <Grid item md={2} className={classes.headerValues}>
                                <Typography variant="h6">Operator</Typography>
                            </Grid>
                            <Grid item md={6}>
                                <Typography variant="h6" className={classes.headerValues}>Content</Typography>
                            </Grid>
                            <Grid item md={2}>
                                <Typography variant="h6" className={classes.headerValues}>Type</Typography>
                            </Grid>
                            <Grid item md={1}>
                                <Typography variant="h6" className={classes.headerValues}>Phrase</Typography>
                            </Grid>
                            <Grid item md={1}>

                            </Grid>
                        </Grid>
                        <hr />
                    </div>
                    {gridItems}

                    <div className={classes.button}>
                        <Button type="submit" color="primary" variant="contained"
                            startIcon={<SearchIcon />} className={classes.submit} onClick={checkSearchValues}>Search</Button>
                    </div>
                </Paper>

            </Grid>
            <Grid item md={1}></Grid>
        </Grid>
    );
};


const mapDispatchToProps = dispatch => {
    return {
        clearState: () => dispatch(actions.clearState()),
    }
};

export default connect(null, mapDispatchToProps)(AdvancedSearch)