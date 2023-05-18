import React from 'react'
import styles from './Login.module.css'
import * as Yup from 'yup';
import { yupResolver } from '@hookform/resolvers/yup';
import { useForm } from 'react-hook-form';
import { Grid, FormControl, InputLabel, OutlinedInput, FormHelperText, Button } from '@mui/material';
import { title } from 'process';
import { LoginControllerApi, LoginRequest, UserDao } from '../../../shared/restApiClient';
import { useNavigate } from 'react-router';
import LoginLayout from '../../layout/LoginLayout';

type LoginSubmitForm = {
    email: string;
    password: string;
};


const Login = () => {
    const navigate = useNavigate();

    const loginController = new LoginControllerApi();
    const emailRegex = new RegExp('[A-Za-zÀ-ÖØ-öø-ū0-9_.+]+@(inetum-realdolmen.world|realdolmen.com)')

    const validationSchema = Yup.object().shape({
        email: Yup.string()
            .required('Required field')
            .matches(emailRegex, 'Email must be of format @inetum-realdolmen.world or @realdolmen.com'),
        password: Yup.string()
            .required('Required field')
    });

    const {
        register,
        handleSubmit,
        formState: { errors }
    } = useForm<LoginSubmitForm>({
        resolver: yupResolver(validationSchema),
    });

    const onSubmit = async (data:any)=>{
        let user:UserDao = {
            name: '',
            firstName: '',
            email: data.email,
            password: data.password,
            role: '',
            active: false
        }
        let loginUser:LoginRequest = {
            userDao: user,
        }

        if(await loginController.login(loginUser)){
            navigate('/home')
        }
        
    }
    
  return (
        <>
        <LoginLayout>
          <div className={styles['screen-title']}>User Login</div>
          <form className={styles['login-form']} onSubmit={handleSubmit(onSubmit)}>
              <Grid container spacing={2} >
                  <Grid container item xs={6} direction="column" >
   

                      <FormControl className={styles['formcontrol']}>
                          <InputLabel htmlFor="component-error">Email</InputLabel>
                          <OutlinedInput
                              id="email"
                              label="email"
                              {...register('email', { required: true })}
                              error={errors.email?.type === 'required'}
                          />
                          <FormHelperText error id="component-error-text">{errors.email?.message}</FormHelperText>
                      </FormControl>

                      <FormControl className={styles['formcontrol']}>
                          <InputLabel htmlFor="component-error" >Password</InputLabel>
                          <OutlinedInput
                              type='password'
                              id="password"
                              label="password"
                              {...register('password', { required: true })}
                              error={errors.password?.type === 'required'}
                          />
                          <FormHelperText error id="component-error-text">{errors.password?.message}</FormHelperText>
                      </FormControl>


                  </Grid>
              </Grid>
              <div className={styles['button-div']}>
                  <Button  className={styles['card-button']} color="primary" variant="outlined" type="submit">
                      Login
                  </Button>
              </div>
              </form>
          </LoginLayout>
          </>
  )
}

export default Login