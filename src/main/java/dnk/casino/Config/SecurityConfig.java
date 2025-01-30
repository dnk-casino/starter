package dnk.casino.Config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import dnk.casino.Users.JwtAuthenticationFilter;

/**
 * Configuración de seguridad para la aplicación.
 * 
 * @author Danikileitor
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Filtro de autenticación JWT.
     */
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Codificador de contraseñas.
     * 
     * @return el codificador de contraseñas
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración de la cadena de filtros de seguridad.
     * 
     * @param http la configuración de seguridad HTTP
     * @return la cadena de filtros de seguridad
     * @throws Exception si ocurre un error en la configuración
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("Authorization", "Content-Type"));

        return http
                // Configuración de CSRF
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF
                // Permitimos todas las CORS
                .cors(Customizer.withDefaults())
                // Permitir todo básicamente
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/admin/api", "/admin/api/**", "/users", "/users/**")
                        .hasAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll())
                // Deshabilita el login basado en formulario por defecto
                .formLogin(login -> login.disable())
                // Deshabilita X-Frame-Options para que se pueda insertar en iframes
                .headers(headers -> headers.frameOptions(options -> options.disable()))
                // Añadir el filtro JWT
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // Build
                .build();
    }
}
