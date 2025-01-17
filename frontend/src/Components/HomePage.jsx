import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Cookies from "js-cookie";
import axios from "axios";
import MedicineDetails from "./MedicineDetails";

const HomePage = () => {
    console.log(Cookies.get("username"));
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [userType, setUserType] = useState(null);

    // Function to validate cookie and set login state
    const validateSession = async () => {
        const username = Cookies.get("username"); // Retrieve cookie
        const token = localStorage.getItem("jwt_token"); // Retrieve token

        if (username && token) {
            try {
                const username = Cookies.get("username");
                if (username && token) {
                    setIsLoggedIn(true);
                } else {
                    handleLogout();
                }
            } catch (error) {
                console.error("Session validation failed:", error);
                handleLogout();
            }
        } else {
            handleLogout(); // No valid token or username
        }
    };

    useEffect(() => {
        validateSession(); // Check session on component mount
    }, []);

    const handleLogout = () => {
        const userInfo = Cookies.get("username");
        const response = axios.post("http://localhost:8080/user/logout", {
            credentials: 'include',
        },
            userInfo);
        console.log(response);
        console.log(Cookies.get("username"));
        setIsLoggedIn(false);
        setUserType(null);
        Cookies.remove("username");
        localStorage.removeItem("jwt_token"); // Clear token on logout
    };

    return (
        <div className="min-h-screen bg-gray-100">
            {/* Navbar */}
            <nav className="bg-blue-500 text-white shadow-md">
                <div className="container mx-auto px-6 py-4 flex justify-between items-center">
                    <div className="flex items-center">
                        <h1 className="text-lg font-bold">Medkart Pharmacy</h1>
                    </div>
                    <div className="flex space-x-4 items-center">
                        <Link to="/" className="hover:underline">Home</Link>
                        <Link to="/products" className="hover:underline">Products</Link>
                        <Link to="/about" className="hover:underline">About</Link>
                        {isLoggedIn ? (
                            userType === "admin" ? (
                                <Link to="/admin/dashboard" className="hover:underline">Admin Dashboard</Link>
                            ) : (
                                <Link to="/user/profile" className="hover:underline">Profile</Link>
                            )
                        ) : null}
                        {isLoggedIn ? (
                            <button
                                onClick={handleLogout}
                                className="bg-red-500 px-4 py-2 rounded-md hover:bg-red-600">
                                Logout
                            </button>
                        ) : (
                            <>
                                <Link
                                    to="/login"
                                    className="bg-green-500 px-4 py-2 rounded-md hover:bg-green-600">
                                    Login
                                </Link>
                            </>
                        )}
                    </div>
                </div>
            </nav>

            {/* Hero Section */}
            <header className="bg-blue-500 text-white py-20">
                <div className="container mx-auto text-center">
                    <h2 className="text-4xl font-bold mb-4">Welcome to Medkart Pharmacy</h2>
                    <p className="text-lg mb-6">
                        Your one-stop shop for pharmaceutical products and reliable healthcare solutions.
                    </p>
                    <Link
                        to="/products"
                        className="bg-white text-blue-500 px-6 py-3 rounded-md hover:bg-gray-200 transition">
                        Browse Products
                    </Link>
                </div>
            </header>
             <MedicineDetails/>
            {/* Footer */}
            <footer className="bg-gray-800 text-white py-6">
                <div className="container mx-auto text-center">
                    <p>&copy; {new Date().getFullYear()} Medkart Pharmacy. All rights reserved.</p>
                    <div className="flex justify-center space-x-4 mt-4">
                        <Link to="/terms" className="hover:underline">Terms of Service</Link>
                        <Link to="/privacy" className="hover:underline">Privacy Policy</Link>
                    </div>
                </div>
            </footer>
        </div>
    );
};

export default HomePage;
