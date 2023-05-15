import React, { useEffect, useState } from 'react'
import * as Yup from 'yup';
import styles from './LoanForm.module.css'
import { BookRequestControllerApi, BookRequestDto, LoanControllerApi, LoanDto } from '../../../../shared/restApiClient';
import { Grid, FormControl, InputLabel, OutlinedInput, Button, Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, Box, Container, FormHelperText, MenuItem, Select, Stack, TextField, Typography } from '@mui/material';
import { Controller, useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import moment from 'moment';


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
   
    console.log(new Date().toISOString().substring(0, 16))

    const navigate = useNavigate();
    const loanController = new LoanControllerApi();

    const [open, setOpen] = useState(false)
    const [dialogTitle, setDialogTitle] = useState('')
    const [dialogMessage, setDialogMessage] = useState('')

    const [defaultLoan, setDefaultLoan] = useState<LoanDto>(Object);
    const [defaultDateBorrowed, setDefaultDateBorrowed] = useState(new Date(selectedLoan.dateBorrowed).toISOString().substring(0, 16))
    const [defaultReturnDate, setDefaultReturnDate] = useState(new Date(selectedLoan.dateToReturn).toISOString().substring(0, 16))
    const [defaultDateReturned, setDefaultDateReturned] = useState('dd/mm/yyy --:--')


    const {
        register,
        handleSubmit,
        formState: { errors }
    } = useForm<LoanSubmitForm>({
        values: selectedLoan,
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
        };
        api();
    }, [selectedLoan]);

    const onSubmit = (data: LoanSubmitForm) => {

        console.log('in submit')
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
                                        />
                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Date Borrowed</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            type="datetime-local"
                                            id="dateBorrowed"
                                            label="dateBorrowed"
                                            value={defaultDateBorrowed}
                                            onChange={(e)=>{
                                                setDefaultDateBorrowed(e.target.value)
                                            }}
                                        />
                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Returndate</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            type="datetime-local"
                                            id="dateToReturn"
                                            label="dateToReturn"
                                            value={defaultReturnDate}
                                            onChange={(e) => {
                                                setDefaultReturnDate(e.target.value)
                                            }}
                                        />
                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error" >Date returned</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            type="datetime-local" 
                                            id="dateReturned"
                                            label="dateReturned"
                                            value={defaultDateReturned}
                                            onChange={(e) => {
                                                console.log(e.target.value)
                                                if(e.target.value === ''){
                                                    setDefaultDateReturned('dd/mm/yyy --:--')
                                                }else{
                                                setDefaultDateReturned(e.target.value)
                                                }
                                            }}                                        />
                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Extended</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="extended"
                                            label="extended"
                                            {...register('extended')}
                                        />
                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Book Title</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="bookTitle"
                                            label="bookTitle"
                                            {...register('bookTitle')}
                                        />
                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Book Isbn</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="bookIsbn"
                                            label="bookIsbn"
                                            {...register('bookIsbn')}
                                        />
                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Office</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="office"
                                            label="office"
                                            {...register('bookOnShelveOffice')}
                                        />
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