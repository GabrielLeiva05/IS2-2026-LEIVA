package com.nexora.gestion.service;

import com.nexora.gestion.model.Administrador;

/**
 * Contrato de reglas de negocio relacionadas con Administrador:
 * autenticación y las acciones administrativas de alto nivel del diagrama
 * UML (desbloquearUsuario, gestionarUsuario -> expresado acá como
 * delegación hacia UsuarioService/ProductoService desde el Controller,
 * habilitada por el rol de Administrador).
 */
public interface AdministradorService {

    Administrador registrar(Administrador administrador);

    /** Autentica a un administrador por correo/password (sin la lógica de
     *  bloqueo por intentos que sí tiene Usuario, dado que el diagrama UML
     *  no define esos atributos para Administrador). */
    Administrador iniciarSesion(String correo, String password);

    /** Regla de negocio "desbloquearUsuario()" del diagrama UML: solo un
     *  Administrador autenticado puede ejecutar esta acción (se valida en
     *  el Controller antes de invocar este método, verificando la sesión). */
    void desbloquearUsuario(Long administradorId, Long usuarioId);
}
