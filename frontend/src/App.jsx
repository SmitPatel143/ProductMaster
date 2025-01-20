
import SignUp from './signup.jsx'
import Login from './login.jsx'
import './tailwind.css'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Categories from './components/Category.jsx';
import Product from "./Components/Product.jsx";
import ProductDataTable from "./Components/ProductDetails.jsx";
import MedicineDetails from "./Components/MedicineDetails.jsx";
import HomePage from "./Components/HomePage.jsx";
import CartPage from "./Components/CartPage.jsx";
import AddToCartPage from "./Components/CartPage.jsx";

function App() {
    return (
        <>
            <Router>
                <Routes>
                    <Route path="/cart" element={<CartPage />} />
                    <Route path="/" element={<Login />} />
                    <Route path="/homepage" element={<HomePage />} />
                    <Route path="/signup" element={<SignUp />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/categories" element={<Categories />} />
                    <Route path="/products" element={<Product/>}/>
                    <Route path="/ProductDataTable" element={<ProductDataTable />} />
                    <Route path="/home" element={<MedicineDetails />} />
                </Routes>
            </Router>

        </>
    )
}

export default App
