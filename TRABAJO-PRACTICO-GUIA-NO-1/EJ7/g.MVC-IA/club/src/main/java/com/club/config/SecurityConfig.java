package com.club.config;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration @EnableMethodSecurity
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder(12);}
 @Bean SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{return http
  .authorizeHttpRequests(a->a
   .requestMatchers("/css/**","/login","/error").permitAll()
   .requestMatchers("/auditoria/**").hasRole("ADMIN")
   .requestMatchers(HttpMethod.GET,"/","/socios/**","/familias/**","/pagos/**","/accesos/**","/personas/**","/api/**").authenticated()
   .requestMatchers("/socios/**","/familias/**","/pagos/**","/accesos/**","/api/**").hasRole("ADMIN")
   .anyRequest().authenticated())
  .formLogin(f->f.loginPage("/login").defaultSuccessUrl("/",true).permitAll())
  .logout(l->l.logoutSuccessUrl("/login?logout").permitAll())
  .csrf(c->c.ignoringRequestMatchers("/api/**"))
  .build();}
}
