import React from 'react';
import classNames from 'classnames';

interface IconProps {
    icon: React.ComponentType<any>;
    className?: string;
    iconProps?: Record<string, any>;
}

const Icon: React.FC<IconProps> = ({icon, className, iconProps = {}}) => {
    return (
        <div className={classNames(className)}>
            {React.createElement(icon, iconProps)}
        </div>
    );
};

export default Icon;