import React, { useState, useEffect } from 'react';
import axios from "axios";

function CategoryDropDown({onCategoryChange}) {
    const [categories, setCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState('');
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchCategories = async () => {
            console.log("Fetching categories...");
            try {
                const jwt_token = localStorage.getItem("jwt_token");
                const response = await axios.get('http://localhost:8080/admin/category/getAll',
                    {
                        headers: { Authorization: `Bearer ${jwt_token}`},
                    });
                console.log(response);
                const { status, message, data } = response.data;
                if (status === 200) {
                    setCategories(data);
                } else {
                    setError(message || "Failed to fetch categories.");
                }
            } catch (error) {
                console.log(error);
                const statusCode = error.response ? error.response.status : null;
                if (statusCode === 401) {
                    setError("Unauthorized access.");
                } else if (statusCode === 500) {
                    setError("Failed to fetch categories due to server error.");
                } else {
                    setError("Something went wrong. Please try again.");
                }
            }
        };

        fetchCategories();
    }, []);

    // Handle category selection
    const handleCategoryChange = (e) => {
        console.log(e.target.value);
        setSelectedCategory(e.target.value);
        onCategoryChange(e.target.value);
    };

    return (
        <>
            <div className="mt-4">
                <label
                    htmlFor="category"
                    className="block text-gray-700 text-sm font-bold mb-2"
                >
                    Select Category:
                </label>
                <select
                    id="category"
                    name="category"
                    value={selectedCategory}
                    onChange={handleCategoryChange}
                    required = {true}
                    className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                >
                    <option value="" disabled>Select a category</option>
                    {categories.length > 0 ? (
                        categories.map((category) => (
                            <option key={category.categoryId} value={category.categoryId}>
                                {category.name}
                            </option>
                        ))
                    ) : (
                        <option disabled>No categories available</option>
                    )}
                </select>
            </div>
        </>
    );
}

export default CategoryDropDown;
