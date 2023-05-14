import React, { useEffect, useReducer, useState } from 'react'
import { BookRequestControllerApi, BookRequestDao, BookRequestDto, DeleteUserRequest, UpdateBookRequest, UpdateBookRequestRequest, UserControllerApi } from '../../../../shared/restApiClient';
import { ThemeProvider } from '@emotion/react';
import { Description, Edit, Delete, Email } from '@mui/icons-material';
import { createTheme, Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, TextField, RadioGroup, FormControlLabel, Radio, FormControl, FormLabel } from '@mui/material';
import { GridColDef, DataGrid } from '@mui/x-data-grid';
import { useNavigate } from 'react-router-dom';
import ConfirmDialog from '../../../../components/confirmDialog/ConfirmDialog';
import styles from "./RequestTable.module.css";
import moment from 'moment';
import { red } from '@mui/material/colors';

const myTheme = createTheme({
    palette:{
        warning:{
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

type resultProps = BookRequestDto;

let showAll:boolean = false;
let buttonText = 'Show all'

const RequestTable = () => {

    

    const [confirmDialog, setConfirmDialog] = useState({ isOpen: false, title: '', subTitle: '', onConfirm: function () { } })
    const navigate = useNavigate();
    const [rows, setResult] = useState<resultProps[]>([]);
    const [deleted, forceUpdate] = useReducer((x: number) => x + 1, 0);
    const requestController = new BookRequestControllerApi();

    useEffect(() => {
        const api = async () => {
            const data = await requestController.getAllBookRequests();
            if(showAll){
                buttonText = 'Show not processed'
                setResult(data.sort((a, b) => {
                return a.requester > b.requester ? 1 : -1;
            }));
            }else{
                let requests:BookRequestDto[] = [];
                buttonText = 'Show all'
                data.forEach(request => {
                    if(!request.mailSend){
                        requests.push(request)
                    }
                });
                setResult(requests.sort((a, b) => {
                    return a.requester > b.requester ? 1 : -1;
                }))
            }

        };

        api();
    }, [requestController, deleted]);


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
                            navigate('/requests/' + params.row.id)
                        }}
                    />
                </strong>


                <strong className={styles['render-button-strong']}>
                    <Button
                        className={styles["custom-button"]}
                        startIcon={<Email />}
                        variant="contained"
                        size="medium"
                        onClick={()=>handleClickOpen(params.row)}
                    ></Button>
                </strong>
            </>
        );
    };

    const sendMessage = async (rowId: any) => {
        setConfirmDialog({
            ...confirmDialog,
            isOpen: false
        })

        forceUpdate();
    }

    const columns: GridColDef[] = [
        {
            field: "requester",
            headerName: "User",
            width: 200,
            cellClassName: styles["custom-requester-cells"],
        },
        {
            field: "title",
            headerName: "Title",
            width: 400,
            cellClassName: styles["custom-title-cells"],
        },
        {
            field: "author",
            headerName: "Author",
            width: 200,
            cellClassName: styles["custom-author-cells"],
        },
        {
            field: "subject",
            headerName: "Subject",
            width: 200,
            cellClassName: styles["custom-subject-cells"]
        },
        {
            field: "dateRequested",
            headerName: "Requestdate",
            width: 200,
            valueFormatter: params =>
                moment(new Date(params.value)).format('DD/MM/YYYY'),
            cellClassName: styles["custom-requestDate-cells"]
        },
        {
            field: "approved",
            headerName: "Approved",
            width: 100,
            cellClassName: styles["custom-approved-cells"]
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

    function all(){
        showAll = !showAll
    }

    const [open, setOpen] = useState(false);
    const [title, setTitle] = useState('');
    const [selectedRequest, setSelectedRequest] = useState<BookRequestDto>(Object);
    const [reason, setReason]=useState('');
    const [approveRequest, setApproveRequest]=useState(false);
    const [purchaseRequest, setPurchaseRequest]=useState(false);

    function handleClickOpen(request:any) {
        setSelectedRequest(request)
        setTitle(request.title)
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };
    const sendMail = () => {
        console.log(reason)
        let bookRequestDao: BookRequestDao = {
            title: selectedRequest.title,
            publisher: selectedRequest.publisher,
            author: selectedRequest.author,
            subject: selectedRequest.subject,
            reason: reason,
            approved: approveRequest,
            purchased: purchaseRequest,
        };

        console.log(selectedRequest.id)
        const bookRequestToUpdate: UpdateBookRequestRequest ={
            bookRequestId: selectedRequest.id,
            bookRequestDao: bookRequestDao
        }
        requestController
            .updateBookRequest(bookRequestToUpdate)

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
                <DialogTitle>{title}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Do you approve this request?
                    </DialogContentText>
                    <TextField
                        multiline
                        rows={4}
                        autoFocus
                        margin="dense"
                        id="outlined-multiline-static"
                        label="Reason"
                        fullWidth
                        onChange={(e:any)=>{
                            setReason(e.target.value)
                        }}
                    />
                    <FormControl>
                        <FormLabel id="demo-row-radio-buttons-group-label">Approve request:</FormLabel>
                    <RadioGroup>
                        <FormControlLabel value="true" control={<Radio onChange={(e:any)=>{
                            setApproveRequest(e.target.value)
                            console.log(e.target.value)
                        }} />} label="Approve" />
                            <FormControlLabel value="false" control={<Radio onChange={(e: any) => {
                                setApproveRequest(e.target.value) 
                                console.log(e.target.value) 
                        }}/>} label="Decline" />
                    </RadioGroup>
                    </FormControl>
                    <div >
                    <FormControl>
                        <FormLabel id="demo-row-radio-buttons-group-label">Book purchased?</FormLabel>
                        <RadioGroup>
                            <FormControlLabel value="true" control={<Radio onChange={(e: any) => {
                                setPurchaseRequest(e.target.value)
                            }} />} label="Yes" />
                            <FormControlLabel value="false" control={<Radio onChange={(e: any) => {
                                setPurchaseRequest(e.target.value)
                            }} />} label="No" />
                        </RadioGroup>
                    </FormControl>
                    </div>
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

export default RequestTable

