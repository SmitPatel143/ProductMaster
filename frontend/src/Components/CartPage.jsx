import React, { useState, useEffect } from 'react';
import { getCartItems, removeFromCart, updateCartItemQuantity } from "../Functions/CartService.jsx";
import { Container, Card, CardContent, CardActions, Button, Typography, TextField, Grid, IconButton } from '@mui/material';
import { RemoveCircle, AddCircle } from '@mui/icons-material';  // Material UI icons for adding/removing quantities
import axios from 'axios';

function CartPage() {
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [totalPrice, setTotalPrice] = useState(0);

    useEffect(() => {
        const fetchCartItems = async () => {
            try {
                const data = await getCartItems();  // Fetch cart items
                setCartItems(data);
                calculateTotalPrice(data);
                setLoading(false);
            } catch (error) {
                console.error("Error fetching cart items:", error);
                setLoading(false);
            }
        };

        fetchCartItems();
    }, []);

    const calculateTotalPrice = (cartItems) => {
        const total = cartItems.reduce((sum, item) => sum + item.mrp * item.quantity, 0);
        setTotalPrice(total);
    };

    const handleRemoveItem = (itemId) => {
        removeFromCart(itemId);
        setCartItems(cartItems.filter(item => item.id !== itemId));
        calculateTotalPrice(cartItems);
    };

    const handleQuantityChange = (itemId, newQuantity) => {
        updateCartItemQuantity(itemId, newQuantity);
        setCartItems(cartItems.map(item => item.id === itemId ? { ...item, quantity: newQuantity } : item));
        calculateTotalPrice(cartItems);
    };

    const handleAddToCart = async (product) => {
        const cartDetails = {
            productId: product.wsCode,
            quantity: product.quantity,
            totalPrice: product.mrp * product.quantity,
            username: 'user123',
        };

        try {
            const response = await axios.post(`${BASE_URL}/product/addToCart`, cartDetails, {
                headers: { Authorization: `Bearer ${jwt_token}` },
            });
            console.log('Product added to cart:', response.data);

            // Optionally, update local cart state here as well
            setCartItems([...cartItems, response.data]);  // Assuming response contains the added item
            calculateTotalPrice([...cartItems, response.data]);

        } catch (error) {
            console.error('Error adding product:', error);
        }
    };

    if (loading) {
        return <div>Loading cart...</div>;
    }

    return (
        <Container maxWidth="lg" sx={{ paddingTop: '20px' }}>
            <Typography variant="h3" gutterBottom align="center" sx={{ fontWeight: 'bold', color: '#2C3E50' }}>
                Your Shopping Cart
            </Typography>

            {cartItems.length === 0 ? (
                <Typography variant="h5" align="center">Your cart is empty.</Typography>
            ) : (
                <Grid container spacing={3} justifyContent="center">
                    {cartItems.map((item) => (
                        <Grid item xs={12} sm={6} md={4} key={item.id}>
                            <Card sx={{ borderRadius: 2, boxShadow: 3 }}>
                                <CardContent>
                                    <Typography variant="h6" sx={{ fontWeight: 'bold' }}>{item.name}</Typography>
                                    <Typography variant="body1" color="text.secondary">${item.mrp}</Typography>
                                </CardContent>
                                <CardActions sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px' }}>
                                    <div>
                                        <IconButton onClick={() => handleQuantityChange(item.id, item.quantity - 1)} disabled={item.quantity <= 1}>
                                            <RemoveCircle />
                                        </IconButton>
                                        <TextField
                                            value={item.quantity}
                                            onChange={(e) => handleQuantityChange(item.id, parseInt(e.target.value))}
                                            type="number"
                                            inputProps={{ min: 1 }}
                                            sx={{ width: '60px', textAlign: 'center' }}
                                        />
                                        <IconButton onClick={() => handleQuantityChange(item.id, item.quantity + 1)}>
                                            <AddCircle />
                                        </IconButton>
                                    </div>
                                    <Typography variant="body1">${(item.mrp * item.quantity).toFixed(2)}</Typography>
                                    <Button onClick={() => handleRemoveItem(item.id)} color="error" variant="outlined">Remove</Button>
                                </CardActions>
                            </Card>
                        </Grid>
                    ))}
                </Grid>
            )}

            <div sx={{ marginTop: '20px', display: 'flex', justifyContent: 'flex-end' }}>
                <Typography variant="h5" sx={{ fontWeight: 'bold', marginRight: '20px' }}>Total: ${totalPrice.toFixed(2)}</Typography>
                <Button variant="contained" color="primary" size="large">Proceed to Checkout</Button>
            </div>
        </Container>
    );
}

export default CartPage;
