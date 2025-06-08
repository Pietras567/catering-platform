import React from "react";
import {Field, Form, Formik, type FormikErrors, type FormikHelpers} from "formik";
import type {ObjectSchema} from "yup";

export interface FieldConfig {
    name: string;
    type: string;
    label: string;
    row: number;
    col: number;
    colSpan?: number;
    rowSpan?: number;
    placeholder?: string;
}

export interface ButtonConfig {
    text: string;
    color: string;
    onClick: () => void;
}

export interface FormBuilderProps {
    fields: FieldConfig[];
    onSubmit: (
        values: Record<string, string>,
        formikHelpers: FormikHelpers<Record<string, string>>
    ) => void;
    validationSchema?: ObjectSchema<Record<string, any>>;
    numColumns?: number;
    gap?: number;
    leftButton?: ButtonConfig;
    rightButton?: ButtonConfig;
}

const FormBuilder: React.FC<FormBuilderProps> = ({
                                                     fields,
                                                     onSubmit,
                                                     validationSchema,
                                                     numColumns,
                                                     gap = 16,
                                                     leftButton,
                                                     rightButton
                                                 }) => {
    const computedMaxCol = fields.reduce(
        (max, f) => Math.max(max, f.col + (f.colSpan ?? 1) - 1),
        1
    );
    const computedMaxRow = fields.reduce(
        (max, f) => Math.max(max, f.row + (f.rowSpan ?? 1) - 1),
        1
    );

    const cols = numColumns && numColumns > 0 ? numColumns : computedMaxCol;

    const initialValues: Record<string, string> = {};
    fields.forEach((f) => {
        initialValues[f.name] = "";
    });

    const justifyClass = leftButton && rightButton ? "justify-between" : "justify-center";

    return (
        <Formik<Record<string, string>>
            initialValues={initialValues}
            validationSchema={validationSchema}
            onSubmit={onSubmit}
        >
            {({errors, touched}) => (
                <Form
                    style={{
                        display: "grid",
                        gridTemplateColumns: `repeat(${cols}, minmax(0, 1fr))`,
                        gridAutoRows: "auto",
                        gap: `${gap}px`,
                    }}
                >
                    {fields.map((field) => {
                        const colSpan = field.colSpan ?? 1;
                        const rowSpan = field.rowSpan ?? 1;
                        const errorForField = (errors as FormikErrors<Record<string, string>>)[field.name];
                        const touchedForField = touched[field.name];

                        return (
                            <div
                                key={field.name}
                                style={{
                                    gridColumnStart: field.col,
                                    gridColumnEnd: `span ${colSpan}`,
                                    gridRowStart: field.row,
                                    gridRowEnd: `span ${rowSpan}`,
                                }}
                                className="flex flex-col"
                            >
                                <label
                                    htmlFor={field.name}
                                    className="text-sm font-medium text-gray-700"
                                >
                                    {field.label}
                                </label>

                                {field.type === "textarea" ? (
                                    <Field
                                        as="textarea"
                                        id={field.name}
                                        name={field.name}
                                        placeholder={field.placeholder ?? ""}
                                        rows={4}
                                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-xl shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 resize-vertical"
                                    />
                                ) : (
                                    <Field
                                        id={field.name}
                                        name={field.name}
                                        type={field.type}
                                        placeholder={field.placeholder ?? ""}
                                        className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-xl shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                                    />
                                )}

                                {typeof errorForField === "string" && touchedForField && (
                                    <span className="text-red-600 text-sm mt-1">
                                        {errorForField}
                                    </span>
                                )}
                            </div>
                        );
                    })}

                    <div
                        style={{
                            gridColumnStart: 1,
                            gridColumnEnd: `span ${cols}`,
                            gridRowStart: computedMaxRow + 1,
                        }}
                        className={`flex ${justifyClass}`}
                    >
                        {leftButton && (
                            <button
                                type="button"
                                onClick={leftButton.onClick}
                                style={{backgroundColor: leftButton.color}}
                                className="text-white font-semibold rounded-2xl px-4 py-2 shadow hover:opacity-90 focus:outline-none focus:ring-2 focus:ring-offset-2"
                            >
                                {leftButton.text}
                            </button>
                        )}

                        {rightButton && (
                            <button
                                type="submit"
                                onClick={rightButton.onClick}
                                style={{backgroundColor: rightButton.color}}
                                className="text-white font-semibold rounded-2xl px-4 py-2 shadow hover:opacity-90 focus:outline-none focus:ring-2 focus:ring-offset-2"
                            >
                                {rightButton.text}
                            </button>
                        )}
                    </div>
                </Form>
            )}
        </Formik>
    );
};

export default FormBuilder;
