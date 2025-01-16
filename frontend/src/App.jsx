
import SignUp from './signup.jsx'
import Login from './login.jsx'
import './tailwind.css'
import Navbar from "./components/Navbar.jsx";
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Categories from './components/Category.jsx';

function App() {
    return (
        <>
            <Router>
                <Navbar/>
                <Routes>

                    <Route path="/categories" element={<Categories />} />
                    {/*<Route path="/products" element={<Products />} />*/}
                    {/*<Route path="/users" element={<Users />} />*/}
                    {/*<Route path="/orders" element={<Orders />} />*/}
                </Routes>
            </Router>

            {/*<Login/>*/}
        </>
    )
}

export default App
