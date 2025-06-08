import {contactInfo, footerSections} from "./FooterItems";
import FooterItem from "./FooterItem";
import PhoneIcon from "../../../assets/phone.svg?react";
import MailIcon from "../../../assets/mail.svg?react";
import MapPinIcon from "../../../assets/map-pin.svg?react";
import Logo from "../../../assets/logo_white.svg?react";
import Icon from "../icons/Icon";

const Footer = () => {
    return (
        <footer className="bg-gray-800 text-white pt-12 pb-8">
            <div className="container mx-auto px-6">
                <div className="grid grid-cols-1 md:grid-cols-4 gap-8">

                    <div className="col-span-1">
                        <div className="mb-4">
                            <Icon
                                icon={Logo}
                                iconProps={{
                                    className: "text-red-400",
                                    strokeWidth: 2
                                }}
                            />
                            <p className="text-gray-300 text-sm leading-relaxed">
                                Catering z maksymalną dbałością o składniki
                            </p>
                        </div>
                    </div>

                    <div className="col-span-1">
                        <h4 className="text-lg font-semibold mb-4 text-white">
                            O nas
                        </h4>
                        <div className="space-y-1">
                            {footerSections.aboutUs.map((item) => (
                                <FooterItem
                                    key={item.href}
                                    href={item.href}
                                    label={item.label}
                                />
                            ))}
                        </div>
                    </div>

                    <div className="col-span-1">
                        <h4 className="text-lg font-semibold mb-4 text-white">
                            Dla klientów
                        </h4>
                        <div className="space-y-1">
                            {footerSections.forClients.map((item) => (
                                <FooterItem
                                    key={item.href}
                                    href={item.href}
                                    label={item.label}
                                />
                            ))}
                        </div>
                    </div>

                    <div className="col-span-1">
                        <h4 className="text-lg font-semibold mb-4 text-white">
                            Kontakt
                        </h4>

                        <div className="space-y-3">
                            <div className="text-gray-300 text-sm flex items-center gap-3">
                                <div className="flex items-center justify-center w-6 h-6 rounded-full bg-blue-600/20">
                                    <Icon
                                        icon={PhoneIcon}
                                        iconProps={{
                                            className: "text-blue-400 w-5 h-5",
                                            strokeWidth: 2
                                        }}
                                    />
                                </div>
                                <span>{contactInfo.phone}</span>
                            </div>
                            <div className="text-gray-300 text-sm flex items-center gap-3">
                                <div className="flex items-center justify-center w-6 h-6 rounded-full bg-green-600/20">
                                    <Icon
                                        icon={MailIcon}
                                        iconProps={{
                                            className: "text-green-400 w-5 h-5",
                                            strokeWidth: 2
                                        }}
                                    />
                                </div>
                                <span>{contactInfo.email}</span>
                            </div>
                            <div className="text-gray-300 text-sm flex items-center gap-3">
                                <div className="flex items-center justify-center w-6 h-6 rounded-full bg-red-600/20">
                                    <Icon
                                        icon={MapPinIcon}
                                        iconProps={{
                                            className: "text-red-400 w-5 h-5",
                                            strokeWidth: 2
                                        }}
                                    />
                                </div>
                                <span>{contactInfo.address}</span>
                            </div>
                        </div>
                    </div>
                </div>

                <hr className="my-8 border-gray-600"/>

                <div className="flex flex-col md:flex-row justify-between items-center">
                    <div className="text-gray-400 text-sm mb-4 md:mb-0">
                        © 2025 Broccoflower. Wszelkie prawa zastrzeżone.
                    </div>
                    <div className="flex space-x-4">
                        <a href="/privacy" className="text-gray-400 hover:text-white text-sm transition-colors">
                            Polityka prywatności
                        </a>
                        <a href="/terms" className="text-gray-400 hover:text-white text-sm transition-colors">
                            Regulamin
                        </a>
                        <a href="/cookies" className="text-gray-400 hover:text-white text-sm transition-colors">
                            Cookies
                        </a>
                    </div>
                </div>
            </div>
        </footer>
    );
}

export default Footer;
