import React, { useState, useEffect } from 'react';
import MediaCard from './Card.jsx';
import Cookies from "js-cookie";
import { getProducts, addProductToCart } from "../Functions/CartService.jsx"; // Add addProductToCart

function MedicineDetails() {
    const username = Cookies.get('username');
    const [cartItems, setCartItems] = useState([]);
    const [totalPrice, setTotalPrice] = useState(0);
    const [productDetails, setProductDetails] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const data = await getProducts();
                setProductDetails(data);
                setLoading(false);
            } catch (error) {
                console.error("Error fetching products:", error);
                setLoading(false);
            }
        };

        fetchProducts();
    }, []);

    const handleAddToCart = async (product) => {
        try {
            await addProductToCart(product);
            alert(`${product.name} has been added to your cart!`);
        } catch (error) {
            console.error('Error adding to cart:', error);
            alert('Failed to add product to the cart.');
        }
    };



    const handleLearnMore = (product) => {
        alert(`Learn more about ${product.name}`);
        // You can add logic to open a product details page or show a modal
    };

    if (loading) {
        return <div>Loading products...</div>;
    }

    return (
        <div style={{
            display: 'flex',
            flexWrap: 'wrap',
            gap: '20px',
            justifyContent: 'center',
            padding: '20px',
            overflowX: 'auto',
        }}>
            {productDetails.length > 0 ? (
                productDetails.map((product) => (
                    <MediaCard
                        key={product.id}
                        image={product.imageURL || "./src/assets/default.jpg"}
                        title={product.name}
                        description={product.description}
                        button1Text={username == null ? "Buy Now" : "Add to Cart"}
                        button2Text="Learn More"
                        onClickAddToCart={() => handleAddToCart(product)}
                        onClickLearnMore={() => handleLearnMore(product)}
                    />
                ))
            ) : (
                <p>No products available.</p>
            )}
        </div>
    );
}

export default MedicineDetails;
