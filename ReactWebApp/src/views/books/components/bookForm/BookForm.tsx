import { useEffect, useReducer, useState } from 'react'
import styles from './BookForm.module.css'
import { Autocomplete, Button,  Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, FormControl, FormHelperText, Grid, InputLabel,  OutlinedInput, TextField } from '@mui/material';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as Yup from 'yup';
import {  useNavigate } from 'react-router-dom';
import { AddAuthorRequest, AddBookRequest, AddPublisherRequest, AddSubjectRequest, AuthorControllerApi, AuthorDao, AuthorDto, BookControllerApi, BookDao, BookDto,  OfficeControllerApi,  OfficeDto, PublisherControllerApi, PublisherDao, PublisherDto, SubjectControllerApi, SubjectDao, SubjectDto, UpdateBookRequest } from '../../../../shared/restApiClient';
import AddNewSubject from '../addNewSubject/AddNewSubject';
import AddNewPublisher from '../addNewPublisher/AddNewPublisher';
import AddNewAuthor from '../addNewAuthor/AddNewAuthor';

type OfficeProps = OfficeDto;
type AuthorProps = AuthorDto;
type SubjectProps = SubjectDto;
type PublisherProps = PublisherDto;

type BookSubmitForm = {
    isbn: string;
    title: string;
    language: string;
    pages: number;
    description: string;
    publishedYear: number;
    officeDto: OfficeDto;
    publisherDto: PublisherDto;
    authorDtos: AuthorDto[];
    subjectDtos: SubjectDto[];
};

type BookFormProps = {
    readOnly: boolean,
    title: string,
    buttonText: string,
    cancelButtonText: string,
    selectedBook: BookDto,
}


const BookForm = ({ readOnly, title, buttonText, cancelButtonText, selectedBook }: BookFormProps) => {
    const navigate = useNavigate();
    const bookController = new BookControllerApi();
    const officeController = new OfficeControllerApi();
    const authorController = new AuthorControllerApi();
    const subjectController = new SubjectControllerApi();
    const publisherController = new PublisherControllerApi();
    const [added, forceUpdate] = useReducer((x: number) => x + 1, 0);

    const [open, setOpen] = useState(false)
    const [dialogTitle, setDialogTitle] = useState('')
    const [dialogMessage, setDialogMessage] = useState('')

    const [defaultOffice, setDefaultOffice] = useState<OfficeDto>(Object);
    const [defaultAuthors, setDefaultAuthors] = useState<AuthorDto[]>([]);
    const [defaultPublisher, setDefaultPublisher] = useState<PublisherDto>(Object);
    const [defaultSubjects, setDefaultSubjects] = useState<SubjectDto[]>([]);

    const validationSchema = Yup.object().shape({
        isbn: Yup.string()
            .required('Required field')
            .min(10, 'Isbn must be at least 10 characters')
            .max(17, 'Isbn must not exceed 17 characters'),
        title: Yup.string()
            .required('Required field'),
        language: Yup.string()
            .required('Required field'),
        pages: Yup.number()
            .required('Required field')
            .min(1, 'Pages must be at least 1'),
        description: Yup.string()
            .required('Required field'),
        publishedYear: Yup.number()
            .required('Required field')
            .min(1950, 'Year must be after 1950')
            .max(new Date().getFullYear(), `Published year must be before ${new Date().getFullYear() + 1}`),
    });

    const {
        register,
        handleSubmit,
        formState: { errors }
    } = useForm<BookSubmitForm>({
        values: selectedBook,
        resolver: yupResolver(validationSchema),
    });


    const onSubmit = (data: BookSubmitForm) => {
        let book: BookDao = {
            isbn: data.isbn,
            title: data.title,
            language: data.language,
            pages: data.pages,
            description: data.description,
            publishedYear: data.publishedYear,
            subjectDaos: defaultSubjects,
            publisherDao: defaultPublisher,
            authorDaos: defaultAuthors,
            officeDao: defaultOffice
        }

        console.log(data)
        console.log(book)

        const bookToAdd: AddBookRequest = {
            bookDao: book
        }
        const bookToUpdate: UpdateBookRequest = {
            bookId: selectedBook.id,
            bookDao: book
        }

        if (selectedBook.id === 0) {
            bookController.addBook(bookToAdd)
            navigate('/books')
        } else {
            bookController.updateBook(bookToUpdate)
            navigate('/books')
            forceUpdate()
        }
    };

    const cancel = () => {
        navigate('/books')
    }


    const [offices, setOffices] = useState<OfficeProps[]>([]);
    const [authors, setAuthors] = useState<AuthorProps[]>([]);
    const [subjects, setSubjects] = useState<SubjectProps[]>([]);
    const [publishers, setPublishers] = useState<PublisherProps[]>([]);
    useEffect(() => {
        const api = async () => {
            if (!readOnly) {
                const officeData = await officeController.getAllOffices();
                const authorData = await authorController.getAllAuthors();
                const subjectData = await subjectController.getAllSubjects();
                const publisherData = await publisherController.getAllPublishers();

                setOffices(officeData.sort((a, b) => {
                    return a.name > b.name ? 1 : -1;
                }));
                setAuthors(authorData.sort((a, b) => {
                    return a.name > b.name ? 1 : -1;
                }));
                setSubjects(subjectData.sort((a, b) => {
                    return a.technologyName > b.technologyName ? 1 : -1;
                }));
                setPublishers(publisherData.sort((a, b) => {
                    return a.name > b.name ? 1 : -1;
                }));
            } else {
                let selectedOffice: OfficeDto[] = [];
                selectedOffice.push(selectedBook!.officeDto)
                setOffices(selectedOffice)
                setAuthors(selectedBook.authorDtos.sort((a, b) => {
                    return a.name > b.name ? 1 : -1;
                }));
                setSubjects(selectedBook.subjectDtos.sort((a, b) => {
                    return a.technologyName > b.technologyName ? 1 : -1;
                }));
                let selectedPublisher: PublisherDto[] = [];
                selectedPublisher.push(selectedBook.publisherDto)
                setPublishers(selectedPublisher)
            }
            if (selectedBook !== undefined) {
                setDefaultOffice(selectedBook.officeDto)
                setDefaultAuthors(selectedBook.authorDtos)
                setDefaultPublisher(selectedBook.publisherDto)
                setDefaultSubjects(selectedBook.subjectDtos)
            }
        };
        api();
    }, [selectedBook]);



    const newSubject = async (subject: SubjectDao): Promise<void> => {
        let allSubjects = subjects;
        let selectedSubjects = defaultSubjects;

        let existingSubject = subjects.find(
            (s) =>
                s.technologyName.toLowerCase() ===
                subject.technologyName.toLowerCase()
        );

        if (existingSubject === undefined) {
            const newSubject: AddSubjectRequest = {
                subjectDao: subject
            }
            let createdSubject = await subjectController.addSubject(newSubject);

            allSubjects.push(createdSubject)
            setSubjects(allSubjects)

            selectedSubjects.push(createdSubject)
            setDefaultSubjects(selectedSubjects)
            forceUpdate();
        } else {
            setDialogTitle('Subject already exists!')
            setDialogMessage(`Subject ${existingSubject.technologyName} already exists and is automatically selected!`)
            setOpen(true)

            if (selectedSubjects.findIndex(a => a.technologyName === existingSubject?.technologyName) === -1) {
                selectedSubjects.push(existingSubject)
                forceUpdate();
            }
        }
    }


    const newPublisher = async (publisher: PublisherDao): Promise<void> => {
        let allPublishers = publishers;
        let existingPublisher = publishers.find(
            (p) =>
                p.name.toLowerCase() ===
                publisher.name.toLowerCase()
        );

        if (existingPublisher === undefined) {

            const newPublisher:AddPublisherRequest ={
                publisherDao: publisher
            }
            let createdPublisher = await publisherController.addPublisher(newPublisher);

            allPublishers.push(createdPublisher)
            setPublishers(allPublishers)

            setDefaultPublisher(createdPublisher);
            console.log(defaultPublisher)

        } else if (existingPublisher.id === 0) {
            let index = allPublishers.findIndex(p => p === existingPublisher)
            allPublishers.splice(index, 1)
            allPublishers.push(publisher)
            setPublishers(allPublishers)

            setDefaultPublisher(publisher)
            forceUpdate();

        } else {
            setDialogTitle('Publisher already exists!')
            setDialogMessage(`Publisher ${existingPublisher.name} already exists and is automatically selected!`)
            setOpen(true)

            setDefaultPublisher(existingPublisher)
            forceUpdate();
        }
    }

    const newAuthor = async (author: AuthorDao): Promise<void> => {
        let allAuthors = authors;
        let selectedAuthors = defaultAuthors;

        let existingAuthor = authors.find(
            (a) =>
                a.firstName.toLowerCase() ===
                author.firstName.toLowerCase() &&
                a.name.toLowerCase() ===
                author.name.toLowerCase()
        );

        if (existingAuthor === undefined) {
            const newAuthor: AddAuthorRequest = {
                authorDao: author
            }
            let createdAuthor = await authorController.addAuthor(newAuthor);

            allAuthors.push(createdAuthor)
            setAuthors(allAuthors)

            selectedAuthors.push(createdAuthor)
            setDefaultAuthors(selectedAuthors)
            forceUpdate();
        } else {
            setDialogTitle('Author already exists!')
            setDialogMessage(`Author ${existingAuthor.name} ${existingAuthor.firstName} already exists and is automatically selected!`)
            setOpen(true)

            if(selectedAuthors.findIndex(a=>a === existingAuthor) === -1){
                selectedAuthors.push(existingAuthor)
                forceUpdate();
            }

        }
    }

    const officeProps = {
        options: offices,
        getOptionLabel: (option: OfficeDto) => option.name
    }

    const authorProps = {
        options: authors,
        getOptionLabel: (option: AuthorDto) => option.firstName + ' ' + option.name
    }

    const publisherProps = {
        options: publishers,
        getOptionLabel: (option: PublisherDto) => option.name
    }

    const subjectProps = {
        options: subjects,
        getOptionLabel: (option: SubjectDto) => option.technologyName
    }

    return (
        selectedBook ? 
        <>
        <div>
            <div className={styles['screen-title']}>{title}</div>
            <div className={styles['register-form']}>
                <form className={styles['book-form']} onSubmit={handleSubmit(onSubmit)}>
                    <Grid container spacing={2} >
                        <Grid container item xs={6} direction="column" >
                        <FormControl className={styles['formcontrol']}>
                        <InputLabel htmlFor="name"  >Isbn</InputLabel>
                        <OutlinedInput
                            readOnly={readOnly}
                            label="isbn"
                            id="isbn"
                            {...register('isbn', { required: true, min: 10, max: 17 })}
                            error={errors.isbn?.type === 'required'}
                        />
                        <FormHelperText error id="component-error-text">{errors.isbn?.message}</FormHelperText>
                    </FormControl>

                    <FormControl className={styles['formcontrol']}>
                        <InputLabel htmlFor="component-error">Title</InputLabel>
                        <OutlinedInput readOnly={readOnly}
                            id="validation-outlined-input"
                            label="Title"
                            {...register('title', { required: true })}
                            error={errors.title?.type === 'required'}
                        />
                        <FormHelperText error id="component-error-text">{errors.title?.message}</FormHelperText>
                    </FormControl>

                    <FormControl className={styles['formcontrol']}>
                        <InputLabel htmlFor="component-error">Language</InputLabel>
                        <OutlinedInput readOnly={readOnly}
                            id="validation-outlined-input"
                            label="Language"
                            {...register('language', { required: true })}
                            error={errors.language?.type === 'required'}
                        />
                        <FormHelperText error id="component-error-text">{errors.language?.message}</FormHelperText>
                    </FormControl>

                    <FormControl className={styles['formcontrol']}>
                        <InputLabel htmlFor="component-error">Pages</InputLabel>
                        <OutlinedInput readOnly={readOnly}
                            type='number'
                            id="validation-outlined-input"
                            label="pages"
                            {...register('pages', { required: true, min: 1 })}
                            error={errors.pages?.type === 'min'}
                        />
                        <FormHelperText error id="component-error-text">{errors.pages?.message}</FormHelperText>
                    </FormControl>

                    <FormControl className={styles['formcontrol']}>
                        <InputLabel htmlFor="component-error">Description</InputLabel>
                        <OutlinedInput readOnly={readOnly}
                            multiline
                            id="validation-outlined-input"
                            label="Description"
                            {...register('description', { required: true })}
                            error={errors.description?.type === 'required'}
                        />
                        <FormHelperText error id="component-error-text">{errors.description?.message}</FormHelperText>
                    </FormControl>

                    <FormControl className={styles['formcontrol']}>
                        <InputLabel htmlFor="component-error">Published Year</InputLabel>
                        <OutlinedInput readOnly={readOnly}
                            type='number'
                            id="validation-outlined-input"
                            label="Published Year"
                            {...register('publishedYear', { required: true })}
                            error={errors.publishedYear?.type === "min" || errors.publishedYear?.type === "max"}
                        />
                        <FormHelperText error id="component-error-text">{errors.publishedYear?.message}</FormHelperText>
                    </FormControl>

                    <FormControl className={styles['formcontrol']}>
                        <Autocomplete
                            {...officeProps}
                            readOnly={readOnly}
                            id="autoComplete"
                            disableClearable
                            value={defaultOffice}
                            onChange={(event, value) => {
                                setDefaultOffice(value!);
                            }}
                            renderInput={(params) => (
                                <TextField {...params} label="Office"
                                    helperText={defaultOffice.id === 0 ? 'Required field' : ''}
                                    error={defaultOffice.id === 0} variant="outlined" />
                            )}
                        />
                    </FormControl>
                        </Grid>
                        <Grid container item xs={6} direction="column" >
                    <FormControl className={styles['formcontrol']}>
                        <Autocomplete
                            {...publisherProps}
                            readOnly={readOnly}
                            disableClearable
                            id="tags-outlined"
                            onChange={(event, value) => {
                                setDefaultPublisher(value!);
                            }}
                            value={defaultPublisher}
                            renderInput={(params) => (
                                <TextField
                                    {...params} label="Publisher"
                                    helperText={defaultPublisher?.id === 0 ? 'Required field' : ''}
                                    error={defaultPublisher?.id === 0} variant="outlined" />
                            )}
                        />
                    </FormControl>

                    <div hidden={readOnly}>
                        <AddNewPublisher publisher={newPublisher}></AddNewPublisher>
                    </div>

                    <FormControl className={styles['formcontrol']}>
                        <Autocomplete
                            {...authorProps}
                            readOnly={readOnly}
                            multiple
                            id="tags-outlined"
                            onChange={(event, value) => {
                                setDefaultAuthors(value!);
                            }}
                            value={defaultAuthors}
                            filterSelectedOptions
                            renderInput={(params) => (
                                <TextField
                                    {...params} label="Author"
                                    helperText={defaultAuthors.length === 0 ? 'Required field' : ''}
                                    error={defaultAuthors.length === 0} variant="outlined" />
                            )}
                        />
                    </FormControl>

                    <div hidden={readOnly}>
                        <AddNewAuthor author={newAuthor}></AddNewAuthor>
                    </div>

                    <FormControl className={styles['formcontrol']}>
                        <Autocomplete
                            {...subjectProps}
                            readOnly={readOnly}
                            multiple
                            id="tags-outlined"
                            onChange={(event, value) => {
                                setDefaultSubjects(value!);
                            }}
                            value={defaultSubjects}
                            filterSelectedOptions
                            renderInput={(params) => (
                                <TextField
                                    {...params} label="Subject"
                                    helperText={defaultSubjects.length === 0 ? 'Required field' : ''}
                                    error={defaultSubjects.length === 0} variant="outlined" />
                            )}
                        />
                    </FormControl>

                    <div hidden={readOnly}>
                        <AddNewSubject subject={newSubject}></AddNewSubject>
                    </div>
                        </Grid>
                    </Grid>
                    <div className={styles['button-div']}>
                        <Button style={{ display: readOnly ? 'none' : undefined }} className={styles['card-button']} color="primary" variant="outlined" type="submit">
                            {buttonText}
                        </Button>
                        <Button className={styles['card-button']} color="primary" variant="outlined"
                            type="button"
                            onClick={() => cancel()}
                        >
                            {cancelButtonText}
                        </Button>
                    </div>

                </form>
            </div>

            <Dialog open={open} onClose={() => setOpen(false)} aria-labelledby="dialog-title" aria-describedby="dialog-description">
                <DialogTitle id='dialog-title'>{dialogTitle}</DialogTitle>
                <DialogContent>
                    <DialogContentText id='dialog-description'>{dialogMessage}</DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setOpen(false)}>Ok</Button>
                </DialogActions>
            </Dialog>
                </div>
        </> : <div>Loading...</div>
    );
};

export default BookForm