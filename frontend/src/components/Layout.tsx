import NavBar from "./common/navbar/NavBar.tsx";
import * as React from "react";
import {Outlet} from "react-router-dom";
import Footer from "./common/footer/Footer.tsx";

const Layout: React.FC = () => {
    return (
        <>
            <NavBar/>

            <main>
                <Outlet/>
            </main>

            <Footer/>
        </>
    );
}

export default Layout;