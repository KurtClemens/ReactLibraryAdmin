import { Button } from '@mui/base'
import { TextField } from '@mui/material'
import styles from './AddNewPublisher.module.css'
import { PublisherDao } from '../../../../shared/restApiClient';
import { useState } from 'react';

let newPublisherCheck: boolean = false;

let newPublisher: PublisherDao = {
    id: 0,
    name: ''
}

type PublisherProps = {
    publisher: (arg: PublisherDao) => any
}

const AddNewPublisher = ({ publisher }: PublisherProps) => {
    let [publisherText, setPublisherText] = useState<string>('+ Add new publisher')
    const publisherCheck = () => {
        newPublisherCheck = !newPublisherCheck;
        if (newPublisherCheck) {
            setPublisherText('hide')
        } else {
            setPublisherText('+ Add new publisher')
        }
    }


    const addPublisher = () => {
        newPublisher.name = defaultPublisherName
        newPublisherCheck = false;
        setPublisherText('+ Add new subject');
        setDefaultPublisherName('')
        publisher(newPublisher)
    }

    let [defaultPublisherName, setDefaultPublisherName] = useState<string>('')
    const handleChange = (event: any) => {
        setDefaultPublisherName(event.target.value.toString())
    }

    return (
        <>
            <div className={styles['center-div']}>
                <Button className={styles['card-button']}
                    onClick={publisherCheck}
                >
                    {publisherText}
                </Button>
            </div>
            <div style={{ display: !newPublisherCheck ? 'none' : undefined }}>
                <div className={styles['center-div']}>
                    <TextField className={styles['input-textfield']}
                        type="text"
                        label="Subject name"
                        variant='outlined'
                        value={defaultPublisherName}
                        onChange={handleChange}
                        helperText={!defaultPublisherName ? 'Required field' : ''}
                        error={!defaultPublisherName}
                    />

                </div>
                <div className={styles['center-div']}>
                    <Button className={styles['card-button']} disabled={!defaultPublisherName} onClick={addPublisher}>add</Button>
                </div>
            </div>
        </>

    )
}

export default AddNewPublisher
