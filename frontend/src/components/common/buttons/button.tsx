import classnames from "classnames";

const Button = (classes: string) => {
    return (
        <button className={classnames(classes)}>Button</button>
    );
}

export default Button;