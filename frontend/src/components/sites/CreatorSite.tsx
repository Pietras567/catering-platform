import React from "react";
import type {ButtonConfig, FieldConfig} from "../common/forms/FormBuilder.tsx";
import FormBuilder from "../common/forms/FormBuilder.tsx";
import {redirect} from "react-router";

const CreatorSite: React.FC = () => {
    const fields: FieldConfig[] = [
        {
            name: "event_type",
            label: "Typ wydarzenia",
            type: "text",
            row: 1,
            col: 1,
            colSpan: 2,
            placeholder: "np. wesele, urodziny, spotkanie biznesowe",
        },
        {
            name: "people_count",
            label: "Liczba osób",
            type: "number",
            row: 2,
            col: 1,
            colSpan: 2,
            placeholder: "Podaj liczbę gości",
        },
        {
            name: "date",
            label: "Data",
            type: "date",
            row: 3,
            col: 1,
        },
        {
            name: "time",
            label: "Godzina",
            type: "time",
            row: 3,
            col: 2,
        },
        {
            name: "preferences",
            label: "Twoje wytyczne dotyczące menu",
            type: "textarea",
            row: 4,
            col: 1,
            colSpan: 2,
            placeholder: "Opisz swoje preferencje smakowe, ulubione składniki, dania, które chcesz uwzględnić lub wykluczyć...",
        },
        {
            name: "budget",
            label: "Budżet na osobę",
            type: "number",
            row: 5,
            col: 1,
            colSpan: 2,
            placeholder: "50"
        },
        {
            name: "dietary_preferences",
            label: "Preferencje dietetyczne",
            type: "textarea",
            row: 6,
            col: 1,
            colSpan: 2,
            placeholder: "Alergie, diety specjalne (wegetariańska, bezglutenowa, itp.),",
        },
    ];

    const leftButton: ButtonConfig = {
        text: "Wróć",
        color: "#6B7280",
        onClick: () => {
            redirect("/");
        },
    };

    const rightButton: ButtonConfig = {
        text: "Wyślij wytyczne",
        color: "#10B981",
        onClick: () => {
            console.log("Kliknięto wyślij wytyczne");
        },
    };

    const handleSubmit = async (values: Record<string, string>) => {
        try {
            console.log("Wysyłanie danych:", values);

            const eventData = {
                event_type: values.event_type,
                people_count: parseInt(values.people_count),
                date: values.date,
                time: values.time,
                preferences: values.preferences || null,
                budget: values.budget ? parseFloat(values.budget) : null,
                dietary_preferences: values.dietary_preferences || null
            };

            const response = await fetch('https://localhost:8443/api/client/event-requests', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: "include",
                body: JSON.stringify(eventData)
            });

            if (response.ok) {
                const result = await response.json();
                console.log("Success:", result);
                alert("The guidelines have been successfully sent!");
                redirect("/");
            } else {
                const error = await response.json();
                console.error("Error: ", error);
                alert("There was an error while uploading the guidelines: " + (error.message || "Unknown error"));
            }
        } catch (error) {
            console.error("Network error:", error);
            alert("An error occurred during data transfer. Check your Internet connection.");
        }
    };

    return (
        <div className="min-h-screen py-10">
            <div className="max-w-6xl mx-auto">
                <div className="bg-white rounded-2xl shadow-lg p-6 relative z-10">
                    <p className="mb-2">
                        <span className="text-2xl font-bold">Prześlij własne wytyczne</span>
                    </p>
                    <p className="mb-10">
                        <span className="text-gray-600 text-sm">
                          Opisz swoje oczekiwania, a nasi kucharze przygotują dla Ciebie spersonalizowaną propozycję menu.
                        </span>
                    </p>

                    <FormBuilder
                        fields={fields}
                        onSubmit={handleSubmit}
                        numColumns={2}
                        gap={20}
                        leftButton={leftButton}
                        rightButton={rightButton}
                    />
                </div>
            </div>
        </div>
    );
};

export default CreatorSite;
