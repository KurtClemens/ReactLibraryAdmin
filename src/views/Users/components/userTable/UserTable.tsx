import { Button, ThemeProvider, createTheme } from '@mui/material';
import React, { useEffect, useReducer, useState } from 'react'
import { DeleteUserRequest, UserControllerApi, UserDto } from '../../../../shared/restApiClient';
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import ConfirmDialog from '../../../../components/confirmDialog/ConfirmDialog';
import { useNavigate } from 'react-router-dom';
import styles from "./UserTable.module.css";
import { Delete, Description, Edit } from '@mui/icons-material';


const myTheme = createTheme({
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

type resultProps = UserDto;


const UserTable = () => {
    const [confirmDialog, setConfirmDialog] = useState({ isOpen: false, title: '', subTitle: '', onConfirm: function () { } })
    const navigate = useNavigate();
    const [rows, setResult] = useState<resultProps[]>([]);
    const [deleted, forceUpdate] = useReducer((x: number) => x + 1, 0);
    const userController = new UserControllerApi();

    useEffect(() => {
        const api = async () => {
            const data = await userController.getAllUsers();
            setResult(data.sort((a, b) => {
                return a.email > b.email ? 1 : -1;
            }));
        };

        api();
    }, [userController, deleted]);

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
                            navigate('/users/' + params.row.id)
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
                            navigate('/users/edit/' + params.row.id)
                        }}
                    />
                </strong>

                <strong className={styles['render-button-strong']}>
                    <Button
                        className={styles["custom-button"]}
                        startIcon={<Delete />}
                        variant="contained"
                        size="medium"
                        onClick={() => {
                            setConfirmDialog({
                                isOpen: true,
                                title: `${params.row.title}`,
                                subTitle: 'Are you sure you want to delete this user? This cannot be undone!',
                                onConfirm: () => {
                                    onDelete(params.row.id)
                                }
                            })
                        }}
                    ></Button>
                </strong>
            </>
        );
    };

    const onDelete = async (rowId: any) => {
        setConfirmDialog({
            ...confirmDialog,
            isOpen: false
        })
        let user: DeleteUserRequest = {
            userId: rowId,
            booksReturned: false
        }
        await userController.deleteUser(user);
        forceUpdate();
    }

    const columns: GridColDef[] = [
        {
            field: "firstName",
            headerName: "First name",
            width: 250,
            cellClassName: styles["custom-firstName-cells"],
        },
        {
            field: "name",
            headerName: "Name",
            width: 300,
            cellClassName: styles["custom-name-cells"],
        },
        {
            field: "email",
            headerName: "Email",
            width: 600,
            cellClassName: styles["custom-email-cells"],
        },
        {
            field: "active",
            headerName: "Active",
            width: 150,
            cellClassName: styles["custom-active-cells"]
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
    
    return (
        <ThemeProvider theme={myTheme}>
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
            <ConfirmDialog setConfirm={setConfirmDialog} confirm={confirmDialog}></ConfirmDialog>
        </ThemeProvider>
    );
};

export default UserTable