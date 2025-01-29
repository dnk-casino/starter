package dnk.casino.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import dnk.casino.Users.Usuario.Rol;

/**
 * Utilidad para la generación y verificación de tokens JWT.
 * 
 * @author Danikileitor
 */
public class JwtTokenUtil {

    /**
     * Clave secreta para la firma de tokens JWT.
     */
    private final static String secret = System.getenv().get("JWT_SECRET");

    /**
     * Tiempo de expiración de los tokens JWT en milisegundos.
     */
    private static Long expiration = Long.parseLong(System.getenv().get("JWT_EXPIRATION"), 10);

    /**
     * Obtiene la clave de firma para los tokens JWT.
     * 
     * @return la clave de firma
     */
    private final static SecretKey getSignInKey() {
        byte[] bytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    /**
     * Genera un token JWT para un usuario.
     * 
     * @param usuario el usuario para el que se genera el token
     * @return el token JWT generado
     */
    public static String generateToken(Usuario usuario) {
        return Jwts.builder()
                .subject(usuario.getUsername()) // Añade el nombre del usuario
                .claim("role", usuario.getRol()) // Añade el rol del usuario
                .issuedAt(new Date()) // Hora actual como "emitido"
                .expiration(new Date(System.currentTimeMillis() + expiration)) // Tiempo de expiración
                .signWith(getSignInKey()) // Firma del token
                .compact();
    }

    /**
     * Extrae todas las reclamaciones de un token JWT.
     * 
     * @param token el token JWT
     * @return las reclamaciones
     */
    private static Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Obtiene el nombre de usuario desde un token JWT.
     * 
     * @param token el token JWT
     * @return el nombre de usuario
     */
    private static String getUsernameFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Obtiene el rol de usuario desde un token JWT.
     * 
     * @param token el token JWT
     * @return el rol de usuario
     */
    private static Rol getRoleFromToken(String token) {
        return extractAllClaims(token).get("role", Rol.class);
    }

    /**
     * Extrae el nombre de usuario de un token JWT.
     * 
     * @param token el token JWT
     * @return el nombre de usuario, si está presente
     */
    public static Optional<String> extractUsernameFromToken(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // Elimina el prefijo "Bearer "
            }

            return Optional.ofNullable(getUsernameFromToken(token));
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                System.out.println("El token ha expirado");
                return Optional.empty();
            } else {
                e.printStackTrace();
                return Optional.empty();
            }
        }
    }

    /**
     * Extrae el rol de usuario de un token JWT.
     * 
     * @param token el token JWT
     * @return el rol de usuario, si está presente
     */
    public static Optional<Rol> extractRoleFromToken(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7); // Elimina el prefijo "Bearer "
            }

            return Optional.ofNullable(getRoleFromToken(token));
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                System.out.println("El token ha expirado");
                return Optional.empty();
            } else {
                e.printStackTrace();
                return Optional.empty();
            }
        }
    }
}
