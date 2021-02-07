import React, {  useState } from 'react';
import * as actions from '../MerchantBooksActions';
import { Avatar, Button, CssBaseline, Typography, Container, Grid } from '@material-ui/core';
import { useStyles } from './CreateBookStyles';
import { connect } from 'react-redux';
import { useHistory } from 'react-router';
import { toastr } from 'react-redux-toastr';
import MenuBookIcon from '@material-ui/icons/MenuBook';
import Form from '../../../UI/Form/Form';
import PublishIcon from '@material-ui/icons/Publish';

const CreateBook = (props) => {
    const history = useHistory();

    const classes = useStyles();
    var form = null;
    const [formIsValid, setFormIsValid] = useState(false);

    const [controls, setControls] = useState({
        title: {
            elementType: 'input',
            elementConfig: {
                label: 'Title'
            },
            value: '',
            validation: {
                required: true,
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: '',
            }
        },
        writersNames: {
            elementType: 'input',
            elementConfig: {
                label: 'Writers names'
            },
            value: '',
            validation: {
                required: true,
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: '',
            }
        },
        synopsis: {
            elementType: 'textarea',
            elementConfig: {
                label: 'Synopsis',
                rows: 4,
            },
            value: '',
            validation: {
                required: true,
            },
            error: false,
            errorMessage: '',
        },
        genre: {
            elementType: 'selectOne',
            elementConfig: {
                label: 'Genre',
                options: props.genres,
            },
            value: "",
            validation: {
                requiredSelect: true,
            },
            error: false,
            errorMessage: '',
            additionalData: {
                multipleSelect: false
            }
        },
        isbn: {
            elementType: 'input',
            elementConfig: {
                label: 'ISBN'
            },
            value: '',
            validation: {
                required: true,
                isISBN: true
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: '',
            }
        },
        publisher: {
            elementType: 'input',
            elementConfig: {
                label: 'Publisher'
            },
            value: '',
            validation: {
                required: true,
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: '',
            }
        },
        publisherCity: {
            elementType: 'input',
            elementConfig: {
                label: 'Publisher city'
            },
            value: '',
            validation: {
                required: true,
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: '',
            }
        },
        publicationDate: {
            elementType: 'date',
            elementConfig: {
                label: 'Publication Date'
            },
            validation: {
                required: true,
            },
            value: null,
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: '',
            }
        },
        numberOfPages: {
            elementType: 'input',
            elementConfig: {
                label: 'Number of pages',
            },
            value: '',
            validation: {
                required: true,
                isNumeric: true
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: 'Number of pages is not valid.',
            }
        },
        price: {
            elementType: 'input',
            elementConfig: {
                label: 'Price',
            },
            value: '',
            validation: {
                required: true,
                isPrice:true
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: 'Price is not valid.',
            }
        },
        discount: {
            elementType: 'input',
            elementConfig: {
                label: 'Discount',
            },
            value: '',
            validation: {
                required: true,
                pattern: '^(100|[0-9]{1,2}){1}$'
            },
            valid: false,
            touched: false,
            error: false,
            errorMessage: '',
            additionalData: {
                errorMessage: 'Discount is not valid.',
            }
        },

    })


    if (controls) {
        form = <Form controls={controls} setControls={setControls} setFormIsValid={setFormIsValid} />;
    }
    
    const [image, setImage] = useState(null);
    const [pdfFile, setPdfFile] = useState(null);

    const submitHander = (event) => {
        event.preventDefault();
        if (!image) {
            toastr.warning('Create book', "Please upload book's image.");
            return;
        }

        if (!pdfFile) {
            toastr.warning('Create book', "Please upload book's pdf.");
            return;
        }

        const createBookDTO = {
            writersNames: controls.writersNames.value,
            title: controls.title.value,
            synopsis: controls.synopsis.value,
            genre: controls.genre.value,
            isbn: controls.isbn.value,
            numberOfPages: controls.numberOfPages.value,
            publisherCity: controls.publisherCity.value,
            publicationDate: controls.publicationDate.value,
            publisher: controls.publisher.value,
            price: controls.price.value,
            discount: controls.discount.value,
        };

        const formData = new FormData();
        formData.append('image', image);
        formData.append('pdfFile', pdfFile);
        console.log(createBookDTO)
        formData.append('createBookDTO', new Blob([JSON.stringify(createBookDTO)], { type: "application/json" }));

        props.onAddBook(formData, history);
    }

    const handleChooseImage = ({ target }) => {
        setImage(target.files[0]);
    }

    const handleChoosePDFFile = ({ target }) => {
        setPdfFile(target.files[0]);
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <MenuBookIcon />
                </Avatar>
                <Typography component="h1" variant="h4" className={classes.title}>Create book</Typography>
                <form className={classes.form} noValidate onSubmit={submitHander}>
                    {form}
                    <Grid item xs={12} className={classes.upload}>
                        <input type="file" accept="image/jpeg, image/png" hidden id="upload-image"
                            onChange={handleChooseImage} />
                        <label htmlFor="upload-image">
                            <Grid container>
                                <Grid item xs={5} >
                                    <Button color="primary" variant="contained" component="span"
                                        startIcon={<PublishIcon />}>
                                        Upload image
                                    </Button>
                                </Grid>
                                <Grid item xs={7} className={classes.fileNameGrid}>
                                    <Typography variant="body2" component="span" className={classes.fileName}>
                                        {image ? image.name : ''}
                                    </Typography>
                                </Grid>
                            </Grid>
                        </label>
                    </Grid>
                    <Grid item xs={12} className={classes.upload}>
                        <input type="file" accept="application/pdf" hidden id="upload-pdf-file"
                                onChange={handleChoosePDFFile}
                            />
                            <label htmlFor="upload-pdf-file">
                                <Grid container >
                                    <Grid item xs={5} >
                                        <Button color="primary" variant="contained" component="span">
                                            Upload book PDF
                                        </Button>
                                    </Grid>
                                    <Grid item xs={7} className={classes.fileNameGrid}>
                                        <Typography variant="body2" component="span" className={classes.fileName}>
                                            {pdfFile ? pdfFile.name : ''}
                                        </Typography>
                                    </Grid>
                                </Grid>
                            </label>
                    </Grid>
                    <Button type="submit" color="primary" className={classes.submit}
                        variant="contained" disabled={!formIsValid} fullWidth>
                        Confirm
                    </Button>
                </form>
            </div>
        </Container>
    );

};

const mapStateToProps = state => {
    return {
        genres: state.merchantBookList.genres
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onAddBook: (data, history) => dispatch(actions.addBook(data, history))
    }
};
export default connect(mapStateToProps, mapDispatchToProps)(CreateBook);