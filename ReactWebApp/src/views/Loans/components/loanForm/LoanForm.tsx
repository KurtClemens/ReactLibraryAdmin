import React, { useEffect, useReducer, useState } from 'react'
import * as Yup from 'yup';
import styles from './LoanForm.module.css'
import { BookRequestControllerApi, BookRequestDto, LoanControllerApi, LoanDao, LoanDto, UpdateLoanRequest } from '../../../../shared/restApiClient';
import { Grid, FormControl, InputLabel, OutlinedInput, Button, Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Box, Container, FormHelperText, MenuItem, Select, Stack, TextField, Typography } from '@mui/material';
import { Controller, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import moment from 'moment';
import { yupResolver } from '@hookform/resolvers/yup';


type LoanProps = LoanDto;


type LoanSubmitForm = {
    userEmail: string;
    dateBorrowed: Date;
    dateToReturn: Date;
    dateReturned?: Date;
    extended: boolean;
    bookTitle: string;
    bookIsbn: string;
    bookOnShelveOffice: string;
};

type LoanFormProps = {
    readOnly: boolean,
    title: string,
    buttonText: string,
    cancelButtonText: string,
    selectedLoan: LoanDto,
}


const LoanForm = ({ readOnly, title, buttonText, cancelButtonText, selectedLoan }: LoanFormProps) => {

    const navigate = useNavigate();
    const loanController = new LoanControllerApi();

    const [open, setOpen] = useState(false)
    const [dialogTitle, setDialogTitle] = useState('')
    const [dialogMessage, setDialogMessage] = useState('')
    const [updated, forceUpdate] = useReducer((x: number) => x + 1, 0);

    const [defaultLoan, setDefaultLoan] = useState<LoanDto>(Object);
    const [defaultDateBorrowed, setDefaultDateBorrowed] = useState(new Date(selectedLoan.dateBorrowed).toISOString().substring(0, 16))
    const [defaultReturnDate, setDefaultReturnDate] = useState(new Date(selectedLoan.dateToReturn).toISOString().substring(0, 16))
    const [defaultDateReturned, setDefaultDateReturned] = useState('dd/mm/yyy --:--')

    const validationSchema = Yup.object().shape({
        userEmail: Yup.string()
            .required('Required field'),
        dateBorrowed: Yup.string()
            .required('Required field'),
        dateToReturn: Yup.string()
            .required('Required field'),
        extended: Yup.boolean()
            .required('Required field'),
        bookTitle: Yup.string()
            .required('Required field'),
        bookIsbn: Yup.string()
            .required('Required field'),
        bookOnShelveOffice: Yup.string()
            .required('Required field'),
    });

    const {
        register,
        handleSubmit,
        formState: { errors }
    } = useForm<LoanSubmitForm>({
        values: selectedLoan,
        resolver: yupResolver(validationSchema),
    });


    const cancel = () => {
        navigate('/loans')
    }


    const [loans, setLoans] = useState<LoanProps[]>([]);

    useEffect(() => {
        const api = async () => {
            if (!readOnly) {
                const lonaData = await loanController.getAllLoans();

                setLoans(lonaData.sort((a, b) => {
                    return a.userEmail > b.userEmail ? 1 : -1;
                }));

            }
            if(selectedLoan.dateReturned !== undefined){
                setDefaultDateReturned(new Date(selectedLoan.dateReturned).toISOString().substring(0, 16))
            }
        };
        api();
    }, [selectedLoan]);


    const onSubmit = (data: LoanSubmitForm) => {
        let dateReturned = undefined
        if (defaultDateReturned !== 'dd/mm/yyy --:--'){
            dateReturned = new Date(defaultDateReturned)
        }
        let loan : LoanDao={
            id: selectedLoan.id,
            extended: data.extended,
            userEmail: data.userEmail,
            bookTitle: data.bookTitle,
            bookIsbn: data.bookIsbn,
            bookOnShelveOffice: data.bookOnShelveOffice,
            bookOnShelveId: selectedLoan.bookOnShelveId,
            dateBorrowed: new Date(defaultDateBorrowed),
            dateToReturn: new Date(defaultReturnDate),
            dateReturned: dateReturned
        }

        console.log(loan)

        let loanToUpdate: UpdateLoanRequest = {
            loanId: selectedLoan.id,
            loanDao: loan
        }

        loanController.updateLoan(loanToUpdate)
        navigate('/loans')
        forceUpdate();
    }

    return (
         selectedLoan ?
            <>
                <div>
                    <div className={styles['screen-title']}>{title}</div>
                    <div className={styles['register-form']}>
                       
                        <form className={styles['book-form']} onSubmit={handleSubmit(onSubmit)}>
                            <Grid container spacing={2} >
                                <Grid container item xs={6} direction="column" >
                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="name"  >User</InputLabel>
                                        <OutlinedInput
                                            readOnly={readOnly}
                                            label="user"
                                            id="user"
                                            {...register('userEmail')}
                                            error={errors.userEmail?.type === 'required'}
                                        />
                                        <FormHelperText error id="component-error-text">{errors.userEmail?.message}</FormHelperText>

                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Date Borrowed</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            type="datetime-local"
                                            id="dateBorrowed"
                                            label="dateBorrowed"
                                            value={defaultDateBorrowed}
                                            {...register('dateBorrowed')}
                                            onChange={(e)=>{
                                                setDefaultDateBorrowed(e.target.value)
                                            }}
                                            error={errors.dateBorrowed?.type === 'required'}
                                        />
                                        <FormHelperText error id="component-error-text">{errors.dateBorrowed?.message}</FormHelperText>

                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Returndate</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            type="datetime-local"
                                            id="dateToReturn"
                                            label="dateToReturn"
                                            value={defaultReturnDate}
                                            {...register('dateToReturn')}
                                            onChange={(e) => {
                                                setDefaultReturnDate(e.target.value)
                                            }}
                                            error={errors.dateToReturn?.type === 'required'}
                                        />
                                        <FormHelperText error id="component-error-text">{errors.dateToReturn?.message}</FormHelperText>

                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error" >Date returned</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            type="datetime-local" 
                                            id="dateReturned"
                                            label="dateReturned"
                                            value={defaultDateReturned}
                                            {...register('dateReturned')}
                                            onChange={(e) => {
                                                console.log(e.target.value)
                                                if(e.target.value === ''){
                                                    setDefaultDateReturned('dd/mm/yyy --:--')
                                                }else{
                                                setDefaultDateReturned(e.target.value)
                                                }
                                            }}                                        
                                        />
                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Extended</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="extended"
                                            label="extended"
                                            {...register('extended')}
                                            error={errors.extended?.type === 'required'}
                                        />
                                        <FormHelperText error id="component-error-text">{errors.extended?.message}</FormHelperText>
                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Book Title</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="bookTitle"
                                            label="bookTitle"
                                            {...register('bookTitle')}
                                            error={errors.bookTitle?.type === 'required'}
                                        />
                                        <FormHelperText error id="component-error-text">{errors.bookTitle?.message}</FormHelperText>

                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Book Isbn</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="bookIsbn"
                                            label="bookIsbn"
                                            {...register('bookIsbn')}
                                            error={errors.bookIsbn?.type === 'required'}
                                        />
                                        <FormHelperText error id="component-error-text">{errors.bookIsbn?.message}</FormHelperText>

                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Office</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="office"
                                            label="office"
                                            {...register('bookOnShelveOffice')}
                                            error={errors.bookOnShelveOffice?.type === 'required'}
                                        />
                                        <FormHelperText error id="component-error-text">{errors.bookOnShelveOffice?.message}</FormHelperText>

                                    </FormControl>

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
            </> : <div>Loading....</div>
    )
}

export default LoanForm