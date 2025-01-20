import React, { useState, useEffect } from 'react';

import {
    TableContainer,
    Table,
    TableHead,
    TableBody,
    TableRow,
    TableCell,
    TablePagination,
    TableSortLabel,
    Paper,
    Button,
} from '@mui/material';
import axios from "axios";
import Product from './Product';


function ProductDataTable() {
    const [rows, setRows] = useState([]);
    const [order, setOrder] = useState('asc');
    const [orderBy, setOrderBy] = useState('name');
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [selectedProduct, setSelectedProduct] = useState(null);
    const [isEditing, setIsEditing] = useState(false);

    const columns = [
        { id: 'id', label: 'Id', numeric: true },
        { id: 'wsCode', label: 'Product Code', numeric: false },
        { id: 'name', label: 'Product Name', numeric: false },
        { id: 'description', label: 'Description', numeric: false },
        { id: 'quantity', label: 'Quantity', numeric: true },
        { id: 'categoryName', label: 'Category', numeric: false },
        { id: 'mrp', label: 'MRP', numeric: true },
        { id: 'salesPrice', label: 'Sales Price', numeric: true },
        { id: 'packageSize', label: 'Package Size', numeric: true },
        { id: 'activeStatus', label: 'Active Status', boolean: false },
        { id: 'edit', label: 'Actions', numeric: false }
    ];

    useEffect(() => {
        async function fetchData() {
            try {
                const jwt_token = localStorage.getItem('jwt_token');
                const response = await
                    axios.get('http://localhost:8080/admin/products/getAll',
                        {
                            headers: { Authorization: `Bearer ${jwt_token}`},
                        });
                console.log(response);
                if (response.status === 200 && Array.isArray(response.data.data)) {
                    setRows(response.data.data);
                }
            } catch (error) {
                console.error('Error fetching product data:', error);
            }
        }
        fetchData();
    }, []);

    const handleRequestSort = (event, property) => {
        const isAsc = orderBy === property && order === 'asc';
        setOrder(isAsc ? 'desc' : 'asc');
        setOrderBy(property);
    };

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const sortRows = (rows) => {
        return rows.sort((a, b) => {
            if (orderBy === 'name') {
                return order === 'asc' ? a.name.localeCompare(b.name) : b.name.localeCompare(a.name);
            } else {
                return order === 'asc' ? a[orderBy] - b[orderBy] : b[orderBy] - a[orderBy];
            }
        });
    };

    const sortedRows = sortRows(rows);

    const handleEditClick = (product) => {
        setSelectedProduct({ ...product });
        setIsEditing(true);
    };

    const closeEditForm = () => {
        setIsEditing(false); // Hide the form when user closes it
        setSelectedProduct(null); // Reset the selected product
    };

    return (
        <div className="container mx-auto p-4">
            {/* Table */}
            <div style={{ filter: isEditing ? 'blur(4px)' : 'none' }}>
                <Paper sx={{ width: '100%', overflow: 'hidden' }} className="shadow-lg">
                    <TableContainer>
                        <Table aria-labelledby="tableTitle">
                            <TableHead className="bg-indigo-300 text-black dark:text-black text-sm uppercase tracking-wider">

                            <TableRow>
                                    {columns.map((column) => (
                                        <TableCell
                                            key={column.id}
                                            align={column.numeric ? 'right' : 'left'}
                                            sortDirection={orderBy === column.id ? order : false}
                                            className="px-6 py-3 font-semibold text-white"
                                        >
                                            <TableSortLabel
                                                active={orderBy === column.id}
                                                direction={orderBy === column.id ? order : 'asc'}
                                                onClick={(event) => handleRequestSort(event, column.id)}
                                                className="text-white"
                                            >
                                                {column.label}
                                            </TableSortLabel>
                                        </TableCell>
                                    ))}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {sortedRows
                                    .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                                    .map((row, index) => (
                                        <TableRow hover role="checkbox" key={index} className="border-b hover:bg-gray-100">
                                            {columns.map((column) => (
                                                <TableCell key={column.id} align={column.numeric ? 'right' : 'left'} className="px-6 py-4">
                                                    {column.id === 'activeStatus'
                                                        ? row[column.id] ? 'Active' : 'Inactive'
                                                        : column.id === 'edit'
                                                            ? (
                                                                <Button variant="contained" color="primary" onClick={() => handleEditClick(row)}>
                                                                    Edit
                                                                </Button>
                                                            )
                                                            : row[column.id]}
                                                </TableCell>
                                            ))}
                                        </TableRow>
                                    ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <TablePagination
                        rowsPerPageOptions={[5, 10, 25]}
                        component="div"
                        count={rows.length}
                        rowsPerPage={rowsPerPage}
                        page={page}
                        onPageChange={handleChangePage}
                        onRowsPerPageChange={handleChangeRowsPerPage}
                        className="bg-white"
                    />
                </Paper>
            </div>


            {isEditing && selectedProduct && (
                <div className="fixed inset-0 bg-gray-700 bg-opacity-50 flex justify-center items-center z-50">
                    <div className="bg-white p-6 rounded-lg shadow-lg w-150">
                        <div className="flex justify-between items-center mb-4">
                            <h3 className="text-xl font-semibold">Edit Product</h3>
                            <button
                                onClick={closeEditForm}
                                className="text-red-500 hover:text-red-700 font-bold transition duration-300"
                            >
                                CLOSE
                            </button>
                        </div>
                        <Product
                            product={selectedProduct}
                            closeForm={closeEditForm}
                        />
                    </div>
                </div>
            )}

        </div>
    );
}

export default ProductDataTable;
