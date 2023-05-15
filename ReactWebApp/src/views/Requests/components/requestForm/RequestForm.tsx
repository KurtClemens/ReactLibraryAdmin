import React, { useEffect, useReducer, useState } from 'react'
import { AddUserRequest, BookRequestControllerApi, BookRequestDto, UpdateUserRequest, UserDao } from '../../../../shared/restApiClient';
import { useNavigate } from 'react-router-dom';
import { yupResolver } from '@hookform/resolvers/yup';
import { useForm } from 'react-hook-form';
import * as Yup from 'yup';
import styles from './RequestForm.module.css'
import { Grid, FormControl, InputLabel, OutlinedInput, FormHelperText, Button, Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions } from '@mui/material';


type RequestProps = BookRequestDto;


type RequestSubmitForm = {
    requester: string;
    title: string;
    publisher: string;
    subject: string;
    author: string;
    approved: boolean;
    mailSend: boolean;
    purchased: boolean;
    reason?: string;
    dateRequested: Date;
};

type RequestFormProps = {
    readOnly: boolean,
    title: string,
    buttonText: string,
    cancelButtonText: string,
    selectedRequest: BookRequestDto,
}

const RequestForm = ({ readOnly, title, buttonText, cancelButtonText, selectedRequest }: RequestFormProps) => {

    const navigate = useNavigate();
    const requestController = new BookRequestControllerApi();

    const [open, setOpen] = useState(false)
    const [dialogTitle, setDialogTitle] = useState('')
    const [dialogMessage, setDialogMessage] = useState('')

    const [defaultRequest, setDefaultRequest] = useState<BookRequestDto>(Object);


    const {
        register,
        handleSubmit,
        formState: { errors }
    } = useForm<RequestSubmitForm>({
        values: selectedRequest,
    });


    const cancel = () => {
        navigate('/requests')
    }


    const [requests, setRequests] = useState<RequestProps[]>([]);

    useEffect(() => {
        const api = async () => {
            if (!readOnly) {
                const userData = await requestController.getAllBookRequests();

                setRequests(userData.sort((a, b) => {
                    return a.requester > b.requester ? 1 : -1;
                }));

            }
        };
        api();
    }, [selectedRequest]);

    return (
        selectedRequest ?
            <>
                <div>
                    <div className={styles['screen-title']}>{title}</div>
                    <div className={styles['register-form']}>
                        <form className={styles['book-form']}>
                            <Grid container spacing={2} >
                                <Grid container item xs={6} direction="column" >
                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="name"  >Requester</InputLabel>
                                        <OutlinedInput
                                            readOnly={readOnly}
                                            label="requester"
                                            id="requester"
                                            {...register('requester')}
                                        />
                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Title</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="title"
                                            label="title"
                                            {...register('title')}
                                        />
                                    </FormControl>

                                     <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Publisher</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="publisher"
                                            label="publisher"
                                            {...register('publisher')}
                                        />
                                    </FormControl>

                                        <FormControl className={styles['formcontrol']}>
                                            <InputLabel htmlFor="component-error" >Subject</InputLabel>
                                            <OutlinedInput
                                                id="subject"
                                            label="subject"
                                            {...register('subject')}
                                            />
                                        </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Approved</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="approved"
                                            label="approved"
                                            {...register('approved')}
                                        />
                                    </FormControl> 

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Reason</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="reason"
                                            label="reason"
                                            {...register('reason')}
                                        />
                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Mail send</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="mailSend"
                                            label="mailSend"
                                            {...register('mailSend')}
                                        />
                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Purchased</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="purchased"
                                            label="purchased"
                                            {...register('purchased')}
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
            </> : <div>Loading...</div>
    )
}


export default RequestForm