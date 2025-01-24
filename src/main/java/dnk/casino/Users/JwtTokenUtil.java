package dnk.casino.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

public class JwtTokenUtil {
    private final static String secret = System.getenv().get("JWT_SECRET");

    private static Long expiration = Long.parseLong(System.getenv().get("JWT_EXPIRATION"), 10);

    // Método para generar el token JWT
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

    public static String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static String getRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    /**
     * Extrae el nombre de usuario del token JWT.
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