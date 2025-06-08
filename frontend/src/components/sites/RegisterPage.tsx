import React from "react";
import {useNavigate} from "react-router-dom";
import type {ObjectSchema} from "yup";
import * as Yup from "yup";
import FormBuilder, {type ButtonConfig, type FieldConfig} from "../common/forms/FormBuilder";

const registerFields: FieldConfig[] = [
    {
        name: "username",
        label: "Nazwa użytkownika",
        type: "text",
        row: 1,
        col: 1,
        colSpan: 2,
        placeholder: "Wpisz nazwę użytkownika",
    },
    {
        name: "password",
        label: "Hasło",
        type: "password",
        row: 2,
        col: 1,
        colSpan: 2,
        placeholder: "Wpisz hasło",
    },
    {
        name: "email",
        label: "E-mail",
        type: "email",
        row: 3,
        col: 1,
        colSpan: 2,
        placeholder: "twoj@email.pl",
    },
    {
        name: "first_name",
        label: "Imię",
        type: "text",
        row: 4,
        col: 1,
        colSpan: 1,
        placeholder: "Wpisz imię",
    },
    {
        name: "last_name",
        label: "Nazwisko",
        type: "text",
        row: 4,
        col: 2,
        colSpan: 1,
        placeholder: "Wpisz nazwisko",
    },
    {
        name: "phone",
        label: "Numer telefonu",
        type: "text",
        row: 5,
        col: 1,
        colSpan: 2,
        placeholder: "Wpisz numer telefonu",
    },
];

const registerValidationSchema: ObjectSchema<Record<string, any>> = Yup.object().shape({
    username: Yup.string().required("Nazwa użytkownika jest wymagana"),
    password: Yup.string()
        .min(6, "Hasło musi mieć co najmniej 6 znaków")
        .required("Hasło jest wymagane"),
    email: Yup.string()
        .email("Email musi mieć poprawny format")
        .required("Email jest wymagany"),
    first_name: Yup.string().required("Imię jest wymagane"),
    last_name: Yup.string().required("Nazwisko jest wymagane"),
    phone: Yup.string().required("Numer telefonu jest wymagany"),
});

const RegisterPage: React.FC = () => {
    const navigate = useNavigate();

    const handleRegisterSubmit = async (values: Record<string, string>) => {
        try {
            const response = await fetch("https://localhost:8443/api/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(values),
            });
            const data = await response.json();
            if (response.ok) {
                navigate("/login");
            } else {
                alert(data.message || "Coś poszło nie tak podczas rejestracji.");
            }
        } catch (error) {
            console.error("Błąd podczas rejestracji:", error);
            alert("Błąd podczas rejestracji. Spróbuj ponownie.");
        }
    };

    const leftButton: ButtonConfig = {
        text: "Wróć",
        color: "#6B7280",
        onClick: () => navigate("/"),
    };

    const rightButton: ButtonConfig = {
        text: "Zarejestruj się",
        color: "#10B981",
        onClick: () => {
        },
    };

    return (
        <div
            className="min-h-screen bg-gradient-to-b from-white to-gray-100 flex items-center justify-center py-12 px-4 relative z-50">
            <div className="max-w-3xl w-full bg-white p-8 rounded-3xl shadow-lg border border-gray-200">
                <h2 className="text-center text-2xl font-semibold text-gray-800 mb-4">Rejestracja</h2>
                <p className="text-center text-sm text-gray-500 mb-8">Utwórz nowe konto</p>
                <FormBuilder
                    fields={registerFields}
                    onSubmit={handleRegisterSubmit}
                    validationSchema={registerValidationSchema}
                    numColumns={2}
                    gap={20}
                    leftButton={leftButton}
                    rightButton={rightButton}
                />
                <p className="mt-6 text-center text-sm text-gray-600">
                    Masz już konto?{' '}
                    <button
                        onClick={() => navigate("/login")}
                        className="font-medium text-blue-600 hover:text-blue-800"
                    >
                        Zaloguj się
                    </button>
                </p>
            </div>
        </div>
    );
};

export default RegisterPage;
