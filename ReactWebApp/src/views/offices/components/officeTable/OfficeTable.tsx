import React, { useEffect, useReducer, useState } from 'react'
import { DeleteOfficeRequest, DeleteUserRequest, OfficeControllerApi, OfficeDto } from '../../../../shared/restApiClient';
import { Description, Edit, Delete } from '@mui/icons-material';
import { createTheme, Button, ThemeProvider } from '@mui/material';
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import { useNavigate } from 'react-router-dom';
import styles from "./OfficeTable.module.css";
import ConfirmDialog from '../../../../components/confirmDialog/ConfirmDialog';


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

type resultProps = OfficeDto;

const OfficeTable = () => {

    const [confirmDialog, setConfirmDialog] = useState({ isOpen: false, title: '', subTitle: '', onConfirm: function () { } })
    const navigate = useNavigate();
    const [rows, setResult] = useState<resultProps[]>([]);
    const [deleted, forceUpdate] = useReducer((x: number) => x + 1, 0);
    const officeController = new OfficeControllerApi();

    useEffect(() => {
        const api = async () => {
            const data = await officeController.getAllOffices();
            setResult(data.sort((a, b) => {
                return a.name > b.name ? 1 : -1;
            }));
        };

        api();
    }, [officeController, deleted]);

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
                            navigate('/offices/' + params.row.id)
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
                            navigate('/offices/edit/' + params.row.id)
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
                                subTitle: 'Are you sure you want to delete this office? This cannot be undone!',
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
        let office: DeleteOfficeRequest = {
            officeId: rowId,
        }
        await officeController.deleteOffice(office);
        forceUpdate();
    }

    const columns: GridColDef[] = [
        {
            field: "name",
            headerName: "Name",
            width: 300,
            cellClassName: styles["custom-name-cells"],
        },
        {
            field: "city",
            headerName: "City",
            width: 200,
            cellClassName: styles["custom-city-cells"],
        },
        {
            field: "postalCode",
            headerName: "Postal code",
            width: 200,
            cellClassName: styles["custom-postalCode-cells"],
        },
        {
            field: "street",
            headerName: "Street",
            width: 450,
            cellClassName: styles["custom-street-cells"]
        },
        {
            field: "number",
            headerName: "Number",
            width: 150,
            cellClassName: styles["custom-number-cells"]
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
  )
}

export default OfficeTable