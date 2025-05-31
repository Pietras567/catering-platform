import classnames from "classnames";

interface ButtonProps {
    classes?: string;
    label: string;
    onClickAction?: () => void;
}

const Button: React.FC<ButtonProps> = ({classes, label, onClickAction}) => {
    return (
        <button className={classnames("rounded-4xl min-w-32", classes)} onClick={onClickAction}>
            <span>{label}</span>
        </button>
    );
}

export default Button;