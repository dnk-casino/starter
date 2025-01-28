package dnk.casino.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
        // http.authorizeHttpRequests((auth) -> auth.anyRequest().permitAll());

        http
                // Configuración de CSRF
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF
                // Permitir todo básicamente
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/admin/api", "/admin/api/**", "/users", "/users/**")
                        .hasAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll())
                // Deshabilita el login basado en formulario por defecto
                .formLogin(login -> login.disable())
                // Deshabilita X-Frame-Options para que se pueda insertar en iframes
                .headers(headers -> headers.frameOptions(options -> options.disable()));

        // Añadir el filtro JWT
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}
