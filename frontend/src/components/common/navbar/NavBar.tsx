import items from "./NavItems.tsx";
import NavItem from "./NavItem.tsx";

const NavBar = () => {
    return (
        <div className="w-full h-16 bg-white">
            <div className="w-full h-full flex justify-center items-center p-12">
                {items.map((link) => (
                    <NavItem href={link.href} label={link.label} key={link.href}/>
                ))}
            </div>
        </div>
    );
}
export default NavBar;
