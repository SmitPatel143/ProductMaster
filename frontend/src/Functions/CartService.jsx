import axios from 'axios';
import Cookies from "js-cookie";
    const BASE_URL = 'http://localhost:8080';

const jwt_token = localStorage.getItem('jwt_token');

export const getCartDetails = async () => {
    try {
        const response = await axios.get(`${BASE_URL}/user/cart`);
        return response.data;
    } catch (error) {
        console.error('Error fetching cart details:', error);
        throw error;
    }
};

export const updateCartItem = async (itemId, quantity) => {
    try {
        const response = await axios.put(`${BASE_URL}/user/cart/${itemId}`, { quantity });
        console.log(response);
        return response.data;
    } catch (error) {
        console.error('Error updating cart item:', error);
        throw error;
    }
};

export const removeCartItem = async (itemId) => {
    try {
        const response = await axios.delete(`${BASE_URL}/user/cart/${itemId}`);
        return response.data;
    } catch (error) {
        console.error('Error removing cart item:', error);
        throw error;
    }
};

export const getProducts = async () => {
    try{
        console.log(localStorage.getItem('jwt_token'));
        const response = await axios.get("http://localhost:8080/product/getAll" , {
            headers: { Authorization: `Bearer ${jwt_token}`},
        });
        return response.data.data;
    }catch (error) {
        console.error('Error getting products:', error);
        throw error;
    }
}



export const addProductToCart = (product) => {
    const username = Cookies.get('username');
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    cart.push({ ...product, quantity: 1 });

    const cartDetails = {
        productId: product.wsCode,
        quantity: product.quantity,
        totalPrice: product.mrp * product.quantity,
        username: username,
    };
    console.log(product.wsCode, product.quantity, cartDetails.totalPrice, username);
    try{
        const response = axios.post(`${BASE_URL}/product/addToCart`, cartDetails,{
            headers: { Authorization: `Bearer ${jwt_token}`},
        });
        console.log(response);
    }catch (error){
         console.error('Error adding product:', error);
    }
    localStorage.setItem('cart', JSON.stringify(cart));
};

export const getCartItems = () => {
    return JSON.parse(localStorage.getItem('cart')) || [];
};

export const removeFromCart = (itemId) => {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    cart = cart.filter(item => item.id !== itemId);
    localStorage.setItem('cart', JSON.stringify(cart));
};

export const updateCartItemQuantity = (itemId, newQuantity) => {
    let cart = JSON.parse(localStorage.getItem('cart')) || [];
    cart = cart.map(item =>
        item.id === itemId ? { ...item, quantity: newQuantity } : item
    );
    localStorage.setItem('cart', JSON.stringify(cart));
};
