package com.furnish.erp.domain.enums;

/**
 * Roles de seguridad de la aplicación, usados por Spring Security para
 * autorizar el acceso a determinadas rutas (ver SecurityConfig).
 *
 *  ADMIN    -> acceso total: ABM de empleados, proveedores, productos,
 *              usuarios, órdenes de compra y stock.
 *  EMPLEADO -> operación diaria: registrar órdenes de compra, consultar
 *              stock y productos, pero sin poder administrar usuarios
 *              ni eliminar empleados/proveedores.
 *
 * Spring Security requiere que los roles se antepongan con el prefijo
 * "ROLE_" al guardarlos como GrantedAuthority; esa conversión se realiza
 * en UsuarioServiceImpl (implementación de UserDetailsService).
 */
public enum Rol {
    ADMIN,
    EMPLEADO
}
