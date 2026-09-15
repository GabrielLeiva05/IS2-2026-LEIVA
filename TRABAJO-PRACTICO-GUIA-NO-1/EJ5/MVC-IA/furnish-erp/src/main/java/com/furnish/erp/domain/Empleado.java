package com.furnish.erp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * Entidad JPA: Empleado
 * ============================================================================
 * Representa la clase "Empleado" del diagrama de clases de diseño.
 *
 * IMPORTANTE (regla de arquitectura MVC pedida por el cliente): esta clase es
 * PURAMENTE de persistencia (Modelo). Los métodos de negocio del diagrama
 * (crearOrdenCompra(), registrarEmpleado(), editarEmpleado(), eliminarEmpleado())
 * NO se implementan acá: viven en {@link com.furnish.erp.service.EmpleadoService}
 * y en {@link com.furnish.erp.service.OrdenCompraService}, que son las únicas
 * clases autorizadas a aplicar reglas de negocio.
 *
 * Anotaciones JPA/Hibernate utilizadas:
 *  - @Entity            : marca la clase como entidad persistente (tabla).
 *  - @Table             : nombre explícito de la tabla en MySQL.
 *  - @Id + @GeneratedValue(IDENTITY) : clave primaria autoincremental (equivalente
 *        al AUTO_INCREMENT de MySQL).
 *  - @Column            : restricciones de columna (nullable, length, unique).
 *  - @OneToMany(mappedBy) : lado "1" de la relación 1...* con OrdenCompra
 *        (un Empleado registra muchas Órdenes de Compra).
 *
 * Anotaciones Lombok (generan código repetitivo en tiempo de compilación):
 *  - @Getter/@Setter        : getters y setters de todos los atributos.
 *  - @NoArgsConstructor      : constructor vacío (requerido por JPA/Hibernate).
 *  - @AllArgsConstructor     : constructor con todos los campos.
 *  - @Builder                : patrón Builder (Empleado.builder()...build()).
 *  - @ToString(exclude=...)  : evita loops infinitos al imprimir colecciones.
 * ============================================================================
 */
@Entity
@Table(name = "empleados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "ordenesCompra")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Long idEmpleado;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120)
    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @NotNull(message = "El DNI es obligatorio")
    @Column(name = "dni", nullable = false, unique = true)
    private Integer dni;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @Column(name = "correo", nullable = false, length = 150)
    private String correo;

    @NotBlank(message = "El cargo es obligatorio")
    @Column(name = "cargo", nullable = false, length = 80)
    private String cargo;

    /** Baja lógica: en vez de borrar físicamente el registro (para no perder
     *  la trazabilidad de las órdenes históricas asociadas), se marca como
     *  eliminado = true y se excluye de los listados activos. */
    @Builder.Default
    @Column(name = "eliminado", nullable = false)
    private boolean eliminado = false;

    /** Lado inverso de la relación 1 Empleado -- * OrdenCompra del diagrama.
     *  mappedBy indica que la FK "empleado" vive en la tabla ordenes_compra. */
    @Builder.Default
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.PERSIST)
    private List<OrdenCompra> ordenesCompra = new ArrayList<>();
}
