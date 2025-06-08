import React, {useEffect, useState} from "react";
import type {ButtonConfig, FieldConfig} from "../common/forms/FormBuilder.tsx";
import FormBuilder from "../common/forms/FormBuilder.tsx";
import {redirect} from "react-router";
import Icon from "../common/icons/Icon.tsx";
import ConfettiIcon from "../../assets/confetti.svg?react";
import WeddingCake from "../../assets/wedding-cake.svg?react";
import HandshakeIcon from "../../assets/handshake.svg?react";

interface EventType {
    id: string;
    title: string;
    subtitle: string;
    icon: React.ComponentType<any>;
}

interface DishResponse {
    id: number;
    name: string;
    price: number;
    dish_type_id: number;
    dish_type_name: string;
    description: string;
    energy: number;
    protein: number;
    fiber: number;
    carbohydrates: number;
    fats: number;
    is_available: boolean;
}

interface Dish {
    id: string;
    name: string;
    subtitle: string;
    price: number;
    image: string;
    category: string;
    categoryName: string;
}

interface SelectedDish extends Dish {
    quantity: number;
}

interface DishCategory {
    id: string;
    name: string;
    dishTypeId?: number;
}

const CreatorSite: React.FC = () => {
    const [activeTab, setActiveTab] = useState<'compose' | 'guidelines'>('compose');
    const [selectedEventType, setSelectedEventType] = useState<string>('');
    const [eventDate, setEventDate] = useState<string>('');
    const [eventTime, setEventTime] = useState<string>('');
    const [peopleCount, setPeopleCount] = useState<number>(0);
    const [selectedDishCategory, setSelectedDishCategory] = useState<string>('wszystkie');
    const [selectedDishes, setSelectedDishes] = useState<SelectedDish[]>([]);
    const [dishes, setDishes] = useState<Dish[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [dishCategories, setDishCategories] = useState<DishCategory[]>([
        {id: 'wszystkie', name: 'Wszystkie'}
    ]);

    const fallbackImages = [
        'https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=400&h=300&fit=crop',
        'https://images.unsplash.com/photo-1594212699903-ec8a3eca50f5?w=400&h=300&fit=crop',
        'https://images.unsplash.com/photo-1547592180-85f173990554?w=400&h=300&fit=crop',
        'https://images.unsplash.com/photo-1476718406336-bb5a9690ee2a?w=400&h=300&fit=crop',
        'https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=400&h=300&fit=crop',
        'https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=400&h=300&fit=crop',
        'https://images.unsplash.com/photo-1488477181946-6428a0291777?w=400&h=300&fit=crop'
    ];

    const getRandomFallbackImage = () => {
        return fallbackImages[Math.floor(Math.random() * fallbackImages.length)];
    };

    const eventTypes: EventType[] = [
        {
            id: 'wedding',
            title: 'Wesele',
            subtitle: 'Uroczystość weselna dla gości',
            icon: WeddingCake
        },
        {
            id: 'birthday',
            title: 'Urodziny',
            subtitle: 'Przyjęcie urodzinowe',
            icon: ConfettiIcon
        },
        {
            id: 'business',
            title: 'Spotkanie biznesowe',
            subtitle: 'Spotkanie firmowe lub konferencja',
            icon: HandshakeIcon
        }
    ];

    useEffect(() => {
        const fetchDishes = async () => {
            try {
                setLoading(true);

                const response = await fetch('https://localhost:8443/api/dishes', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    credentials: 'include',
                });

                if (response.ok) {
                    const dishesResponse: DishResponse[] = await response.json();

                    const availableDishes = dishesResponse.filter(dish => dish.is_available === true);

                    const uniqueCategories = new Set<string>();
                    const categoryMap = new Map<string, number>();

                    availableDishes.forEach(dish => {
                        if (dish.dish_type_name) {
                            uniqueCategories.add(dish.dish_type_name);
                            categoryMap.set(dish.dish_type_name, dish.dish_type_id);
                        }
                    });

                    const categories: DishCategory[] = [
                        {id: 'wszystkie', name: 'Wszystkie'},
                        ...Array.from(uniqueCategories).map(categoryName => ({
                            id: categoryName,
                            name: categoryName,
                            dishTypeId: categoryMap.get(categoryName)
                        }))
                    ];

                    setDishCategories(categories);

                    const convertedDishes: Dish[] = availableDishes.map(dish => ({
                        id: dish.id.toString(),
                        name: dish.name,
                        subtitle: dish.description || 'Brak opisu',
                        price: Number(dish.price),
                        image: getRandomFallbackImage(),
                        category: dish.dish_type_name || 'inne',
                        categoryName: dish.dish_type_name || 'inne'
                    }));

                    setDishes(convertedDishes);
                } else {
                    setDishes([]);
                    setDishCategories([{id: 'wszystkie', name: 'Wszystkie'}]);
                }
            } catch (error) {
                setDishes([]);
                setDishCategories([{id: 'wszystkie', name: 'Wszystkie'}]);
            } finally {
                setLoading(false);
            }
        };

        fetchDishes();
    }, []);

    const filteredDishes = selectedDishCategory === 'wszystkie'
        ? dishes
        : dishes.filter(dish => dish.category === selectedDishCategory);

    const addDish = (dish: Dish) => {
        const existing = selectedDishes.find(d => d.id === dish.id);
        if (existing) {
            setSelectedDishes(selectedDishes.map(d =>
                d.id === dish.id ? {...d, quantity: d.quantity + 1} : d
            ));
        } else {
            setSelectedDishes([...selectedDishes, {...dish, quantity: 1}]);
        }
    };

    const removeDish = (dishId: string) => {
        setSelectedDishes(selectedDishes.filter(d => d.id !== dishId));
    };

    const increaseDishQuantity = (dishId: string) => {
        setSelectedDishes(selectedDishes.map(d =>
            d.id === dishId ? {...d, quantity: d.quantity + 1} : d
        ));
    };

    const decreaseDishQuantity = (dishId: string) => {
        setSelectedDishes(selectedDishes.map(d =>
            d.id === dishId ? {...d, quantity: Math.max(1, d.quantity - 1)} : d
        ));
    };

    const totalCost = selectedDishes.reduce((total, dish) =>
        total + (dish.price * dish.quantity), 0
    ) * peopleCount;

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
        color: "#16a34a",
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

    const renderStepIndicator = () => (
        <div className="flex justify-center mb-8">
            <div className="flex items-center space-x-4">
                <div
                    className="flex items-center justify-center w-10 h-10 bg-green-600 text-white rounded-full font-semibold">
                    1
                </div>
                <div className="w-16 h-1 bg-gray-300"></div>
                <div
                    className="flex items-center justify-center w-10 h-10 bg-gray-300 text-gray-600 rounded-full font-semibold">
                    2
                </div>
                <div className="w-16 h-1 bg-gray-300"></div>
                <div
                    className="flex items-center justify-center w-10 h-10 bg-gray-300 text-gray-600 rounded-full font-semibold">
                    3
                </div>
            </div>
        </div>
    );

    const renderComposeMenu = () => (
        <div>
            <div className="text-center mb-8">
                <h1 className="text-3xl font-bold text-gray-900 mb-2">
                    Kreator wydarzeń okolicznościowych
                </h1>
                <p className="text-gray-600 mb-6">
                    Skomponuj zestaw dań dla gości
                </p>

                {renderStepIndicator()}
            </div>


            <div className="mb-8">
                <h2 className="text-xl font-semibold mb-4">1. Wybierz typ wydarzenia</h2>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                    {eventTypes.map((type) => (
                        <div
                            key={type.id}
                            onClick={() => setSelectedEventType(type.id)}
                            className={`bg-white rounded-lg p-6 shadow-sm hover:shadow-md transition-shadow cursor-pointer ${
                                selectedEventType === type.id
                                    ? 'ring-2 ring-green-500'
                                    : ''
                            }`}
                        >
                            <div className="mb-4">
                                <Icon
                                    icon={type.icon}
                                    className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center"
                                    iconProps={{
                                        className: "w-6 h-6 text-green-600 fill-current"
                                    }}
                                />
                            </div>

                            <h3 className="text-xl font-bold text-gray-900 mb-3">
                                {type.title}
                            </h3>

                            <p className="text-gray-600 leading-relaxed">
                                {type.subtitle}
                            </p>
                        </div>
                    ))}
                </div>
            </div>

            <div className="mb-8">
                <h2 className="text-xl font-semibold mb-4">2. Dane wydarzenia</h2>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Data
                        </label>
                        <input
                            type="date"
                            value={eventDate}
                            onChange={(e) => setEventDate(e.target.value)}
                            className="w-full px-3 py-2 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Godzina rozpoczęcia
                        </label>
                        <input
                            type="time"
                            value={eventTime}
                            onChange={(e) => setEventTime(e.target.value)}
                            className="w-full px-3 py-2 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                            Liczba osób
                        </label>
                        <input
                            type="number"
                            value={peopleCount || ''}
                            onChange={(e) => setPeopleCount(parseInt(e.target.value) || 0)}
                            className="w-full px-3 py-2 border border-gray-300 rounded-xl focus:outline-none focus:ring-2 focus:ring-green-500"
                            placeholder="Liczba gości"
                        />
                    </div>
                </div>
            </div>

            <div className="mb-8">
                <h2 className="text-xl font-semibold mb-2">3. Skomponuj zestaw dań</h2>
                <p className="text-gray-600 mb-4">Wybierz z naszego menu</p>

                {loading ? (
                    <div className="flex justify-center items-center py-8">
                        <div className="text-gray-600">Ładowanie dań...</div>
                    </div>
                ) : (
                    <>
                        <div className="flex flex-wrap gap-2 mb-6">
                            {dishCategories.map((category) => (
                                <button
                                    key={category.id}
                                    onClick={() => setSelectedDishCategory(category.id)}
                                    className={`px-6 py-3 rounded-full font-medium transition-all ${
                                        selectedDishCategory === category.id
                                            ? 'bg-green-600 text-white shadow-lg'
                                            : 'bg-gray-200 text-gray-700 hover:bg-green-100 hover:text-green-800'
                                    }`}
                                >
                                    {category.name}
                                </button>
                            ))}
                        </div>

                        <div className="mb-4 text-sm text-gray-600">
                            Znaleziono {filteredDishes.length} dań{selectedDishCategory !== 'wszystkie' && ` w kategorii "${dishCategories.find(c => c.id === selectedDishCategory)?.name}"`}
                        </div>

                        {filteredDishes.length > 0 ? (
                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-8">
                                {filteredDishes.map((dish) => (
                                    <div key={dish.id}
                                         className="border border-gray-300 rounded-lg p-4 bg-white shadow-sm hover:shadow-md transition-shadow">
                                        <div className="flex items-start space-x-4">
                                            <img
                                                src={dish.image}
                                                alt={dish.name}
                                                className="w-20 h-20 object-cover rounded-lg flex-shrink-0"
                                                onError={(e) => {
                                                    const target = e.target as HTMLImageElement;
                                                    target.src = getRandomFallbackImage();
                                                }}
                                            />
                                            <div className="flex-1">
                                                <h3 className="font-semibold text-lg mb-1">{dish.name}</h3>
                                                <p className="text-gray-600 text-sm mb-2">{dish.subtitle}</p>
                                                <p className="font-semibold text-green-600 mb-3">{dish.price.toFixed(2)} zł
                                                    / osoba</p>
                                                <button
                                                    onClick={() => addDish(dish)}
                                                    className="bg-green-600 text-white px-4 py-2 rounded-2xl hover:bg-green-700 transition-colors text-sm font-semibold"
                                                >
                                                    Dodaj
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div className="text-center py-8 text-gray-500">
                                <p>Brak dostępnych dań w tej kategorii</p>
                            </div>
                        )}
                    </>
                )}

                {selectedDishes.length > 0 && (
                    <div className="bg-gray-50 rounded-lg p-4 mb-8">
                        <h3 className="font-semibold mb-4">Dodane dania:</h3>
                        <div className="space-y-2">
                            {selectedDishes.map((dish) => {
                                const dishTotalForPeople = dish.price * dish.quantity * peopleCount;
                                return (
                                    <div key={dish.id}
                                         className="flex items-center justify-between bg-white p-3 rounded-lg">
                                        <div className="flex items-center space-x-3">
                                            <img
                                                src={dish.image}
                                                alt={dish.name}
                                                className="w-12 h-12 object-cover rounded-lg"
                                                onError={(e) => {
                                                    const target = e.target as HTMLImageElement;
                                                    target.src = getRandomFallbackImage();
                                                }}
                                            />
                                            <div className="flex-1">
                                                <p className="font-medium">
                                                    {dish.name} ({dish.categoryName})
                                                </p>
                                                <p className="text-sm text-gray-600">
                                                    {dish.price.toFixed(2)} zł × {dish.quantity} × {peopleCount} osób
                                                    = {dishTotalForPeople.toFixed(2)} zł
                                                </p>
                                            </div>
                                        </div>

                                        <div className="flex items-center space-x-2">
                                            <div className="flex items-center bg-gray-100 rounded-lg">
                                                <button
                                                    onClick={() => decreaseDishQuantity(dish.id)}
                                                    className="w-8 h-8 flex items-center justify-center text-gray-600 hover:text-green-600 hover:bg-green-100 rounded-l-lg transition-colors"
                                                    disabled={dish.quantity <= 1}
                                                >
                                                    -
                                                </button>
                                                <div
                                                    className="w-10 h-8 flex items-center justify-center text-sm font-medium bg-white">
                                                    {dish.quantity}
                                                </div>
                                                <button
                                                    onClick={() => increaseDishQuantity(dish.id)}
                                                    className="w-8 h-8 flex items-center justify-center text-gray-600 hover:text-green-600 hover:bg-green-100 rounded-r-lg transition-colors"
                                                >
                                                    +
                                                </button>
                                            </div>

                                            <button
                                                onClick={() => removeDish(dish.id)}
                                                className="w-8 h-8 flex items-center justify-center bg-red-100 text-red-600 rounded-full hover:bg-red-200 transition-colors"
                                            >
                                                ×
                                            </button>
                                        </div>
                                    </div>
                                );
                            })}
                        </div>
                    </div>
                )}
            </div>

            <div className="grid grid-cols-3 items-center">
                <div className="justify-self-start">
                    <button
                        type="button"
                        onClick={() => redirect("/")}
                        style={{backgroundColor: "#6B7280"}}
                        className="text-white font-semibold rounded-2xl px-4 py-2 shadow hover:opacity-90 focus:outline-none focus:ring-2 focus:ring-offset-2"
                    >
                        Wróć
                    </button>
                </div>

                <div className="text-center">
                    <p className="text-sm text-gray-600">Szacowany koszt</p>
                    <p className="text-2xl font-bold text-green-600">{totalCost.toFixed(2)} zł</p>
                </div>

                <div className="justify-self-end">
                    <button
                        type="button"
                        onClick={async () => {
                            try {
                                const eventData = {
                                    event_type: selectedEventType,
                                    date: eventDate,
                                    time: eventTime,
                                    people_count: peopleCount,
                                    selected_dishes: selectedDishes,
                                    total_cost: totalCost
                                };

                                const response = await fetch('https://localhost:8443/api/client/events', {
                                    method: 'POST',
                                    headers: {
                                        'Content-Type': 'application/json',
                                    },
                                    credentials: "include",
                                    body: JSON.stringify(eventData)
                                });

                                if (response.ok) {
                                    const result = await response.json();
                                    alert('Wydarzenie zostało utworzone!');
                                    redirect("/");
                                } else {
                                    console.error('Błąd podczas tworzenia wydarzenia:', response.statusText);
                                }
                            } catch (error) {
                                console.error('Błąd sieci:', error);
                            }
                        }}
                        style={{backgroundColor: "#16a34a"}}
                        className="text-white font-semibold rounded-2xl px-4 py-2 shadow hover:opacity-90 focus:outline-none focus:ring-2 focus:ring-offset-2"
                    >
                        Przejdź dalej
                    </button>
                </div>
            </div>
        </div>
    );

    const renderGuidelines = () => (
        <div>
            <div className="text-center mb-8">
                <h1 className="text-3xl font-bold text-gray-900 mb-2">
                    Prześlij własne wytyczne
                </h1>
                <p className="text-gray-600">
                    Opisz swoje oczekiwania, a nasi kucharze przygotują dla Ciebie spersonalizowaną propozycję menu.
                </p>
            </div>

            <FormBuilder
                fields={fields}
                onSubmit={handleSubmit}
                numColumns={2}
                gap={20}
                leftButton={leftButton}
                rightButton={rightButton}
            />
        </div>
    );

    return (
        <div className="min-h-screen py-10">
            <div className="max-w-6xl mx-auto">
                <div className="bg-white rounded-2xl shadow-lg relative z-10">
                    <div className="flex border-b">
                        <button
                            onClick={() => setActiveTab('compose')}
                            className={`flex-1 py-4 px-6 text-center font-medium transition-colors ${
                                activeTab === 'compose'
                                    ? 'text-green-600 border-b-2 border-green-600 bg-green-50'
                                    : 'text-gray-600 hover:text-green-600 hover:bg-green-50'
                            }`}
                        >
                            Skomponuj menu
                        </button>
                        <button
                            onClick={() => setActiveTab('guidelines')}
                            className={`flex-1 py-4 px-6 text-center font-medium transition-colors ${
                                activeTab === 'guidelines'
                                    ? 'text-green-600 border-b-2 border-green-600 bg-green-50'
                                    : 'text-gray-600 hover:text-green-600 hover:bg-green-50'
                            }`}
                        >
                            Prześlij wytyczne
                        </button>
                    </div>

                    <div className="p-6">
                        {activeTab === 'compose' ? renderComposeMenu() : renderGuidelines()}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default CreatorSite;
