import { Button } from '@mui/base'
import { TextField } from '@mui/material'
import styles from './AddNewAuthor.module.css'
import { AuthorDao } from '../../../../shared/restApiClient';
import { useState } from 'react';

const today = new Date();
const maxDate = today.getFullYear()+ "-" 
                + ('0'+(today.getMonth()+1)).slice(-2) + "-"
                + ('0' + (today.getDay())).slice(-2)

                let newAuthorCheck: boolean = false;

let newAuthor: AuthorDao = {
    id: 0,
    name: '',
    firstName: '',
    dateOfBirth: undefined,
}

type AuthorProps = {
    author: (arg: AuthorDao) => any
}

const AddNewAuthor = ({ author }: AuthorProps) => {

    let [authorText, setAuthorText] = useState<string>('+ Add new author')
    const authorCheck = () => {
        newAuthorCheck = !newAuthorCheck;
        if (newAuthorCheck) {
            setAuthorText('hide')
        } else {
            setAuthorText('+ Add new author')
        }
    }


    const addAuthor = () => {

        newAuthor.name = defaultName
        newAuthor.firstName = defaultFirstName
        newAuthor.dateOfBirth = new Date(defaultDob!)
        console.log(newAuthor.dateOfBirth)
        newAuthorCheck = false;
        setAuthorText('+ Add new subject');
        setDefaultName('')
        setDefaultFirstName('')
        setDefaultDob(undefined)
        author(newAuthor)
    }

    let [defaultName, setDefaultName] = useState<string>('')
    const handleNameChange = (event: any) => {
        setDefaultName(event.target.value.toString())
    }


    let [defaultFirstName, setDefaultFirstName] = useState<string>('')
    const handleFirstNameChange = (event: any) => {
        setDefaultFirstName(event.target.value.toString())
    }

    let [defaultDob, setDefaultDob] = useState<Date>()
    const handleDobChange = (event: any) => {
        let selectedDate = new Date(event.target.value)
        let minDate = new Date("1900-01-01")
        if (selectedDate < today && selectedDate > minDate){
            setDefaultDob(event.target.value)
        }else{
            setDefaultDob(undefined)
        }
    }

    return (
        <>
            <div className={styles['center-div']}>
                <Button className={styles['card-button']}
                    onClick={authorCheck}
                >
                    {authorText}
                </Button>
            </div>
            <div style={{ display: !newAuthorCheck ? 'none' : undefined }}>
                <div className={styles['center-div']}>
                    <TextField className={styles['input-textfield']}
                        type="text"
                        label="Author name"
                        variant='outlined'
                        value={defaultName}
                        onChange={handleNameChange}
                        helperText={!defaultName ? 'Required field' : ''}
                        error={!defaultName}
                    />
                </div>

                <div className={styles['center-div']}>
                    <TextField className={styles['input-textfield']}
                        type="text"
                        label="Author firstname"
                        variant='outlined'
                        value={defaultFirstName}
                        onChange={handleFirstNameChange}
                        helperText={!defaultFirstName ? 'Required field' : ''}
                        error={!defaultFirstName}
                    />
                </div>

                <div className={styles['center-div']}>
                    <TextField className={styles['input-textfield']}
                        type="date"
                        label="Author date of birth"
                        variant='outlined'
                        value={defaultDob}
                        onChange={handleDobChange}
                        InputLabelProps={{
                            shrink: true,
                        }}
                        InputProps={{ inputProps: { min: "1900-01-01", max: maxDate } }}
                        helperText={!defaultDob ? 'Required field' : ''}
                        error={!defaultDob}
                    />
                </div>

                <div className={styles['center-div']}>
                    <Button className={styles['card-button']} disabled={!defaultName || !defaultFirstName || !defaultDob} onClick={addAuthor}>add</Button>
                </div>
            </div>
        </>

    )
}


export default AddNewAuthor