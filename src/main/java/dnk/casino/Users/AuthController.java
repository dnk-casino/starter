package dnk.casino.Users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import dnk.casino.Mail.EmailService;
import dnk.casino.Users.Usuario.Rol;

/**
 * Controlador de autenticación para la API.
 * 
 * @author Danikileitor
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * Servicio de usuarios.
     */
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Servicio de correo electrónico.
     */
    @Autowired
    private EmailService emailService;

    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param registroRequest la solicitud de registro
     * @return el usuario registrado o un mensaje de error
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistroRequest registroRequest) {
        if (registroRequest.getRol() != null) {
            try {
                Usuario usuario = usuarioService.registrarUsuario(
                        registroRequest.getUsername(),
                        registroRequest.getPassword(),
                        registroRequest.getEmail(),
                        Rol.valueOf(registroRequest.getRol().toUpperCase()));
                return ResponseEntity.ok(usuario);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            try {
                Usuario usuario = usuarioService.registrarUsuario(
                        registroRequest.getUsername(),
                        registroRequest.getPassword(),
                        registroRequest.getEmail(),
                        Rol.ROLE_USER);
                return ResponseEntity.ok(usuario);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    /**
     * Inicia sesión un usuario en el sistema.
     * 
     * @param loginRequest la solicitud de login
     * @return el token de autenticación o un mensaje de error
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<Usuario> usuario = usuarioService.login(
                loginRequest.getUsername(),
                loginRequest.getPassword());
        if (usuario.isPresent()) {
            String token = JwtTokenUtil.generateToken(usuario.get());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    usuario.get(), // Principal (Usuario autenticado)
                    null, // Credenciales (pueden ser null después de autenticación)
                    List.of(new SimpleGrantedAuthority(usuario.get().getRol().name())) // Roles/Authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Si es su primer login del día damos una recompensa
            if (usuario.get().isFirstLoginOfDay()) {
                usuario.get().setCoins(usuario.get().getCoins() + 20);
                usuarioService.updateUser(usuario.get().getId(), usuario.get());
            }
            usuarioService.updateUserLoginDate(usuario.get().getId());

            return ResponseEntity.ok(token); // Devuelve el token como texto plano
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
    }

    /**
     * Inicia sesión un administrador en el sistema.
     * 
     * @param loginRequest la solicitud de login
     * @return el token de autenticación o un mensaje de error
     */
    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody LoginRequest loginRequest) {
        Optional<Usuario> usuario = usuarioService.login(
                loginRequest.getUsername(),
                loginRequest.getPassword());
        if (usuario.isPresent() && usuario.get().getRol() == Rol.ROLE_ADMIN) {
            String token = JwtTokenUtil.generateToken(usuario.get());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    usuario.get(), // Principal (Usuario autenticado)
                    null, // Credenciales (pueden ser null después de autenticación)
                    List.of(new SimpleGrantedAuthority(usuario.get().getRol().name())) // Roles/Authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok(token); // Devuelve el token como texto plano
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Acceso denegado. Solo los administradores pueden acceder.");
    }

    /**
     * Envía un correo electrónico para restablecer la contraseña de un usuario.
     * 
     * @param emailRequest la solicitud de restablecimiento de contraseña
     * @return un mensaje de éxito o error
     */
    @PostMapping("/olvidar-contrasena")
    public ResponseEntity<?> olvidarContrasena(@RequestBody EmailRequest emailRequest) {
        String email = emailRequest.getEmail();
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            // Generar token de restablecimiento de contraseña
            String token = UUID.randomUUID().toString();
            usuarioService.guardarTokenRestablecimientoContrasena(usuarioOpt.get(), token);

            // Enviar correo electrónico con el enlace de restablecimiento
            String mensaje = System.getenv().get("WEB_HOST") + "/restablecer-contrasena?token=" + token;
            emailService.enviarCorreo(email, "Restablecimiento de contraseña", mensaje);

            return ResponseEntity.ok("Correo electrónico enviado con éxito");
        } else {
            return ResponseEntity.badRequest().body("Correo electrónico no encontrado");
        }
    }

    /**
     * Restablece la contraseña de un usuario.
     * 
     * @param token                  la solicitud de restablecimiento de contraseña
     * @param nuevaContrasenaRequest la nueva contraseña
     * @return un mensaje de éxito o error
     */
    @PostMapping("/restablecer-contrasena/{token}")
    public ResponseEntity<?> restablecerContrasena(@PathVariable String token,
            @RequestBody NuevaContrasenaRequest nuevaContrasenaRequest) {
        Optional<Usuario> usuarioOpt = usuarioService.findByTokenRestablecimientoContrasena(token);
        if (usuarioOpt.isPresent()) {
            usuarioService.actualizarContrasena(usuarioOpt.get(), nuevaContrasenaRequest.getNuevaContrasena());
            return ResponseEntity.ok("Contraseña restablecida con éxito");
        } else {
            return ResponseEntity.badRequest().body("Token inválido");
        }
    }
}

/**
 * Solicitud de registro de un usuario.
 */
class RegistroRequest {
    private String username;
    private String password;
    private String email;
    private String rol;

    /**
     * Obtiene el nombre de usuario.
     * 
     * @return el nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     * 
     * @param username el nombre de usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña.
     * 
     * @return la contraseña
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña.
     * 
     * @param password la contraseña
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el correo electrónico.
     * 
     * @return el correo electrónico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico.
     * 
     * @param email el correo electrónico
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el rol.
     * 
     * @return el rol
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol.
     * 
     * @param rol el rol
     */
    public void setRol(String rol) {
        this.rol = rol;
    }
}

/**
 * Solicitud de login de un usuario.
 */
class LoginRequest {
    private String username;
    private String password;

    /**
     * Obtiene el nombre de usuario.
     * 
     * @return el nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     * 
     * @param username el nombre de usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña.
     * 
     * @return la contraseña
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña.
     * 
     * @param password la contraseña
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

/**
 * Solicitud de restablecimiento de contraseña.
 */
class EmailRequest {
    private String email;

    /**
     * Obtiene el correo electrónico.
     * 
     * @return el correo electrónico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico.
     * 
     * @param email el correo electrónico
     */
    public void setEmail(String email) {
        this.email = email;
    }
}

/**
 * Solicitud de nueva contraseña.
 */
class NuevaContrasenaRequest {
    private String nuevaContrasena;

    /**
     * Obtiene la nueva contraseña.
     * 
     * @return la nueva contraseña
     */
    public String getNuevaContrasena() {
        return nuevaContrasena;
    }

    /**
     * Establece la nueva contraseña.
     * 
     * @param nuevaContrasena la nueva contraseña
     */
    public void setNuevaContrasena(String nuevaContrasena) {
        this.nuevaContrasena = nuevaContrasena;
    }
}
