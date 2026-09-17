package com.learnhub;

import com.learnhub.dto.CambiarPasswordDTO;
import com.learnhub.entity.Usuario;
import com.learnhub.exception.BusinessException;
import com.learnhub.repository.ProfesorRepository;
import com.learnhub.repository.RolRepository;
import com.learnhub.repository.UsuarioRepository;
import com.learnhub.service.ProfesorService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Pruebas unitarias de reglas críticas de negocio.
 * No requieren levantar MySQL ni Spring completo.
 */
class ProfesorServiceTest {

    @Test
    void noPermiteCambiarPasswordConPasswordActualIncorrecta() {
        UsuarioRepository usuarios = mock(UsuarioRepository.class);
        ProfesorRepository profesores = mock(ProfesorRepository.class);
        RolRepository roles = mock(RolRepository.class);
        ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

        Usuario usuario = new Usuario();
        usuario.setEmail("docente@mail.com");
        usuario.setPasswordHash(new BCryptPasswordEncoder().encode("correcta123"));

        when(usuarios.findByEmailIgnoreCase("docente@mail.com")).thenReturn(java.util.Optional.of(usuario));

        ProfesorService service = new ProfesorService(
                profesores, usuarios, roles, new BCryptPasswordEncoder(), publisher
        );

        assertThrows(BusinessException.class, () ->
                service.cambiarPassword(
                        "docente@mail.com",
                        new CambiarPasswordDTO("incorrecta", "nueva1234", "nueva1234")
                )
        );

        verify(usuarios, never()).save(any());
    }
}
