package com.club.config;
import com.club.entity.Usuario; import com.club.enumeration.Rol; import com.club.repository.UsuarioRepository; import lombok.RequiredArgsConstructor; import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration @RequiredArgsConstructor public class DataInitializer {
 private final UsuarioRepository repository; private final PasswordEncoder encoder;
 @Bean CommandLineRunner seedUsers(){return args->{if(repository.findByUsernameIgnoreCase("admin").isEmpty())repository.save(new Usuario("admin",encoder.encode("admin123"),Rol.ADMIN));if(repository.findByUsernameIgnoreCase("user").isEmpty())repository.save(new Usuario("user",encoder.encode("user123"),Rol.USER));};}
}
