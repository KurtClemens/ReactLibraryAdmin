import React, { useEffect, useReducer, useState } from 'react'
import { AddUserRequest, UpdateUserRequest, UserControllerApi, UserDao, UserDto } from '../../../../shared/restApiClient';
import { useNavigate } from 'react-router-dom';
import * as Yup from 'yup';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import styles from './UserForm.module.css'
import { Grid, FormControl, InputLabel, OutlinedInput, FormHelperText, Autocomplete, TextField, Button, Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions } from '@mui/material';

type UserProps = UserDto;


type UserSubmitForm = {
    name: string;
    firstName: string;
    email: string;
    role: string;
    password?: string;
    active: boolean;
};

type UserFormProps = {
    readOnly: boolean,
    title: string,
    buttonText: string,
    cancelButtonText: string,
    selectedUser: UserDto,
}

const UserForm = ({ readOnly, title, buttonText, cancelButtonText, selectedUser }: UserFormProps) => {
    const navigate = useNavigate();
    const userController = new UserControllerApi();
    const [added, forceUpdate] = useReducer((x: number) => x + 1, 0);

    const [open, setOpen] = useState(false)
    const [dialogTitle, setDialogTitle] = useState('')
    const [dialogMessage, setDialogMessage] = useState('')

    const emailRegex = new RegExp('[A-Za-zÀ-ÖØ-öø-ū0-9_.+]+@(inetum-realdolmen.world|realdolmen.com)')

    const validationSchema = Yup.object().shape({
        name: Yup.string()
            .required('Required field')
            .min(2, 'Name must be at least 2 characters'),
        firstName: Yup.string()
            .required('Required field')
            .min(2, 'First name must be at least 2 characters'),
        email: Yup.string()
            .required('Required field')
            .matches(emailRegex, 'Email must be of format @inetum-realdolmen.world or @realdolmen.com'),
        role: Yup.string()
            .required('Required field'),          
        });

    const {
        register,
        handleSubmit,
        formState: { errors }
    } = useForm<UserSubmitForm>({
        values: selectedUser,
        resolver: yupResolver(validationSchema),
    });


    const onSubmit = (data: UserSubmitForm) => {
        console.log('in submit')
        let user: UserDao = {
            name: data.name,
            firstName: data.firstName,
            email: data.email,
            password: data.password!,
            active: data.active,
            role: data.role
        }

        console.log(data)
        console.log(user)

        const userToAdd: AddUserRequest = {
            userDao: user
        }
        const userToUpdate: UpdateUserRequest = {
            userId: selectedUser.id,
            userDao: user
        }

        if (selectedUser.id === 0) {
            userController.addUser(userToAdd)
            navigate('/users')
        } else {
        userController.updateUser(userToUpdate)
            navigate('/users')
            forceUpdate()
        }
    };

    const cancel = () => {
        navigate('/users')
    }


    const [users, setUsers] = useState<UserProps[]>([]);

    useEffect(() => {
        const api = async () => {
            if (!readOnly) {
                const userData = await userController.getAllUsers();

                setUsers(userData.sort((a, b) => {
                    return a.email > b.email ? 1 : -1;
                }));
                
            }
        };
        api();
    }, [selectedUser]);



return(
        selectedUser ?
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
                                        <InputLabel htmlFor="component-error">First name</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="firstName"
                                            label="firstName"
                                            {...register('firstName', { required: true, min: 2 })}
                                        error={errors.firstName?.type === 'required'}
                                        />
                                    <FormHelperText error id="component-error-text">{errors.firstName?.message}</FormHelperText>
                                    </FormControl>

                                   <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Email</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="email"
                                        label="email"
                                        {...register('email', { required: true })}
                                        error={errors.email?.type === 'required'}
                                        />
                                    <FormHelperText error id="component-error-text">{errors.email?.message}</FormHelperText>
                                    </FormControl>

                                 <div hidden={title !== 'Add user'}>
                                    <FormControl className={styles['formcontrol']}>
                                    <InputLabel htmlFor="component-error" >Password</InputLabel>
                                    <OutlinedInput
                                            id="password"
                                        label="password"
                                        {...register('password', { required: true})}
                                            error={errors.password?.type === 'required'}
                                        />
                                    <FormHelperText error id="component-error-text">{errors.password?.message}</FormHelperText>
                                    </FormControl>
                                    </div>
                                    <FormControl className={styles['formcontrol']}>
                                        <InputLabel htmlFor="component-error">Role</InputLabel>
                                        <OutlinedInput readOnly={readOnly}
                                            id="role"
                                        label="role"
                                        {...register('role', { required: true })}
                                        error={errors.role?.type === 'required'}
                                        />
                                    <FormHelperText error id="component-error-text">{errors.role?.message}</FormHelperText>
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

export default UserForm