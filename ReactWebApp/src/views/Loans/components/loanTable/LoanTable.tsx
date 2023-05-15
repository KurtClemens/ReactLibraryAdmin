import React, { useEffect, useReducer, useState } from 'react'
import { BookRequestControllerApi, BookRequestDao, BookRequestDto, LoanControllerApi, LoanDao, LoanDto, SendEmailToUserRequest, UpdateBookRequestRequest, UpdateLoanRequest } from '../../../../shared/restApiClient';
import styles from "./LoanTable.module.css";
import moment from 'moment';
import { ThemeProvider } from '@emotion/react';
import { Description, Edit, Email } from '@mui/icons-material';
import { createTheme, Button, Dialog, DialogTitle, DialogContent, DialogContentText, TextField, FormControl, FormLabel, RadioGroup, FormControlLabel, Radio, DialogActions } from '@mui/material';
import { GridColDef, DataGrid } from '@mui/x-data-grid';
import { useNavigate } from 'react-router-dom';

const myTheme = createTheme({
    palette: {
        warning: {
            main: "#ff0000"
        }
    },
    components: {
        //@ts-ignore - this isn't in the TS because DataGird is not exported from `@mui/material`
        MuiDataGrid: {
            styleOverrides: {
                row: {
                    "&.Mui-selected": {
                        backgroundColor: "#00AA9A",
                        color: "#232c4b",
                        "&:hover": {
                            backgroundColor: "#00AA9A",
                        },
                    },
                    "&.MuiDataGrid-row:hover": {
                        backgroundColor: "#00AA9A",
                    },
                },
            },
        },
    },
});

type resultProps = LoanDto;

let showAll: boolean = false;
let buttonText = 'Show all'

const LoanTable = () => {
    const [confirmDialog, setConfirmDialog] = useState({ isOpen: false, title: '', subTitle: '', onConfirm: function () { } })
    const navigate = useNavigate();
    const [rows, setResult] = useState<resultProps[]>([]);
    const [deleted, forceUpdate] = useReducer((x: number) => x + 1, 0);
    const loanController = new LoanControllerApi();

    useEffect(() => {
        const api = async () => {
            if (showAll) {
                const data = await loanController.getAllLoans();
                buttonText = 'Show overdue'
                setResult(data.sort((a, b) => {
                    return a.userEmail > b.userEmail ? 1 : -1;
                }));
            } else {
                const data = await loanController.getLoansOverdue();
                buttonText = 'Show all'
                setResult(data.sort((a, b) => {
                    return a.userEmail > b.userEmail ? 1 : -1;
                }))
            }
        };

        api();
    }, [loanController, deleted]);


    const renderButtons = (params: any) => {
        return (
            <>
                <strong className={styles['render-button-strong']}>
                    <Button
                        className={styles["custom-button"]}
                        startIcon={<Description />}
                        variant="contained"
                        size="medium"
                        onClick={() => {
                            navigate('/loans/' + params.row.id)
                        }}
                    />
                </strong>

                <strong className={styles['render-button-strong']}>
                    <Button
                        className={styles["custom-button"]}
                        startIcon={<Edit />}
                        variant="contained"
                        size="medium"
                        onClick={() => {
                            navigate('/loans/edit/' + params.row.id)
                        }}
                    />
                </strong>


                <strong className={styles['render-button-strong']}>
                    <Button
                        className={styles["custom-button"]}
                        startIcon={<Email />}
                        variant="contained"
                        size="medium"
                        onClick={() => handleClickOpen(params.row)}
                    ></Button>
                </strong>
            </>
        );
    };


    const columns: GridColDef[] = [
        {
            field: "userEmail",
            headerName: "User",
            width: 300,
            cellClassName: styles["custom-userEmail-cells"],
        },
        {
            field: "dateBorrowed",
            headerName: "Date borrowed",
            width: 200,
            cellClassName: styles["custom-dateBorrowed-cells"],
            valueFormatter: params =>
                moment(new Date(params.value)).format('DD/MM/YYYY HH:mm'),
        },
        {
            field: "dateToReturn",
            headerName: "Returndate",
            width: 200,
            cellClassName: styles["custom-dateToReturn-cells"],
            valueFormatter: params =>
                moment(new Date(params.value)).format('DD/MM/YYYY HH:mm'),
        },
        {
            field: "bookTitle",
            headerName: "Title",
            width: 400,
            cellClassName: styles["custom-title-cells"]
        },
        {
            field: "bookOnShelveOffice",
            headerName: "Office",
            width: 200,
            cellClassName: styles["custom-office-cells"]
        },
        {
            field: "actions",
            width: 200,
            headerName: "",
            sortable: false,
            cellClassName: styles["custom-actions-cells"],
            disableColumnMenu: true,
            renderCell: renderButtons,
        },
    ];

    function all() {
        showAll = !showAll
    }

    const [open, setOpen] = useState(false);
    const [user, setUser] = useState('');
    const [selectedLoan, setSelectedLoan] = useState<LoanDto>(Object);
    const [message, setMessage] = useState('');

    function handleClickOpen(loan: any) {
        setSelectedLoan(loan)
        setUser(loan.userEmail)
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };
    const sendMail = () => {
        console.log(message)
        console.log(selectedLoan.id)
        
        const email: SendEmailToUserRequest = {
            userEmail: selectedLoan.userEmail,
            message: message
        }

        loanController.sendEmailToUser(email)

        setOpen(false);
        forceUpdate();
    };

    return (
        <ThemeProvider theme={myTheme}>
            <div className={styles['button-top-right-above-card']}>
                <Button className={styles['card-button']} color="primary" variant="outlined"
                    onClick={all}>{buttonText}</Button>
            </div>
            <div className={styles["datagrid-div"]}>
                <DataGrid
                    className={styles["datagrid-table"]}
                    rows={rows}
                    columns={columns}
                    initialState={{
                        pagination: { paginationModel: { pageSize: 5 } },
                    }}
                    pageSizeOptions={[5, 10, 25]}
                />
            </div>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>{user}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Do you want to inquire this person?
                    </DialogContentText>
                    <TextField
                        multiline
                        rows={4}
                        autoFocus
                        margin="dense"
                        id="outlined-multiline-static"
                        label="Message"
                        fullWidth
                        onChange={(e: any) => {
                            setMessage(e.target.value)
                        }}
                    />
                </DialogContent>
                <div className={styles['center-buttons']}>
                    <DialogActions>
                        <Button className={styles['warning-card-button']} onClick={handleClose}>Cancel</Button>
                        <Button className={styles['card-button']} color="primary" onClick={sendMail}>Send</Button>
                    </DialogActions>
                </div>
            </Dialog>
        </ThemeProvider>
    );
};

export default LoanTable