package com.learnhub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Envía el correo solamente después de que la transacción de alta terminó
 * correctamente. Así no se envían bienvenidas de profesores que finalmente
 * no quedaron persistidos.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WelcomeEmailListener {
    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProfesorRegistrado(ProfesorRegistradoEvent event) {
        try {
            emailService.enviarBienvenida(event.email(), event.nombre());
        } catch (RuntimeException ex) {
            // El alta ya fue confirmada. Un fallo SMTP no debe hacer parecer
            // fallido un registro que la base de datos confirmó correctamente.
            log.error("No fue posible enviar el correo de bienvenida a {}", event.email(), ex);
        }
    }
}
