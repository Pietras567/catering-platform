import classNames from "classnames";
import * as React from "react";

interface NavItemProps {
    href: string;
    label: string;
    linkClass?: string;
}

const NavItem: React.FC<NavItemProps> = ({href, label, linkClass}) => {
    return (
        <a href={href} className={classNames("hover:text-gray-700", linkClass)}>
            {label}
        </a>
    );
}

export default NavItem;