package com.learnhub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio de infraestructura para correo. La lógica de negocio no conoce
 * JavaMail: sólo publica el evento ProfesorRegistradoEvent.
 */
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String remitente;

    public void enviarBienvenida(String email, String nombre) {
        SimpleMailMessage message = new SimpleMailMessage();
        if (remitente != null && !remitente.isBlank()) {
            message.setFrom(remitente);
        }
        message.setTo(email);
        message.setSubject("Bienvenido/a a LearnHub");
        message.setText("Hola " + nombre + ",\n\n" +
                "Tu cuenta de profesor fue registrada correctamente en LearnHub.\n" +
                "Tu usuario es tu correo personal.\n\n" +
                "Por seguridad, nunca compartas tu contraseña.\n\n" +
                "Saludos,\nEquipo LearnHub");
        mailSender.send(message);
    }
}
