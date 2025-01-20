import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';

import email_icon from './assets/email.png';
import password_icon from './assets/password.png';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const navigate = useNavigate();  // Initialize the useNavigate hook

    const handleFormSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setMessage('');
        setIsLoading(true);

        const params = new URLSearchParams();
        params.append("username", username);
        params.append("password", password);

        try {
            const response = await axios.post('http://localhost:8080/user/login', params, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                withCredentials: true,
            });

            if (response.status === 200) {
                setMessage(response.data.message || 'Login successful!');
                setUsername('');
                setPassword('');

                if (response.data && response.data['Authorization']) {
                    const token = response.data['Authorization'].split(' ')[1];
                    localStorage.setItem('jwt_token', token);

                    console.log(localStorage.getItem('username'));
                    console.log(Cookies.get('username'))
                    console.log(response.headers);



                    navigate("/homepage");  // Redirect to /homepage after successful login
                    console.log('JWT Token:', token);
                    console.log(document.cookie);
                } else {
                    console.log('Authentication failed or token not found');
                }
            }
        } catch (error) {
            setUsername('');
            setPassword('');
            if (error.response) {
                const status = error.response.status;

                if (status === 401) {
                    setError('Invalid email or password.');
                } else if (status === 403) {
                    setError('Your account is locked. Please contact support.');
                } else if (status === 409) {
                    setError(error.response.data.message || 'Conflict error.');
                } else {
                    setError('Technical Error, Please try again later.');
                }
            } else if (error.request) {
                setError('No response from the server. Please try again later.');
            } else {
                setError('An error occurred. Please try again.');
            }
        } finally {
            setIsLoading(false);
        }
    };

    const inputClass = "w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 transition duration-300 ease-in-out";

    return (
        <div className="max-w-md mx-auto p-6 bg-white rounded-lg shadow-lg">
            <div className="text-center mb-6">
                <h2 className="text-3xl font-semibold text-gray-800">Welcome Back!</h2>
                <div className="w-24 h-1 bg-blue-500 mx-auto mt-2"></div>
            </div>
            <form onSubmit={handleFormSubmit} className="space-y-6">
                <div className="flex items-center space-x-3">
                    <img src={email_icon} alt="email" className="w-6 h-6"/>
                    <input
                        type="email"
                        placeholder="Enter your email"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        aria-label="Email"
                        required
                        className={inputClass}
                    />
                </div>
                <div className="flex items-center space-x-3">
                    <img src={password_icon} alt="password" className="w-6 h-6"/>
                    <input
                        type="password"
                        placeholder="Enter your password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        className={inputClass}
                    />
                </div>

                {error && <div className="text-red-600 bg-red-100 p-3 rounded-lg text-sm">{error}</div>}
                {message && <div className="text-green-600 bg-green-100 p-3 rounded-lg text-sm">{message}</div>}

                <div className="mt-6">
                    <button
                        type="submit"
                        className="bg-blue-500 text-white px-6 py-3 rounded-lg hover:bg-blue-600 focus:outline-none transition duration-300 w-full"
                        disabled={isLoading}
                    >
                        {isLoading ? 'Logging in...' : 'Login'}
                    </button>
                </div>
            </form>
            <div className="mt-6 text-center">
                <p className="text-sm text-gray-600">Don't have an account? <a href="/signup" className="text-blue-500 hover:text-blue-600">Sign Up</a></p>
            </div>
        </div>
    );
};

export default Login;
