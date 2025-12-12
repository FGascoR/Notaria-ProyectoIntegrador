package com.example.ProyectoIntegrador.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'self'; " +
                                        "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://cdn.jsdelivr.net https://cdnjs.cloudflare.com; " +
                                        "style-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net https://cdnjs.cloudflare.com https://fonts.googleapis.com; " +
                                        "img-src 'self' data: https://cdn.jsdelivr.net https://cdnjs.cloudflare.com https://images.pexels.com https://upload.wikimedia.org; " +
                                        "font-src 'self' data: https://cdn.jsdelivr.net https://cdnjs.cloudflare.com https://fonts.gstatic.com; " +
                                        "connect-src 'self' https://cdn.jsdelivr.net https://cdnjs.cloudflare.com; " +
                                        "frame-ancestors 'self'; "
                        ))
                        .frameOptions(frame -> frame.sameOrigin())
                        .httpStrictTransportSecurity(hsts -> hsts.disable())
                        .cacheControl(cache -> cache.disable())
                        .addHeaderWriter((req, res) ->
                                res.setHeader("Permissions-Policy", "geolocation=(), microphone=(), camera=()"))
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/Login", "/login", "/registrarse", "/Registrarse",
                                "/Notaria", "/Pagina",
                                "/css/**", "/js/**", "/img/**",
                                "/uploads/**",
                                "/styleLogin.css", "/stylePagina.css", "/styleSistemaNotario.css"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/Login")
                        .loginProcessingUrl("/Login")
                        .successHandler(successHandler)
                        .failureUrl("/Login?error=true")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/Login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}
