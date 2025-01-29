package dnk.casino.Users;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dnk.casino.Users.Usuario.Rol;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de autenticación JWT que verifica y valida tokens en cada solicitud.
 * 
 * @author Danikileitor
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Procesa la solicitud y verifica el token JWT en el encabezado de
     * autorización.
     * 
     * @param request     la solicitud HTTP
     * @param response    la respuesta HTTP
     * @param filterChain la cadena de filtros
     * @throws ServletException si ocurre un error en el filtro
     * @throws IOException      si ocurre un error de E/S
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Obtener el encabezado de autorización de la solicitud
        String authHeader = request.getHeader("Authorization");

        // Verificar si el encabezado de autorización es válido y contiene un token JWT
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Quitar el prefijo "Bearer " del token
            String token = authHeader.substring(7);

            try {
                // Validar el token y extraer datos
                Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);
                Optional<Rol> roleOpt = JwtTokenUtil.extractRoleFromToken(token);

                // Verificar si el token es válido y no hay una autenticación existente en el
                // contexto de seguridad
                if (usernameOpt.isPresent() && roleOpt.isPresent()
                        && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Crear objeto de autenticación con el nombre de usuario y roles
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            usernameOpt.get(), // Nombre de usuario
                            null, // Credenciales (pueden ser null después de autenticación)
                            List.of(new SimpleGrantedAuthority(roleOpt.get().toString())) // Roles
                    );

                    // Configurar el contexto de seguridad con la autenticación
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Manejar el error de token inválido, si necesario
                System.out.println("Token JWT no válido: " + e.getMessage());
            }
        }

        // Continuar con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }
}
