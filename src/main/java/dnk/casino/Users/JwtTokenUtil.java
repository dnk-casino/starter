package dnk.casino.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

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
     * Genera un token JWT para un usuario.
     * 
     * @param usuario el usuario para el que se genera el token
     * @return el token JWT generado
     */
    public static String generateToken(Usuario usuario) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setSubject(usuario.getUsername()) // Añade el nombre del usuario
                .claim("role", usuario.getRol().name()) // Añade el rol del usuario
                .setIssuedAt(new Date()) // Hora actual como "emitido"
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Tiempo de expiración
                .signWith(key, SignatureAlgorithm.HS256) // Firma del token
                .compact();
    }

    /**
     * Obtiene el nombre de usuario desde un token JWT.
     * 
     * @param token el token JWT
     * @return el nombre de usuario
     */
    public static String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Obtiene el rol de usuario desde un token JWT.
     * 
     * @param token el token JWT
     * @return el rol de usuario
     */
    public static String getRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
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

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return Optional.ofNullable(claims.getSubject()); // Generalmente, el username está en 'sub'
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
