import React, { useState, useEffect } from "react";

function ImageUpload({ onImageChange }) {
    const [files, setFiles] = useState([]);

    useEffect(() => {
        if (files.length > 0) {
            onImageChange(files); // Pass updated files to the parent component (Product)
        }
    }, [files, onImageChange]);

    const handleFileChange = (e) => {
        const newFiles = Array.from(e.target.files);
        const uniqueFiles = newFiles.filter((newFile) => {
            return !files.some((existingFile) => existingFile.name === newFile.name && existingFile.size === newFile.size);
        });

        if (uniqueFiles.length > 0) {
            setFiles((prevFiles) => [...prevFiles, ...uniqueFiles]); // This will trigger the `useEffect`
        }
    };

    return (
        <>
            <div className="mt-4">
                <label
                    htmlFor="images"
                    className="block text-gray-700 text-sm font-bold mb-2"
                >
                    Upload Image:
                </label>
                <input
                    id="images"
                    type="file"
                    accept="image/*"
                    multiple
                    onChange={handleFileChange}
                    className="shadow appearance-none border rounded w-full py-2 px-4 text-sm text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                />
                {files.length > 0 && (
                    <div className="mt-4">
                        <h4 className="text-gray-700">Selected Images:</h4>
                        <div className="grid grid-cols-3 gap-2">
                            {files.map((file, index) => (
                                <div
                                    key={index}
                                    className="w-20 h-20 bg-gray-200 rounded overflow-hidden"
                                >
                                    <img
                                        src={URL.createObjectURL(file)}
                                        alt={`preview-${index}`}
                                        className="w-full h-full object-cover"
                                    />
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </div>
        </>
    );
}

export default ImageUpload;
