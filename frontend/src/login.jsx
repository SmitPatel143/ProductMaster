import React, {useState} from 'react';
import axios from 'axios';

import email_icon from './assets/email.png';
import password_icon from './assets/password.png';

const login = () => {
    const [username, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleFormSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setMessage('');
        setIsLoading(true);

        const params = new URLSearchParams();
        params.append("username", username);
        params.append("password", password);


        try {
            const response = await axios.post('http://localhost:8080/admin/user/login', params, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                }
            });
            if (response.status === 200) {
                setMessage(response.data.message || 'Login successful!');
                setEmail('');
                setPassword('');
                if (response.data && response.data['Authorization']) {
                    const token = response.data['Authorization'].split(' ')[1];
                    localStorage.setItem('jwt_token', token);
                    console.log('JWT Token:', token);
                } else {
                    console.log('Authentication failed or token not found');
                }
            }
        } catch (error) {
            setEmail('');
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

    const inputClass = "w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500";

    return (
        <div className="max-w-md mx-auto p-6 bg-white rounded-lg shadow-lg">
            <div className="text-center mb-6">
                <div className="text-2xl font-semibold text-gray-800">Sign Up</div>
                <div className="w-24 h-1 bg-blue-500 mx-auto mt-2"></div>
            </div>
            <form onSubmit={handleFormSubmit} className="space-y-4">
                <div className="flex items-center space-x-3">
                    <img src={email_icon} alt="email" className="w-5 h-5"/>
                    <input
                        type="email"
                        placeholder="Email"
                        value={username}
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
                        {isLoading ? 'Login...' : 'Login'}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default login;
