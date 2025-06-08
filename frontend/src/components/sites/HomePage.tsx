import Button from "../common/buttons/button.tsx";
import LandingPhoto from "../../assets/landing_photo.avif"
import LeafIcon from "../../assets/leaf.svg?react";
import EcoIcon from "../../assets/eco.svg?react";
import HearthIcon from "../../assets/heart.svg?react";
import Icon from "../common/icons/Icon.tsx";
import {useEffect, useState} from "react";
import LunchPro from "../../assets/LunchPro.avif"
import Bankiet from "../../assets/Bankiet.avif"
import FitAndGreen from "../../assets/FitAndGreen.avif"
import Family from "../../assets/Family.jpg"
import LightDiet from "../../assets/Light.jpg"
import Gourmet from "../../assets/Gourmet.jpg"
import FormBuilder from "../common/forms/FormBuilder.tsx";
import * as Yup from "yup";

const HomePage = () => {
    const [currentMenuIndex, setCurrentMenuIndex] = useState(0);
    const [isAnimating, setIsAnimating] = useState(false);

    const [currentReviewIndex, setCurrentReviewIndex] = useState(0);
    const [isReviewAnimating, setIsReviewAnimating] = useState(false);

    const features = [
        {
            icon: LeafIcon,
            title: "Sezonowe składniki",
            description: "Tylko świeże, lokalne produkty w szczytowym momencie sezonu."
        },
        {
            icon: HearthIcon,
            title: "Proste przepisy",
            description: "Maksimum smaku przy minimum składników. Żadnych zbędnych dodatków."
        },
        {
            icon: EcoIcon,
            title: "Zrównoważony rozwój",
            description: "Ekologiczne opakowania i zero marnowania żywności."
        }
    ];

    const menuItems = [
        {
            id: 1,
            image: LunchPro,
            title: "Lunch Pro",
            description: "Idealne rozwiązanie na spotkania biznesowe",
            price: 120
        },
        {
            id: 2,
            image: Bankiet,
            title: "Bankiet",
            description: "Wyjątkowe menu na specjalne okazje.",
            price: 180
        },
        {
            id: 3,
            image: FitAndGreen,
            title: "Fit & Green",
            description: "Zdrowe, roślinne posiłki dostarczane codziennie.",
            price: 299
        },
        {
            id: 4,
            image: Family,
            title: "Family",
            description: "Tradycyjne smaki w nowoczesnym wydaniu. Idealny dla całej rodziny.",
            price: 125
        },
        {
            id: 5,
            image: LightDiet,
            title: "Light",
            description: "Lekkie i zdrowe posiłki dla osób dbających o linię i kondycję.",
            price: 110
        },
        {
            id: 6,
            image: Gourmet,
            title: "Gourmet",
            description: "Wykwintne dania inspirowane kuchnią międzynarodową.",
            price: 350
        }
    ];

    const steps = [
        {
            number: 1,
            title: "Wybierz",
            description: "Gotowe menu lub stwórz własne."
        },
        {
            number: 2,
            title: "Zamów",
            description: "Określ datę i miejsce dostawy."
        },
        {
            number: 3,
            title: "Zapłać",
            description: "Online lub przy odbiorze."
        },
        {
            number: 4,
            title: "Ciesz się",
            description: "Świeżym i smacznym posiłkiem."
        }
    ];

    const reviews = [
        {
            id: 1,
            rating: 5,
            quote: "Absolutnie wyjątkowy catering! Każde danie to małe dzieło sztuki. Goście byli zachwyceni smakiem i prezentacją.",
            name: "Anna K.",
            position: "Dyrektor marketingu"
        },
        {
            id: 2,
            rating: 5,
            quote: "Zamawiam regularnie na spotkania biznesowe. Zawsze punktualna dostawa i świeże składniki. Polecam wszystkim!",
            name: "Marcin S.",
            position: "CEO"
        },
        {
            id: 3,
            rating: 5,
            quote: "Fit & Green to strzał w dziesiątkę! Zdrowe, smaczne posiłki dostarczone prosto do domu. Wreszcie mam czas na siebie.",
            name: "Kasia M.",
            position: "Klientka indywidualna"
        },
        {
            id: 4,
            rating: 5,
            quote: "Organizowałam bankiet dla 50 osób. Wszystko było perfekcyjne - od serwisu po ostatnią łyżeczkę deseru.",
            name: "Joanna P.",
            position: "Event manager"
        },
        {
            id: 5,
            rating: 5,
            quote: "Family to idealne rozwiązanie dla naszej rodziny. Dzieci uwielbiają te posiłki, a ja mam pewność co do jakości.",
            name: "Tomasz W.",
            position: "Klient indywidualny"
        },
        {
            id: 6,
            rating: 5,
            quote: "Catering na najwyższym poziomie. Goście ciągle pytają, skąd było to jedzenie. Zdecydowanie będę zamawiać ponownie!",
            name: "Magdalena R.",
            position: "Organizatorka eventów"
        }
    ];

    useEffect(() => {
        const interval = setInterval(() => {
            setIsAnimating(true);
            setTimeout(() => {
                setCurrentMenuIndex(prev => (prev + 1) % menuItems.length);
                setIsAnimating(false);
            }, 150);
        }, 10000);

        return () => clearInterval(interval);
    }, [menuItems.length]);

    const getCurrentMenuItems = () => {
        const items = [];
        for (let i = 0; i < 3; i++) {
            items.push(menuItems[(currentMenuIndex + i) % menuItems.length]);
        }
        return items;
    };

    useEffect(() => {
        const interval = setInterval(() => {
            setIsReviewAnimating(true);
            setTimeout(() => {
                setCurrentReviewIndex(prev => (prev + 1) % (reviews.length - 1));
                setIsReviewAnimating(false);
            }, 150);
        }, 8000);

        return () => clearInterval(interval);
    }, [reviews.length]);

    const getCurrentReviews = () => {
        const items = [];
        for (let i = 0; i < 2; i++) {
            items.push(reviews[(currentReviewIndex + i) % reviews.length]);
        }
        return items;
    };

    const contactValidationSchema = Yup.object({
        fullName: Yup.string()
            .min(2, 'Imię i nazwisko musi mieć co najmniej 2 znaki')
            .required('Imię i nazwisko jest wymagane'),
        email: Yup.string()
            .email('Nieprawidłowy format email')
            .required('Email jest wymagany'),
        message: Yup.string()
            .min(10, 'Wiadomość musi mieć co najmniej 10 znaków')
            .required('Wiadomość jest wymagana')
    });

    const contactFields = [
        {
            name: "fullName",
            type: "text",
            label: "Imię i Nazwisko",
            row: 1,
            col: 1,
            placeholder: "Jan Kowalski"
        },
        {
            name: "email",
            type: "email",
            label: "Email",
            row: 1,
            col: 2,
            placeholder: "jan@example.com"
        },
        {
            name: "message",
            type: "textarea",
            label: "Wiadomość",
            row: 2,
            col: 1,
            colSpan: 2,
            placeholder: "Napisz swoją wiadomość..."
        }
    ];

    const handleContactSubmit = (values: Record<string, string>) => {
        console.log('Formularz kontaktowy:', values);
    };

    return (
        <>
            <section className="min-h-150 flex items-center justify-center px-4 py-8">
                <div className="max-w-6xl w-full grid grid-cols-1 lg:grid-cols-2 gap-8 items-center">
                    <div className="space-y-6">
                        <h1 className="text-4xl lg:text-5xl font-bold text-gray-900">
                            Prosto. Świeżo. Smacznie.
                        </h1>

                        <p className="text-lg lg:text-xl text-gray-600 leading-relaxed">
                            Catering z maksymalną dbałością o składniki. Doświadcz różnicy
                        </p>

                        <div className="flex flex-col sm:flex-row gap-4 pt-4">
                            <Button
                                classes="bg-green-600 hover:bg-green-700 text-white px-8 py-3 font-semibold transition-colors"
                                label="Zamów teraz"
                                onClickAction={() => console.log('Zamów teraz')}
                            />
                            <Button
                                classes="bg-white hover:bg-gray-50 text-gray-900 border-2 border-gray-300 px-8 py-3 font-semibold transition-colors"
                                label="Więcej informacji"
                                onClickAction={() => console.log('Więcej informacji')}
                            />
                        </div>
                    </div>

                    <div className="flex justify-center lg:justify-end">
                        <img
                            src={LandingPhoto}
                            alt="Catering - świeże składniki"
                            className="w-full max-w-lg h-auto rounded-2xl shadow-lg"
                        />
                    </div>
                </div>
            </section>

            <section className="py-16 px-4">
                <div className="max-w-6xl mx-auto">
                    <div className="text-center mb-12">
                        <h2 className="text-3xl lg:text-4xl font-bold text-gray-900 mb-4">
                            Dlaczego my?
                        </h2>
                        <p className="text-lg text-gray-600">
                            Prostość i jakość w każdym detalu
                        </p>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                        {features.map((feature, index) => (
                            <div key={index}
                                 className="bg-white rounded-lg p-6 shadow-sm hover:shadow-md transition-shadow">
                                <div className="mb-4">
                                    <Icon
                                        icon={feature.icon}
                                        className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center"
                                        iconProps={{
                                            className: "w-6 h-6 text-green-800"
                                        }}
                                    />
                                </div>

                                <h3 className="text-xl font-bold text-gray-900 mb-3">
                                    {feature.title}
                                </h3>

                                <p className="text-gray-600 leading-relaxed">
                                    {feature.description}
                                </p>
                            </div>
                        ))}
                    </div>
                </div>
            </section>

            <section className="py-16 px-4">
                <div className="max-w-6xl mx-auto">
                    <div className="text-center mb-12">
                        <h2 className="text-3xl lg:text-4xl font-bold text-gray-900 mb-4">
                            Menu
                        </h2>
                        <p className="text-lg text-gray-600">
                            Odkryj nasze wyjątkowe zestawy
                        </p>
                    </div>

                    <div className={`grid grid-cols-1 md:grid-cols-3 gap-8 transition-all duration-300 ${
                        isAnimating ? 'opacity-0 transform translate-x-4' : 'opacity-100 transform translate-x-0'
                    }`}>
                        {getCurrentMenuItems().map((item) => (
                            <div key={`${item.id}-${currentMenuIndex}`}
                                 className="bg-white rounded-lg overflow-hidden shadow-sm hover:shadow-md transition-shadow">
                                <img
                                    src={item.image}
                                    alt={item.title}
                                    className="w-full h-48 object-cover"
                                />

                                <div className="p-6">
                                    <h3 className="text-xl font-bold text-gray-900 mb-3">
                                        {item.title}
                                    </h3>

                                    <p className="text-gray-600 leading-relaxed mb-4">
                                        {item.description}
                                    </p>

                                    <div className="flex justify-between items-center">
                                        <span className="text-lg font-semibold text-gray-900">
                                            {item.price} zł/os.
                                        </span>
                                        <Button
                                            classes="bg-green-600 hover:bg-green-700 text-white px-4 py-2 text-sm font-semibold transition-colors"
                                            label="Wybierz"
                                            onClickAction={() => console.log(`Wybrano: ${item.title}`)}
                                        />
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>

                    <div className="text-center mt-8">
                        <Button
                            classes="text-gray-600 hover:text-gray-700 bg-transparent hover:bg-gray-200 border-1 border-gray-600 hover:border-gray-700 px-6 py-2 font-semibold transition-all"
                            label="Zobacz więcej"
                            onClickAction={() => console.log('Zobacz więcej menu')}
                        />
                    </div>

                </div>
            </section>

            <section className="py-16 px-4">
                <div className="max-w-6xl mx-auto">
                    <div className="text-center mb-12">
                        <h2 className="text-3xl lg:text-4xl font-bold text-gray-900 mb-4">
                            Jak to działa?
                        </h2>
                        <p className="text-lg text-gray-600">
                            Prosto i bez zbędnych komplikacji
                        </p>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
                        {steps.map((step) => (
                            <div key={step.number} className="text-center">
                                <div
                                    className="w-16 h-16 bg-green-100 text-green-500 rounded-full flex items-center justify-center text-2xl font-bold mx-auto mb-4">
                                    {step.number}
                                </div>
                                <h3 className="text-xl font-bold text-gray-900 mb-3">
                                    {step.title}
                                </h3>
                                <p className="text-gray-600 leading-relaxed">
                                    {step.description}
                                </p>
                            </div>
                        ))}
                    </div>
                </div>
            </section>

            <section className="py-16 px-4">
                <div className="max-w-6xl mx-auto">
                    <div className="text-center mb-12">
                        <h2 className="text-3xl lg:text-4xl font-bold text-gray-900 mb-4">
                            Co mówią nasi klienci?
                        </h2>
                        <p className="text-lg text-gray-600">
                            Zaufanie budujemy każdym posiłkiem
                        </p>
                    </div>

                    <div className={`grid grid-cols-1 lg:grid-cols-2 gap-8 transition-all duration-300 ${
                        isReviewAnimating ? 'opacity-0 transform translate-x-4' : 'opacity-100 transform translate-x-0'
                    }`}>
                        {getCurrentReviews().map((review) => (
                            <div key={`${review.id}-${currentReviewIndex}`}
                                 className="bg-white rounded-lg p-8 shadow-sm hover:shadow-md transition-shadow">

                                <div className="flex mb-6">
                                    {[...Array(review.rating)].map((_, index) => (
                                        <svg key={index} className="w-5 h-5 text-yellow-400 fill-current"
                                             viewBox="0 0 20 20">
                                            <path
                                                d="M10 15l-5.878 3.09 1.123-6.545L.489 6.91l6.572-.955L10 0l2.939 5.955 6.572.955-4.756 4.635 1.123 6.545z"/>
                                        </svg>
                                    ))}
                                </div>
                                
                                <blockquote className="text-gray-700 text-lg leading-relaxed mb-6 italic">
                                    "{review.quote}"
                                </blockquote>

                                <div>
                                    <div className="font-bold text-gray-900 mb-1">
                                        {review.name}
                                    </div>
                                    <div className="text-gray-600 text-sm">
                                        {review.position}
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>

                    <div className="text-center mt-8">
                        <Button
                            classes="text-gray-600 hover:text-gray-700 bg-transparent hover:bg-gray-200 border-1 border-gray-600 hover:border-gray-700 px-6 py-2 font-semibold transition-all"
                            label="Zobacz więcej"
                            onClickAction={() => console.log('Zobacz więcej opinii')}
                        />
                    </div>
                </div>
            </section>

            <section className="py-16 px-4">
                <div className="max-w-4xl mx-auto">
                    <div className="text-center mb-12">
                        <h2 className="text-3xl lg:text-4xl font-bold text-gray-900 mb-4">
                            Skontaktuj się z nami
                        </h2>
                        <p className="text-lg text-gray-600">
                            Masz pytania? Chętnie na nie odpowiemy
                        </p>
                    </div>

                    <div className="p-8">
                        <FormBuilder
                            fields={contactFields}
                            onSubmit={handleContactSubmit}
                            validationSchema={contactValidationSchema}
                            numColumns={2}
                            gap={24}
                            rightButton={{
                                text: "Wyślij wiadomość",
                                color: "#16a34a",
                                onClick: () => {
                                }
                            }}
                        />
                    </div>
                </div>
            </section>

        </>
    );
}

export default HomePage
