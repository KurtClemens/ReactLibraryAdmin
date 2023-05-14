import { Button } from '@mui/base'
import { TextField } from '@mui/material'
import styles from './AddNewSubject.module.css'
import { SubjectDao } from '../../../../shared/restApiClient';
import { useState } from 'react';

let newSubjectCheck: boolean = false;

let newSubject: SubjectDao = {
    id: 0,
    technologyName: ''
}

type SubjectProps={
    subject:(arg:SubjectDao)=>any
}



const AddNewSubject = ({subject}:SubjectProps) => {

    let [subjectText, setSubjectText] = useState<string>('+ Add new subject')
    const subjectCheck = () => {
        newSubjectCheck = !newSubjectCheck;
        if (newSubjectCheck) {
            setSubjectText('hide')
        } else {
            setSubjectText('+ Add new subject')
        }
    }


    const addSubject = () => {
        newSubject.technologyName = defaultSubjectName
        newSubjectCheck = false;
        setSubjectText('+ Add new subject');
        setDefaultSubjectName('')
        subject(newSubject)
    }

    let [defaultSubjectName, setDefaultSubjectName] = useState<string>('')
    const handleChange = (event: any) => {
        setDefaultSubjectName(event.target.value.toString())
    }

    return (
        <>
            <div className={styles['center-div']}>
            <Button className={styles['card-button']}
                onClick={subjectCheck}
            >
                {subjectText}
            </Button>
            </div>
            <div style={{ display: !newSubjectCheck ? 'none' : undefined }}>
            <div  className={styles['center-div']}>
                    <TextField className={styles['input-textfield']}
                    type="text"
                    label="Subject name"
                    variant='outlined'
                    value={defaultSubjectName}
                    onChange={handleChange}
                    helperText={!defaultSubjectName ? 'Required field' : ''}
                    error={!defaultSubjectName}
                />
 
            </div>
            <div className={styles['center-div']}>
                    <Button className={styles['card-button']} disabled={!defaultSubjectName} onClick={addSubject}>add</Button>
                </div>
            </div>
        </>

    )
}

export default AddNewSubject