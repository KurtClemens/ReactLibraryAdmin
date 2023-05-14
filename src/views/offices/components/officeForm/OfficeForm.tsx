import React, { useEffect, useReducer, useState } from 'react'
import { AddOfficeRequest, AddUserRequest, OfficeControllerApi, OfficeDao, OfficeDto, UpdateOfficeRequest, UpdateUserRequest, UserDao, UserDto } from '../../../../shared/restApiClient';
import { yupResolver } from '@hookform/resolvers/yup';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import * as Yup from 'yup';
import styles from './OfficeForm.module.css'
import { Grid, FormControl, InputLabel, OutlinedInput, FormHelperText, Button, Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions } from '@mui/material';

type OfficeProps =OfficeDto;


type OfficeSubmitForm = {
    name: string;
    postalCode: string;
    city: string;
    street: string;
    number: string;
};

type OfficeFormProps = {
    readOnly: boolean,
    title: string,
    buttonText: string,
    cancelButtonText: string,
    selectedOffice: OfficeDto,
}

const OfficeForm = ({ readOnly, title, buttonText, cancelButtonText, selectedOffice }: OfficeFormProps) => {
 
    const navigate = useNavigate();
    const officeController = new OfficeControllerApi();
    const [added, forceUpdate] = useReducer((x: number) => x + 1, 0);

    const [open, setOpen] = useState(false)
    const [dialogTitle, setDialogTitle] = useState('')
    const [dialogMessage, setDialogMessage] = useState('')

    const [defaultOffice, setDefaultOffice] = useState<OfficeDto>(Object);

    const validationSchema = Yup.object().shape({
        name: Yup.string()
            .required('Required field')
            .min(2, 'Name must be at least 2 characters'),
        city: Yup.string()
            .required('Required field')
            .min(2, 'First name must be at least 2 characters'),
        postalCode: Yup.string()
            .required('Required field'),
        street: Yup.string()
            .required('Required field'),
        number: Yup.number()
            .required('Required field'),
    });

    const {
        register,
        handleSubmit,
        formState: { errors }
    } = useForm<OfficeSubmitForm>({
        values: selectedOffice,
        resolver: yupResolver(validationSchema),
    });


    const onSubmit = (data: OfficeSubmitForm) => {
        let office: OfficeDao = {
            id:0,
            name: data.name,
            postalCode: data.postalCode,
            city: data.city,
            street: data.street,
            number: data.number
        }

        console.log(data)
        console.log(office)

        const officeToAdd: AddOfficeRequest = {
            officeDao: office
        }
        const officeToUpdate: UpdateOfficeRequest = {
            officeId: selectedOffice.id,
            officeDao: office
        }

        if (selectedOffice.id === 0) {
            officeController.addOffice(officeToAdd)
            navigate('/offices')
        } else {
            officeController.updateOffice(officeToUpdate)
            navigate('/offices')
            forceUpdate()
        }
    };

    const cancel = () => {
        navigate('/offices')
    }


    const [offices, setOffices] = useState<OfficeProps[]>([]);

    useEffect(() => {
        const api = async () => {
            if (!readOnly) {
                const officeData = await officeController.getAllOffices();

                setOffices(officeData.sort((a, b) => {
                    return a.name > b.name ? 1 : -1;
                }));

            }
        };
        api();
    }, [selectedOffice]);

    return (
        selectedOffice ?
            <>
                <div>
                    <div className={styles['screen-title']}>{title}</div>
                    <div className={styles['register-form']}>
                        <form className={styles['book-form']} onSubmit={handleSubmit(onSubmit)}>
                            <Grid container spacing={2} >
                                <Grid container item xs={6} direction="column" >
                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="name"  >Name</InputLabel>
                                        <OutlinedInput
                                            readOnly={readOnly}
                                            label="name"
                                            id="name"
                                            {...register('name', { required: true, min: 2 })}
                                            error={errors.name?.type === 'required'}
                                        />
                                        <FormHelperText error id="component-error-text">{errors.name?.message}</FormHelperText>
                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">City</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="city"
                                            label="city"
                                            {...register('city', { required: true, min: 2 })}
                                            error={errors.city?.type === 'required'}
                                        />
                                        <FormHelperText error id="component-error-text">{errors.city?.message}</FormHelperText>
                                    </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Postal code</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="postalCode"
                                            label="postalCode"
                                            {...register('postalCode', { required: true })}
                                            error={errors.postalCode?.type === 'required'}
                                        />
                                        <FormHelperText error id="component-error-text">{errors.postalCode?.message}</FormHelperText>
                                    </FormControl>

                                        <FormControl className={styles['formcontrol']}>
                                            <InputLabel htmlFor="component-error" >Street</InputLabel>
                                            <OutlinedInput
                                                id="street"
                                                label="street"
                                                {...register('street', { required: true })}
                                            error={errors.street?.type === 'required'}
                                            />
                                            <FormHelperText error id="component-error-text">{errors.street?.message}</FormHelperText>
                                        </FormControl>

                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Number</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="number"
                                            label="number"
                                            {...register('number', { required: true })}
                                            error={errors.number?.type === 'required'}
                                        />
                                        <FormHelperText error id="component-error-text">{errors.number?.message}</FormHelperText>
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
            </> : <div>Loading...</div>  )
}

export default OfficeForm