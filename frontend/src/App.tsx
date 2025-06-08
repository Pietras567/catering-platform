import './App.css'
import {BrowserRouter, Route, Routes} from "react-router-dom";
import HomePage from "./components/sites/HomePage.tsx";
import Layout from "./components/Layout.tsx";
import CreatorSite from "./components/sites/CreatorSite.tsx";
import RegisterPage from './components/sites/RegisterPage.tsx';
import LoginPage from "./components/sites/LoginPage.tsx";

function App() {

    return (
        <>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<Layout/>}>
                        <Route index element={<HomePage/>}/>
                        <Route path="creator" element={<CreatorSite/>}/>
                        <Route path="login" element={<LoginPage/>}/>
                        <Route path="register" element={<RegisterPage/>}/>
                    </Route>
                </Routes>
            </BrowserRouter>
        </>
    )
}

export default App
