import React, { useEffect, useState } from 'react'
import LoanForm from '../../components/loanForm/LoanForm';
import styles from './ReadUpdateLoan.module.css'
import { BookRequestDto, GetBookRequestByIdRequest, GetLoanByIdRequest, LoanControllerApi, LoanDto } from '../../../../shared/restApiClient';
import { useLocation, useParams } from 'react-router-dom';
import Layout from '../../../layout/Layout';


let readOnly;
let title;
let buttonText;
let cancelButtonText;

const ReadUpdateLoan = () => {
    const loanController = new LoanControllerApi();
    const paramsLocation = useLocation();
    const paramsId = useParams();

    let [selectedLoan, setSelectedLoan] = useState<LoanDto>();

    useEffect(() => {
        const api = async () => {
            if (paramsId.id !== undefined) {
                try {
                    const loanId: GetLoanByIdRequest = {
                        loanId: +paramsId.id
                    }
                    const data = await loanController.getLoanById(loanId);
                    setSelectedLoan(data)
                } catch (error) {

                }
            } else {
                let loan: LoanDto = {
                    id: 0,
                    dateBorrowed: new Date(),
                    dateToReturn: new Date(),
                    extended: false,
                    userEmail: '',
                    bookTitle: '',
                    bookIsbn: '',
                    bookOnShelveOffice: '',
                    bookOnShelveId: 0
                }
                setSelectedLoan(loan)
            }
        }

        api();
    }, []);


 if (paramsLocation.pathname === '/loans/edit/' + paramsId.id) {
        readOnly = false;
        title = 'Update loan';
        buttonText = 'Update';
        cancelButtonText = 'Cancel'
    } else {
        readOnly = true;
        title = 'Loan details';
        buttonText = 'Ok';
        cancelButtonText = 'Return'
    }



    return (
        selectedLoan ? <Layout>
            <LoanForm readOnly={readOnly} title={title} buttonText={buttonText} cancelButtonText={cancelButtonText} selectedLoan={selectedLoan} ></LoanForm>     </Layout>
: <div className={styles['screen-title']}>Loading ...</div>
    )
}

export default ReadUpdateLoan