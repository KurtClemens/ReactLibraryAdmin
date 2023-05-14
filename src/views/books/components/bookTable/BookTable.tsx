import { Button, ThemeProvider, createTheme } from "@mui/material";
import { DataGrid, GridColDef } from "@mui/x-data-grid";
import { useState, useEffect, useReducer } from "react";
import { Delete, Edit, Description } from "@mui/icons-material";
import styles from "./BookTable.module.css";
import { BookControllerApi, BookDto, DeleteBookRequest } from "../../../../shared/restApiClient";
import { useNavigate } from "react-router-dom";
import ConfirmDialog from "../../../../components/confirmDialog/ConfirmDialog";

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

type resultProps = BookDto;



const BookTable = () => {
    const [confirmDialog, setConfirmDialog] = useState({ isOpen: false, title: '', subTitle: '', onConfirm: function () { } })

    const navigate = useNavigate();
    const [rows, setResult] = useState<resultProps[]>([]);
    const [deleted, forceUpdate] = useReducer((x: number) => x + 1, 0);
    const bookController = new BookControllerApi();

    useEffect(() => {
        const api = async () => {
            const data = await bookController.getAllBooks();
            setResult(data.sort((a, b) => {
                return a.title > b.title ? 1 : -1;
            }));
        };

        api();
    }, [bookController, deleted]);

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
                            navigate('/books/' + params.row.id)
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
                            navigate('/books/edit/' + params.row.id)
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
                                subTitle: 'Are you sure you want to delete this book? This cannot be undone!',
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
        let bookId: DeleteBookRequest = {
            bookId: rowId
        }
        await bookController.deleteBook(bookId);
        forceUpdate();
    }

    const columns: GridColDef[] = [
        {
            field: "title",
            headerName: "Title",
            width: 500,
            cellClassName: styles["custom-title-cells"],
        },
        {
            field: "authorDtos",
            headerName: "Author",
            width: 400,
            cellClassName: styles["custom-author-cells"],
            valueGetter: (params) => {
                let result: string[] = [];
                for (let index = 0; index < params.row.authorDtos.length; index++) {
                    result.push(
                        params.row.authorDtos[index].firstName +
                        " " +
                        params.row.authorDtos[index].name
                    );
                }
                return result.join(", ");
            },
        },
        {
            field: "subjectDtos",
            headerName: "Subject",
            width: 400,
            cellClassName: styles["custom-subject-cells"],
            valueGetter: (params) => {
                let result: string[] = [];
                for (let index = 0; index < params.row.subjectDtos.length; index++) {
                    result.push(params.row.subjectDtos[index].technologyName);
                }
                return result.join(", ");
            },
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


export default BookTable