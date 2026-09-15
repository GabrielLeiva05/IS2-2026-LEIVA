package com.nexora.gestion.controller;

import com.nexora.gestion.config.SesionConstantes;
import com.nexora.gestion.exception.RecursoNoEncontradoException;
import com.nexora.gestion.exception.StockInsuficienteException;
import com.nexora.gestion.model.Compra;
import com.nexora.gestion.model.Usuario;
import com.nexora.gestion.service.CompraService;
import com.nexora.gestion.service.ProductoService;
import com.nexora.gestion.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ============================================================================
 * "CompraController" (Capa de Controlador) — Carrito y compras del Usuario
 * ============================================================================
 * Rutas bajo "/compras/**" (protegidas: solo Usuario autenticado, ver
 * SesionInterceptor). Modela el flujo típico de "carrito de compras":
 *
 *   1) GET  /compras/carrito     -> obtiene (o crea) la Compra en borrador
 *   2) POST /compras/agregar     -> agrega un producto+cantidad (agregarDetalle)
 *   3) POST /compras/confirmar   -> cierra la compra (registrarCompra)
 *   4) GET  /compras/historial   -> lista las compras confirmadas del usuario
 *   5) POST /compras/{id}/anular -> anula una compra (anularCompra)
 *
 * El id de la Compra en borrador se guarda en HttpSession
 * (SESSION_CARRITO_COMPRA_ID) para simular un "carrito" sin necesidad de
 * volver a pasarlo en cada request. El Controller solo orquesta llamadas al
 * CompraService: ninguna cuenta ni validación de stock se hace acá.
 * ============================================================================
 */
@Controller
@RequestMapping("/compras")
public class CompraController {

    private static final Logger log = LoggerFactory.getLogger(CompraController.class);

    private final CompraService compraService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    public CompraController(CompraService compraService, ProductoService productoService, UsuarioService usuarioService) {
        this.compraService = compraService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/carrito")
    public String verCarrito(HttpSession session, Model model) {
        Compra compra = obtenerOCrearCarrito(session);
        model.addAttribute("compra", compra);
        model.addAttribute("productos", productoService.listarActivos());
        return "compras/carrito"; // -> templates/compras/carrito.html
    }

    @PostMapping("/agregar")
    public String agregarProducto(@RequestParam Long productoId,
                                   @RequestParam Integer cantidad,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Compra compra = obtenerOCrearCarrito(session);
        try {
            compraService.agregarDetalle(compra.getId(), productoId, cantidad);
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto agregado al carrito.");
        } catch (StockInsuficienteException | RecursoNoEncontradoException | IllegalArgumentException | IllegalStateException ex) {
            log.warn("No se pudo agregar el producto {} al carrito: {}", productoId, ex.getMessage());
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error al agregar el producto {} al carrito", productoId, ex);
            redirectAttributes.addFlashAttribute("mensajeError",
                    "No se pudo agregar el producto al carrito. Revisá el stock e intentá nuevamente.");
        }
        return "redirect:/compras/carrito";
    }

    @PostMapping("/confirmar")
    public String confirmar(HttpSession session, RedirectAttributes redirectAttributes) {
        Compra compra = obtenerOCrearCarrito(session);
        try {
            compraService.confirmarCompra(compra.getId());
            session.removeAttribute(SesionConstantes.SESSION_CARRITO_COMPRA_ID);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Compra registrada con éxito!");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/compras/historial";
    }

    @GetMapping("/historial")
    public String historial(HttpSession session, Model model) {
        Usuario usuario = usuarioActual(session);
        model.addAttribute("compras", compraService.historialDeUsuario(usuario));
        return "compras/historial"; // -> templates/compras/historial.html
    }

    @PostMapping("/{id}/anular")
    public String anular(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioActual(session);
            Compra compra = compraService.buscarPorId(id)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Compra no encontrada."));
            if (compra.getUsuario() == null || !compra.getUsuario().getId().equals(usuario.getId())) {
                throw new IllegalStateException("No podés anular una compra que no te pertenece.");
            }
            compraService.anularCompra(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Compra anulada. El stock fue repuesto.");
        } catch (RecursoNoEncontradoException | IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("mensajeError", ex.getMessage());
        }
        return "redirect:/compras/historial";
    }

    // ------------------------------------------------------------------
    // Helpers privados de esta capa (NO son reglas de negocio: solo
    // resuelven "quién es el usuario logueado" y "cuál es su carrito
    // actual", a partir de los datos guardados en sesión).
    // ------------------------------------------------------------------

    private Usuario usuarioActual(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute(SesionConstantes.SESSION_ID);
        return usuarioService.buscarPorId(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario en sesión no encontrado."));
    }

    private Compra obtenerOCrearCarrito(HttpSession session) {
        Long carritoId = (Long) session.getAttribute(SesionConstantes.SESSION_CARRITO_COMPRA_ID);
        if (carritoId != null) {
            var compraExistente = compraService.buscarPorId(carritoId);
            if (compraExistente.isPresent() && !Boolean.TRUE.equals(compraExistente.get().getAnulada())) {
                // Reutilizamos el carrito (compra en borrador) mientras siga existiendo.
                return compraExistente.get();
            }
        }
        Usuario usuario = usuarioActual(session);
        Compra nuevaCompra = compraService.iniciarCompra(usuario);
        session.setAttribute(SesionConstantes.SESSION_CARRITO_COMPRA_ID, nuevaCompra.getId());
        return nuevaCompra;
    }
}
