document.addEventListener('DOMContentLoaded', () => {
    
    const formulario = document.querySelector('.formulario');
    const respuesta = document.getElementById('parrafo');

    respuesta.style.display = 'none';

    formulario.addEventListener('submit', (e) => {
        e.preventDefault();

        const nombre = document.getElementById('nombre').value;
        const password = document.getElementById('password').value;

        if (nombre === '' || password === '') {
            respuesta.style.display = 'block';
            respuesta.textContent = 'Todos los campos son obligatorios.';
            respuesta.style.backgroundColor = 'red';
        } else {
            respuesta.style.display = 'block';
            respuesta.textContent = 'Formulario enviado correctamente';
            respuesta.style.backgroundColor = 'green';
        }
    })
})