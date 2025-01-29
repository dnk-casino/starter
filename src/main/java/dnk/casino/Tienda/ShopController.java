package dnk.casino.Tienda;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import dnk.casino.Skins.Skin;
import dnk.casino.Skins.SkinService;
import dnk.casino.Users.JwtTokenUtil;
import dnk.casino.Users.Usuario;
import dnk.casino.Users.UsuarioService;

/**
 * Controlador de la tienda del casino.
 * 
 * @author Danikileitor
 */
@RestController
@RequestMapping("/shop/api")
public class ShopController {

    /**
     * Servicio de usuarios.
     */
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Servicio de skins.
     */
    @Autowired
    private SkinService skinService;

    /**
     * Compra una skin para un usuario.
     * 
     * @param token   el token de autenticación
     * @param request la solicitud de compra de skin
     * @return la respuesta de la compra
     */
    @PostMapping("/comprar/skin")
    public ResponseEntity<?> buySkin(@RequestHeader("Authorization") String token,
            @RequestBody BuySkinRequest request) {

        // Extrae el nombre de usuario del token
        Optional<String> usernameOpt = JwtTokenUtil.extractUsernameFromToken(token);

        if (usernameOpt.isPresent()) {
            // Busca el usuario por su nombre
            Optional<Usuario> usuarioOpt = usuarioService.findByUsername(usernameOpt.get());
            if (usuarioOpt.isPresent()) {
                // Busca la skin por su nombre
                Optional<Skin> skinOpt = skinService.findByName(request.getName());
                if (skinOpt.isPresent()) {
                    // Verifica si el usuario tiene suficientes monedas para comprar la skin
                    if (usuarioOpt.get().getCoins() >= skinOpt.get().getPrecio()) {
                        // Actualiza las monedas del usuario
                        int newCoins = usuarioOpt.get().getCoins() - skinOpt.get().getPrecio();
                        usuarioOpt.get().setCoins(newCoins);
                        // Desbloquea la skin para el usuario
                        if (usuarioOpt.get().desbloquearSkin(skinOpt.get().getId())) {
                            // Actualiza el usuario en la base de datos
                            usuarioService.updateUser(usuarioOpt.get().getId(), usuarioOpt.get());
                            return ResponseEntity.ok("Has desbloqueado la skin: " + skinOpt.get().getName());
                        } else {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body("Ya tienes la skin: " + request.getName());
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No tienes suficientes monedas");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("No existe la skin: " + request.getName());
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
    }

    /**
     * Solicitud de compra de skin.
     */
    public static class BuySkinRequest {
        /**
         * Nombre de la skin a comprar.
         */
        @JsonProperty("name")
        private String name;

        /**
         * Obtiene el nombre de la skin.
         * 
         * @return el nombre de la skin
         */
        public String getName() {
            return name;
        }

        /**
         * Establece el nombre de la skin.
         * 
         * @param name el nombre de la skin
         */
        public void setName(String name) {
            this.name = name;
        }
    }
}
