import React, {useState,useEffect} from 'react';
import CategoryDropDown from "./CategoryDropDown.jsx";
import ImageUpload from "./ImageUpload.jsx";
import axiosInstance from "../Services/axiosInstance.jsx";

import axios from "axios";

function Product({ product, closeForm }) {
    const fetchData = async () => {
        try {
            const response = await axiosInstance.get("http://localhost:8080/");
            console.log(response)// Add your actual API endpoint
        } catch (error) {
            console.error("Error fetching data:", error);
        }
    };

    useEffect(() => {
        fetchData();
    }, []);


    const [productName, setProductName] = useState("");
    const [productDescription, setProductDescription] = useState("");
    const [mrp, setMrp] = useState("");
    const [quantity, setQuantity] = useState("");
    const [PackageSize, setPackageSize] = useState("");
    const [salesPrice, setSalesPrice] = useState("");
    const [productActiveStatus, setProductActiveStatus] = useState(false);
    const [Images, setImages] = useState([]);
    const [categoryId, setCategoryId] = useState("");

    useEffect(() => {
        if (product) {

            setProductName(product.name || "");
            setProductDescription(product.description || "");
            setMrp(product.mrp || "");
            setQuantity(product.quantity || "");
            setPackageSize(product.packageSize || "");
            setSalesPrice(product.salesPrice || "");
            setProductActiveStatus(product.activeStatus || false);
            setCategoryId(product.categoryId || categoryId);
            setImages(product.imageURL || []);
        }
    }, [product]);

    const handleCategoryChange = (selectedCategoryId) => {
        setCategoryId(selectedCategoryId);
    };
    const handleImageChange = (newImages) => {
        setImages(newImages)
    }

    useEffect(() => {
    }, [categoryId]);


    const handleFormSubmit = async (e) => {
        e.preventDefault();
        try {
            const jwt_token = localStorage.getItem("jwt_token");
            const imageNames = Images.map(image => image.name);
            const productInfo = {
                name: productName,
                description: productDescription,
                categoryId: categoryId,
                mrp: mrp,
                quantity: quantity,
                packageSize: PackageSize,
                salesPrice: salesPrice,
                activeStatus: productActiveStatus,
                imageURL: imageNames
            };

            const response = product?.id
                ? await axios.post(`http://localhost:8080/admin/products/update`, productInfo, {
                    headers: { Authorization: `Bearer ${jwt_token}`, 'Content-Type': 'application/json' },
                })
                : await axios.post('http://localhost:8080/admin/products/save', productInfo, {
                    headers: { Authorization: `Bearer ${jwt_token}`, 'Content-Type': 'application/json' },
                });

            if(response?.status === 200 || response?.status === 201) {

            }

        }catch (error) {
            console.log("error", error);
            setProductName("");
            setProductDescription("");
            setMrp("");
            setQuantity("");
            setPackageSize("");
            setSalesPrice("");
            setProductActiveStatus(true);
            setImages([]);
        }
    }

    return (
        <>
            <div className="max-w-md mx-auto p-6 bg-white rounded-lg shadow-lg">
                <div>
                    <p className="text-lg font-bold text-gray-700 font-calibri">
                        Product Information
                    </p>
                </div>

                <hr className="border-gray-300"/>
                <form onSubmit={handleFormSubmit} className="space-y-4">
                    <div className="mt-4 margintop-10">
                        <label
                            htmlFor={productName}
                            className="block text-gray-700 text-sm font-bold mb-2"
                        >
                            Name:
                        </label>
                        <input
                            id="productName"
                            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            name="productName"
                            type="text"
                            required={true}
                            value={productName}
                            onChange={(e) => setProductName(e.target.value)}
                        />
                    </div>

                    <div className="mt-4 margintop-10">
                        <label
                            htmlFor={productDescription}
                            className="block text-gray-700 text-sm font-bold mb-2"
                        >
                            Description:
                        </label>
                        <input
                            id="productDescription"
                            className="shadow appearance-none border rounded-sm w-full py-12 px-1 text-sm text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                            name="productName"
                            type="text"
                            required={true}
                            value={productDescription}
                            onChange={(e) => setProductDescription(e.target.value)}
                        />
                    </div>

                    <div className="mt-4 space-y-4">
                        <div className="flex space-x-4">
                            <div className="w-1/3">
                                <label
                                    htmlFor="salesPrice"
                                    className="block text-gray-700 text-sm font-bold mb-2"
                                >
                                    Sales Price:
                                </label>
                                <input
                                    id="salesPrice"
                                    className="shadow appearance-none border rounded-sm w-full py-1 px-2 text-sm text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                    name="salesPrice"
                                    type="number"
                                    required={true}
                                    value={salesPrice}
                                    onChange={(e) => setSalesPrice(e.target.value)}
                                />
                            </div>

                            <div className="w-1/3">
                                <label
                                    htmlFor="mrp"
                                    className="block text-gray-700 text-sm font-bold mb-2"
                                >
                                    MRP:
                                </label>
                                <input
                                    id="mrp"
                                    className="shadow appearance-none border rounded-sm w-full py-1 px-2 text-sm text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                    name="mrp"
                                    type="number"
                                    required={true}
                                    value={mrp}
                                    onChange={(e) => setMrp(e.target.value)}
                                />
                            </div>

                            <div className="w-1/3">
                                <label
                                    htmlFor="quantity"
                                    className="block text-gray-700 text-sm font-bold mb-2"
                                >
                                    Quantity:
                                </label>
                                <input
                                    id="quantity"
                                    className="shadow appearance-none border rounded-sm w-full py-1 px-2 text-sm text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                    name="quantity"
                                    type="number"
                                    required={true}
                                    value={quantity}
                                    onChange={(e) => setQuantity(e.target.value)}
                                />
                            </div>
                        </div>
                    </div>

                    <div className="mt-4 space-y-4">
                        <div className="flex items-center space-x-4">
                            <div className="w-1/2">
                                <label
                                    htmlFor="PackageSize"
                                    className="block text-gray-700 text-sm font-bold mb-2"
                                >
                                    Package Size:
                                </label>
                                <input
                                    id="productDescription"
                                    className="shadow appearance-none border rounded w-full py-2 px-10 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                    name="productName"
                                    type="text"
                                    required={true}
                                    value={PackageSize}
                                    onChange={(e) => setPackageSize(e.target.value)}
                                />
                            </div>

                            <div className="flex items-center mt-6  space-x-2">
                                <input
                                    id="default-checkbox"
                                    type="checkbox"
                                    checked={productActiveStatus}
                                    onChange={(e) => setProductActiveStatus(e.target.checked)}
                                    className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
                                />
                                <label
                                    htmlFor="default-checkbox"
                                    className="ms-2 text-sm font-medium text-gray-900 dark:text-gray-700"
                                >
                                    Active Status
                                </label>
                            </div>
                        </div>
                    </div>

                    <div>
                        <ImageUpload onImageChange={handleImageChange}/>
                    </div>
    
                    <div>
                        <CategoryDropDown onCategoryChange={handleCategoryChange}/>
                    </div>

                    <div className="mt-6">
                        <button
                            type="submit"
                            className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition duration-300 w-full"
                        >
                            Submit
                        </button>
                    </div>
                </form>
            </div>
        </>
    )
}

export default Product