import React, { useEffect, useState } from 'react'
import { GetOfficeByIdRequest, OfficeControllerApi, OfficeDto } from '../../../../shared/restApiClient';
import OfficeForm from '../../components/officeForm/OfficeForm';
import { useLocation, useParams } from 'react-router-dom';
import styles from './CreateReadUpdateOffice.module.css'

let readOnly;
let title;
let buttonText;
let cancelButtonText;

const CreateReadUpdateOffice = () => {
    const officeController = new OfficeControllerApi();
    const paramsLocation = useLocation();
    const paramsId = useParams();

    let [selectedOffice, setSelectedOffice] = useState<OfficeDto>();

    useEffect(() => {
        const api = async () => {
            if (paramsId.id !== undefined) {
                try {
                    const officeId: GetOfficeByIdRequest = {
                        officeId: +paramsId.id
                    }
                    const data = await officeController.getOfficeById(officeId);
                    setSelectedOffice(data)
                } catch (error) {

                }

            } else {
                let office: OfficeDto = {
                    id: 0,
                    name: '',
                    postalCode: '',
                    city: '',
                    street: '',
                    number: ''
                }
                setSelectedOffice(office)
            }
        }

        api();
    }, []);

    if (paramsLocation.pathname === '/offices/add') {
        readOnly = false;
        title = 'Add office';
        buttonText = 'Add';
        cancelButtonText = 'Cancel'
    } else if (paramsLocation.pathname === '/offices/edit/' + paramsId.id) {
        readOnly = false;
        title = 'Update office';
        buttonText = 'Update';
        cancelButtonText = 'Cancel'
    } else {
        readOnly = true;
        title = 'Office details';
        buttonText = 'Ok';
        cancelButtonText = 'Return'
    }


    return (
        selectedOffice ? <OfficeForm readOnly={readOnly} title={title} buttonText={buttonText} cancelButtonText={cancelButtonText} selectedOffice={selectedOffice}></OfficeForm> : <div className={styles['screen-title']}>Loading ...</div>
    )
}

export default CreateReadUpdateOffice