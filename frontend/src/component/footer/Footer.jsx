import { FaGithub } from "react-icons/fa";
import { FaInstagram } from "react-icons/fa";
import logoImage from "../../assets/MHO-NoBg.png";

const Footer = () => {
    return(
        <footer className="bg-gray-900 text-white py-8 mt-auto">
            <div className="container mx-auto px-4">
                <div className="flex flex-col space-y-6">
                    <div className="flex flex-col sm:flex-row items-center justify-between space-y-4 sm:space-y-0">
                        <div className="flex items-center">
                            <a href="#top" className="cursor-pointer">
                                <img 
                                    src={logoImage} 
                                    alt="MHO-LOGO" 
                                    className="h-16 w-auto hover:opacity-80 transition-opacity duration-200"
                                />
                            </a>
                        </div>
                        
                        <div className="flex space-x-4">
                            <a 
                                href="https://github.com/Mouayad91" 
                                className="text-gray-400 hover:text-white transition-colors duration-200 text-2xl"
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                <FaGithub />
                            </a>
                            <a 
                                href="https://www.instagram.com/mouayad91/?next=%2F&hl=en" 
                                className="text-gray-400 hover:text-white transition-colors duration-200 text-2xl"
                                target="_blank"
                                rel="noopener noreferrer"
                            >
                                <FaInstagram />
                            </a>
                        </div>
                    </div>
                    
                    <div className="text-center border-t border-gray-700 pt-4">
                        <p className="text-gray-400 text-sm">
                            © MHO TOYS All right reserved
                        </p>
                    </div>
                </div>
            </div>
        </footer>
    )
}

export default Footer;