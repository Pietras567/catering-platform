import React from "react";
import {useNavigate} from "react-router-dom";
import type {ObjectSchema} from "yup";
import * as Yup from "yup";
import FormBuilder, {type ButtonConfig, type FieldConfig} from "../common/forms/FormBuilder";

const loginFields: FieldConfig[] = [
    {
        name: "username",
        label: "Login",
        type: "text",
        row: 1,
        col: 1,
        colSpan: 2,
        placeholder: "Twój login",
    },
    {
        name: "password",
        label: "Hasło",
        type: "password",
        row: 2,
        col: 1,
        colSpan: 2,
        placeholder: "••••••••",
    },
];

const loginValidationSchema: ObjectSchema<Record<string, any>> = Yup.object().shape({
    username: Yup.string().required("Login jest wymagany"),
    password: Yup.string()
        .min(6, "Hasło musi mieć co najmniej 6 znaków")
        .required("Hasło jest wymagane"),
});

const LoginPage: React.FC = () => {
    const navigate = useNavigate();

    const handleLoginSubmit = async (values: Record<string, string>) => {
        try {
            const response = await fetch("https://localhost:8443/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "include",
                body: JSON.stringify({
                    username: values.username,
                    password: values.password,
                }),
            });
            const data = await response.json();
            if (response.ok) {
                navigate("/");
            } else {
                alert(data.message || "Nieprawidłowe dane logowania");
            }
        } catch (error) {
            console.error("Błąd podczas logowania:", error);
            alert("Błąd podczas logowania. Spróbuj ponownie.");
        }
    };

    const loginButton: ButtonConfig = {
        text: "Zaloguj",
        color: "#10B981",
        onClick: () => {
        },
    };

    return (
        <div
            className="min-h-screen bg-gradient-to-b from-white to-gray-100 flex items-center justify-center py-12 px-4 relative z-50">
            <div className="max-w-lg w-full bg-white p-8 rounded-3xl shadow-lg border border-gray-200">
                <h2 className="text-center text-2xl font-semibold text-gray-800 mb-4">
                    Zaloguj się
                </h2>
                <p className="text-center text-sm text-gray-500 mb-8">
                    Wpisz swój login i hasło
                </p>
                <FormBuilder
                    fields={loginFields}
                    onSubmit={handleLoginSubmit}
                    validationSchema={loginValidationSchema}
                    numColumns={2}
                    gap={20}
                    rightButton={loginButton}
                />
                <p className="mt-6 text-center text-sm text-gray-600">
                    Nie masz konta?{" "}
                    <button
                        onClick={() => navigate("/register")}
                        className="font-medium text-blue-600 hover:text-blue-800"
                    >
                        Zarejestruj się
                    </button>
                </p>
            </div>
        </div>
    );
};

export default LoginPage;
