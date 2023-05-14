import React, { useEffect, useState } from 'react'
import { BookRequestControllerApi, BookRequestDto, GetBookRequestByIdRequest } from '../../../../shared/restApiClient';
import { useLocation, useParams } from 'react-router-dom';
import styles from './ReadRequest.module.css'
import RequestForm from '../../components/requestForm/RequestForm';

let readOnly;
let title;
let buttonText;
let cancelButtonText;

const ReadRequest = () => {
    const requestController = new BookRequestControllerApi();
    const paramsLocation = useLocation();
    const paramsId = useParams();

    let [selectedRequest, setSelectedRequest] = useState<BookRequestDto>();

    useEffect(() => {
        const api = async () => {
            if (paramsId.id !== undefined) {
                try {
                    const bookRequestId: GetBookRequestByIdRequest = {
                        bookRequestId: +paramsId.id
                    }
                    const data = await requestController.getBookRequestById(bookRequestId);
                    setSelectedRequest(data)
                } catch (error) {

                }

            } else {
                let request: BookRequestDto = {
                    id: 0,
                    requester: '',
                    title: '',
                    publisher: '',
                    author: '',
                    subject: '',
                    dateRequested: new Date(),
                    approved: false,
                    mailSend: false,
                    purchased: false
                }
                setSelectedRequest(request)
            }
        }

        api();
    }, []);


    readOnly = true;
    title = 'Bookrequest details';
    buttonText = 'Ok';
    cancelButtonText = 'Return'
    


    return (
        selectedRequest ? <RequestForm readOnly={readOnly} title={title} buttonText={buttonText} cancelButtonText={cancelButtonText} selectedRequest={selectedRequest} ></RequestForm> : <div className={styles['screen-title']}>Loading ...</div>
    )
}

export default ReadRequest