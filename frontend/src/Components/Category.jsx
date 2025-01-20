import React, {useState} from "react";
import axios from 'axios';

function Category() {
    const [categoryName, setCategoryName] = useState("");
    const [categoryDescription, setCategoryDescription] = useState("");
    const [categoryActiveStatus, setCategoryActiveStatus] = useState(false);
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');

    const handleFormSubmit = async (e) => {
        e.preventDefault();
        try {
            const jwt_token = localStorage.getItem("jwt_token");

            const response = await axios.post('http://localhost:8080/admin/category/save', {
                    name: categoryName,
                    description: categoryDescription,
                    active: categoryActiveStatus
                }, {
                    headers: {
                        Authorization: 'Bearer ' + jwt_token
                    }
                }
            );
            if (response.status === 201)
                setMessage(response.data.message || 'Category Details Saved Successfully!');

        } catch (error) {
            setCategoryName("");
            setCategoryDescription("");
            setMessage("");

            if (error.response) {
                const status = error.response.status;
                if (status === 401 || status === 403) {
                    localStorage.removeItem("jwt_token");
                    setError("Your session has expired");
                    window.location.href = "/login";
                } else if (status === 409)
                    setError("Category already exists, with this name");
                else if (status === 400)
                    setError("Invalid category details");
                else
                    setError("Failed to save category details, due to server error");
            } else {
                setError("Network error or server not reachable");
            }
        }
    }

    return (
        <>
            <div className="min-h-screen flex items-center justify-center">
                <div className="w-full max-w-xs">
                    <form
                        onSubmit={handleFormSubmit}
                        className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4"
                    >
                        <div className="mb-4">
                            <label
                                htmlFor={categoryName}
                                className="block text-gray-700 text-sm font-bold mb-2"
                            >
                                Name:
                            </label>
                            <input
                                id="categoryName"
                                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                                name="categoryName"
                                type="text"
                                required={true}
                                value={categoryName}
                                onChange={(e) => setCategoryName(e.target.value)}
                            />
                        </div>
                        <div className="mb-4">
                            <label
                                htmlFor={categoryDescription}
                                className="block text-gray-700 text-sm font-bold mb-2"
                            >
                                Description:
                            </label>
                            <input
                                id={categoryDescription}
                                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-900 leading-tight focus:outline-none focus:shadow-outline"
                                name="categoryDescription"
                                type="text"
                                required={true}
                                value={categoryDescription}
                                onChange={(e) => setCategoryDescription(e.target.value)}
                            />
                        </div>
                        <div className="flex items-center mb-4">
                            <input
                                id="default-checkbox"
                                type="checkbox"
                                checked={categoryActiveStatus}
                                onChange={(e) => setCategoryActiveStatus(e.target.checked)}
                                className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
                            />
                            <label
                                htmlFor="default-checkbox"
                                className="ms-2 text-sm font-medium text-gray-900 dark:text-gray-700"
                            >
                                Active Status
                            </label>
                        </div>
                        {error && <p className="text-red-500 text-sm">{error}</p>}
                        {message && <p className="text-green-500 text-sm">{message}</p>}
                        <div className="flex justify-center">
                            <button
                                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline center"
                                type="submit"
                            >
                                Submit
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </>
    )
}

export default Category;