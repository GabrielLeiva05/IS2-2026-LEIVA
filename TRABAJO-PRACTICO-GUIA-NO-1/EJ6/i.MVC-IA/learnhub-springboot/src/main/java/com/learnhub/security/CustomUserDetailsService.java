package com.learnhub.security;

import com.learnhub.entity.Usuario;
import com.learnhub.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/** Adaptador entre Usuario JPA y el contrato UserDetails de Spring Security. */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(username.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Credenciales inválidas."));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPasswordHash())
                .disabled(!usuario.isHabilitado())
                .authorities(usuario.getRoles().stream()
                        .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                        .collect(Collectors.toSet()))
                .build();
    }
}
