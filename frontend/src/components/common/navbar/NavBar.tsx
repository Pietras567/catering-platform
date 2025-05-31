import items from "./NavItems.tsx";
import NavItem from "./NavItem.tsx";
import Button from "../buttons/button.tsx";
import {useNavigate} from "react-router";

const NavBar = () => {
    const navigate = useNavigate();

    return (
        <div className="relative w-full h-14 bg-white">
            <div className="inset-y-0 space-x-6 flex flex-row min-h-screen justify-center h-14 absolute w-full">

                {items.map((link) => (
                    <NavItem href={link.href} label={link.label} key={link.href}
                             linkClass={"h-14 justify-center items-center flex flex-row text-gray-700"}/>
                ))}

                <div className="absolute right-6 h-14 justify-center items-center flex flex-row">
                    <Button label={"Zaloguj się"} onClickAction={() => {
                        navigate("/login")
                    }}/>
                    <Button label={"Założ konto"} classes={"bg-green-500 text-white h-8"} onClickAction={() => {
                        navigate("/register")
                    }}/>
                </div>

                <hr className="h-px bg-gray-200 border-0 dark:bg-gray-100 top-14 absolute w-full"/>
            </div>
        </div>
    );
}
export default NavBar;
