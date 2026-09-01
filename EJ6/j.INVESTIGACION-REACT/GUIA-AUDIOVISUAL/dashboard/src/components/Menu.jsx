import logo from '../assets/img/logo.png';

function Menu() {
    return (
        <>
            <header className="menu-wrap">
                <figure className="user">
                    <div className="user-avatar">
                        <img  className="w-100" src={logo} alt="Cedavilu Web Academy" />
                    </div>
                    <figcaption>
                        Cedavilu Web Academy
                    </figcaption>
                </figure>
                <nav>
                    <section className="menu">
                        <h3>Opciones</h3>
                        <ul>
                            <li>
                                <a href="#">
                                    <i className="bi bi-building" style={{fontSize: "1.2rem", color: "cornflowerblue"}}></i>
                                    - Empresas
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <i className="bi bi-person" style={{fontSize: "1.2rem", color: "cornflowerblue"}}></i>
                                    - Personas
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <i className="bi bi-list-check"></i>
                                    - Profesiones
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <i className="bi bi-person-vcard" style={{fontSize: "1.2rem", color: "cornflowerblue"}}></i>
                                    - Postulate aquí
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <i className="bi bi-chat-left-text"></i>
                                    - Contacto
                                </a>
                            </li>
                        </ul>
                    </section>
                </nav>
            </header>
        </>
    )
}

export default Menu;