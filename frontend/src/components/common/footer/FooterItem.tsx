import classNames from "classnames";
import * as React from "react";

interface FooterItemProps {
    href: string;
    label: string;
    linkClass?: string;
}

const FooterItem: React.FC<FooterItemProps> = ({href, label, linkClass}) => {
    return (
        <a
            href={href}
            className={classNames(
                "text-gray-300 hover:text-white transition-colors duration-200 block mb-2",
                linkClass
            )}
        >
            {label}
        </a>
    );
}

export default FooterItem;
