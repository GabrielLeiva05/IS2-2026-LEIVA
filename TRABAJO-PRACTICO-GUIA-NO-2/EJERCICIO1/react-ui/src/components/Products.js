import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { TableContainer, Paper, Dialog } from '@mui/material';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Box from '@mui/material/Box';
import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import EditIcon from '@mui/icons-material/Edit';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export default function Products() {
    
    const [open, setOpen] = useState(false);
    const [openEdit, setOpenEdit] = useState(false);
    const [products, setProducts] = useState(null);
    const [loadError, setLoadError] = useState(false);
    const [deleteId, setDeleteId] = useState(null);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [newProduct, setNewProduct] = useState({
        name: '',
        description: '',
        brand: '',
        acquisitionDate: '',
        price: ''
    });

    const [editProduct, setEditProduct] = useState({
        id: null,
        name: '',
        description: '',
        brand: '',
        acquisitionDate: '',
        price: ''
    });

    const handleClickOpen = () => {
        setOpen(true);
    }

    const handleConfirmOpen = (productId) => {
        setDeleteId(productId);
        setConfirmOpen(true);
    }

    const handleCloseEdit = () => {
        setOpenEdit(false);
    }

    const handleChangeEdit = (e) => {
        setEditProduct({
            ...editProduct,
            [e.target.name]: e.target.value
        });
    }

    const handleUpdateProduct = async () => {
        try {
            const response = await axios.put(`${API_URL}/products/${editProduct.id}`, {
                ...editProduct,
                acquisitionDate: editProduct.acquisitionDate || null,
                price: editProduct.price === '' ? null : editProduct.price
            });
            setProducts(current => current.map(product => product.id === editProduct.id ? response.data : product));
            handleCloseEdit();
        } catch (error) {
            console.error('Error updating product:', error);
            alert('Error updating product. Please try again later.');
        }
    };

    const handleConfirmClose = (productId) => {
        setDeleteId(null);
        setConfirmOpen(false);
    }

    const handleDelete = async (productId) => {
        try {
            await axios.delete(`${API_URL}/products/${productId}`);
            setProducts(current => current.filter(product => product.id !== productId));
        } catch (error) {
            console.error('Error deleting product:', error);
            alert('Error deleting product. Please try again later.');
        }
    };

    const handleChange = (e) => {
        setNewProduct({
            ...newProduct,
            [e.target.name]: e.target.value
        });
    }

    const handleClose = () => {
        setOpen(false);
    }

    const handleClickOpenEdit = (product) => {
        setEditProduct(product);
        setOpenEdit(true);
    }

    const handleAddProduct = async () => {
        try {
            if (!newProduct.name.trim()) {
                alert('Product name is required.');
                return;
            }
            const response = await axios.post(`${API_URL}/products`, {
                ...newProduct,
                acquisitionDate: newProduct.acquisitionDate || null,
                price: newProduct.price === '' ? null : newProduct.price
            });
            setProducts(current => [...(current || []), response.data]);
            setNewProduct({
                name: '',
                description: '',
                brand: '',
                acquisitionDate: '',
                price: ''
            });
            handleClose();
        } catch (error) {
            console.error('Error adding product:', error);
            alert('Error adding product. Please try again later.');
        }
    };

    useEffect(() => {
        axios.get(`${API_URL}/products`)
            .then(response => {
                setProducts(response.data);
            })
            .catch(error => {
                console.error('Error fetching products:', error);
                setLoadError(true);
            });
    }, []);

  return (
    <Box 
    display="flex"
    justifyContent="center"
    alignItems="center"
    height="100vh"
    >

    <TableContainer component={Paper} style={{width: '80%', margin: 'auto', marginTop: '20px'}}>
        <Box display="flex" justifyContent="space-between" alignItems="center" padding="20px">
        <Button variant="contained" color="primary" style={{ margin: '20px' }} onClick={handleClickOpen}>
            Add Product
        </Button>
        </Box>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>Id</TableCell>
            <TableCell align="right">Name</TableCell>
            <TableCell align="right">Description</TableCell>
            <TableCell align="right">Brand</TableCell>
            <TableCell align="right">Acquisition Date</TableCell>
            <TableCell align="right">Price</TableCell>
            <TableCell align="right">Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
            {products === null && !loadError ? (
                <TableRow><TableCell colSpan={7}>Loading products...</TableCell></TableRow>
            ) : loadError ? (
                <TableRow>
                    <TableCell colSpan={7}>
                        Could not load products. Check that the API and database are running.
                    </TableCell>
                </TableRow>
            ) : products.length === 0 ? (
                <TableRow><TableCell colSpan={7}>No products found.</TableCell></TableRow>
            ) : products.map(product => (
                <TableRow key={product.id}>
                    <TableCell component="th" scope="row">
                        {product.id}
                    </TableCell>
                    <TableCell align="right">{product.name}</TableCell>
                    <TableCell align="right">{product.description}</TableCell>
                    <TableCell align="right">{product.brand}</TableCell>
                    <TableCell align="right">{product.acquisitionDate}</TableCell>
                    <TableCell align="right">{product.price}</TableCell>
                    <TableCell align="right">
                        <IconButton color='secondary' onClick={() => handleConfirmOpen(product.id)}>
                            <DeleteIcon />
                        </IconButton>
                        <IconButton color='secondary' onClick={() => handleClickOpenEdit(product)}>
                            <EditIcon />
                        </IconButton>
                    </TableCell>
                </TableRow>
            ))}
        </TableBody>
      </Table>
    </TableContainer>
    <Dialog open={confirmOpen} onClose={handleConfirmClose}>
        <DialogTitle>Confirm Deletion</DialogTitle>
        <DialogContent>
            <DialogContentText>
                Are you sure you want to delete this product?
            </DialogContentText>
        </DialogContent>
        <DialogActions>
            <Button onClick={handleConfirmClose} color="primary">
                Cancel
            </Button>
            <Button onClick={() => {
                handleDelete(deleteId);
                handleConfirmClose();
            }} color="secondary"
                variant="contained" 
            
            >Delete</Button>
        </DialogActions>
    </Dialog>

    <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Add New Product</DialogTitle>
        <DialogContent>
            <TextField
                margin="dense"
                name="name"
                label="Product Name"
                type="text"
                fullWidth
                value={newProduct.name}
                onChange={handleChange}
            />
            <TextField
                margin="dense"
                name="description"
                label="Description"
                type="text"
                fullWidth
                value={newProduct.description}
                onChange={handleChange}
            />
            <TextField
                margin="dense"
                name="brand"
                label="Brand"
                type="text"
                fullWidth
                value={newProduct.brand}
                onChange={handleChange}
            />
            <TextField
                margin="dense"
                name="acquisitionDate"
                label="Acquisition Date"
                type="date"
                fullWidth
                value={newProduct.acquisitionDate}
                onChange={handleChange}
            />
            <TextField
                margin="dense"
                name="price"
                label="Price"
                type="number"
                fullWidth
                value={newProduct.price}
                onChange={handleChange}
            />
        </DialogContent>
        <DialogActions>
            <Button onClick={handleClose} color="primary">
                Cancel
            </Button>
            <Button onClick={handleAddProduct} color="primary" variant="contained">
                Add Product
            </Button>
        </DialogActions>
    </Dialog>

    <Dialog open={openEdit} onClose={handleCloseEdit}>
        <DialogTitle>Edit Product</DialogTitle>
        <DialogContent>
            <TextField
                margin="dense"
                name="name"
                label="Product Name"
                type="text"
                fullWidth
                value={editProduct.name}
                onChange={handleChangeEdit}
            />
            <TextField
                margin="dense"
                name="description"
                label="Description"
                type="text"
                fullWidth
                value={editProduct.description}
                onChange={handleChangeEdit}
            />
            <TextField
                margin="dense"
                name="brand"
                label="Brand"
                type="text"
                fullWidth
                value={editProduct.brand}
                onChange={handleChangeEdit}
            />
            <TextField
                margin="dense"
                name="acquisitionDate"
                label="Acquisition Date"
                type="date"
                fullWidth
                value={editProduct.acquisitionDate}
                onChange={handleChangeEdit}
            />
            <TextField
                margin="dense"
                name="price"
                label="Price"
                type="number"
                fullWidth
                value={editProduct.price}
                onChange={handleChangeEdit}
            />
        </DialogContent>
        <DialogActions>
            <Button onClick={handleCloseEdit} color="primary">
                Cancel
            </Button>
            <Button onClick={handleUpdateProduct} color="primary" variant="contained">
                Update Product
            </Button>
        </DialogActions>
    </Dialog>

    </Box>
  )
}
