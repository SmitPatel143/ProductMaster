import React, { useState } from 'react';
import axios from 'axios';

import user_icon from './assets/person.png';
import email_icon from './assets/email.png';
import password_icon from './assets/password.png';

const SignUp = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleFormSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setMessage('');
        setIsLoading(true);

        if (password.length < 6) {
            setError('Password must be at least 6 characters long.');
            setIsLoading(false);
            return;
        }

        const payload = { firstName, lastName, email, password };

        try {
            const response = await axios.post('http://localhost:8080/user/register', payload);
            console.log(response);
            if (response.status === 201) {
                setMessage(response.data.message);
                setFirstName('');
                setLastName('');
                setEmail('');
                setPassword('');
            }
        } catch (error) {
            setEmail('');
            setPassword('');
            if (error.response) {
                if (error.response.status === 409) {
                    setError(error.response.data.message);
                } else {
                    setError('Technical Error, Please try again later');
                }
            } else {
                setError('Network error.');
            }
        } finally {
            setIsLoading(false);
        }
    };

    const inputClass = "w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500";

    return (
        <div className="max-w-md mx-auto p-6 bg-white rounded-lg shadow-lg">
            <div className="text-center mb-6">
                <div className="text-2xl font-semibold text-gray-800">Sign Up</div>
                <div className="w-24 h-1 bg-blue-500 mx-auto mt-2"></div>
            </div>
            <form onSubmit={handleFormSubmit} className="space-y-4">
                <div className="flex items-center space-x-3">
                    <img src={user_icon} alt="user" className="w-5 h-5"/>
                    <input
                        type="text"
                        placeholder="First Name"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        aria-label="First Name"
                        required
                        className={inputClass}
                    />
                </div>
                <div className="flex items-center space-x-3">
                    <img src={user_icon} alt="user" className="w-5 h-5"/>
                    <input
                        type="text"
                        placeholder="Last Name"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        aria-label="Last Name"
                        required
                        className={inputClass}
                    />
                </div>
                <div className="flex items-center space-x-3">
                    <img src={email_icon} alt="email" className="w-5 h-5"/>
                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        aria-label="Email"
                        required
                        className={inputClass}
                    />
                </div>
                <div className="flex items-center space-x-3">
                    <img src={password_icon} alt="password" className="w-5 h-5"/>
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        className={inputClass}
                    />
                </div>

                {error && <p className="text-red-500 text-sm">{error}</p>}
                {message && <p className="text-green-500 text-sm">{message}</p>}

                <div className="mt-6">
                    <button
                        type="submit"
                        className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition duration-300 w-full"
                        disabled={isLoading}
                    >
                        {isLoading ? 'Signing Up...' : 'Sign Up'}
                    </button>
                </div>
                <div className="mt-6 text-center">
                    <p className="text-sm text-gray-600">Already have an account? <a href="/login"
                                                                                   className="text-blue-500 hover:text-blue-600">Login
                    </a></p>
                </div>
            </form>
        </div>
    );
};

export default SignUp;
