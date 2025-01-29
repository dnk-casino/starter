package dnk.casino.Admin;

import com.fasterxml.jackson.annotation.JsonProperty;

import dnk.casino.Skins.Skin;
import dnk.casino.Skins.SkinService;
import dnk.casino.Users.Usuario;
import dnk.casino.Users.Usuario.Rol;
import dnk.casino.Users.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controlador de administración para la API.
 * 
 * @author Danikileitor
 */
@RestController
@RequestMapping("/admin/api")
public class AdminController {

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
     * Obtiene una lista de todos los usuarios en el sistema.
     * 
     * @return lista de usuarios
     */
    @PostMapping("/users")
    public List<Usuario> getAllUsers() {
        return usuarioService.getAllUsers();
    }

    /**
     * Actualiza la información de un usuario.
     * 
     * @param id      el ID del usuario a actualizar
     * @param request la solicitud de actualización
     * @return el usuario actualizado
     */
    @PutMapping("/users/{id}")
    public Usuario updateUser(@PathVariable String id, @RequestBody UsuarioRequest request) {
        Usuario updatedUser = new Usuario();
        updatedUser.setRol(request.getRol());
        updatedUser.setSkins(Stream.of(request.getSkins())
                .map(skinId -> {
                    Optional<Skin> skinOpt = skinService.findById(skinId);
                    if (skinOpt.isPresent()) {
                        return skinOpt.get().getId();
                    } else {
                        return null;
                    }
                })
                .collect(Collectors.toSet()));
        updatedUser.setCoins(request.getCoins());
        return usuarioService.updateUser(id, updatedUser);
    }

    /**
     * Elimina un usuario del sistema.
     * 
     * @param id el ID del usuario a eliminar
     * @return una respuesta HTTP con el resultado de la operación
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (usuarioService.deleteUser(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Crea una nueva skin en el sistema.
     * 
     * @param skin la skin a crear
     * @return la skin creada
     */
    @PostMapping(value = "/skins/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Skin> createSkin(@RequestBody Skin skin) {
        System.out.println(skin);
        Skin newSkin = skinService.createSkin(skin);
        System.out.println(newSkin);
        return ResponseEntity.ok(newSkin);
    }

    /**
     * Actualiza una skin existente en el sistema.
     * 
     * @param id      el ID de la skin a actualizar
     * @param request la solicitud de actualización
     * @return la skin actualizada
     */
    @PutMapping("/skins/{id}")
    public Skin updateSkin(@PathVariable String id, @RequestBody SkinRequest request) {
        Skin updatedSkin = new Skin();
        updatedSkin.setName(request.getNombre());
        updatedSkin.setPrecio(request.getPrecio());
        updatedSkin.setDescription(request.getDescription());
        updatedSkin.setReels(request.getReels());
        updatedSkin.setVendible(request.isVendible());
        return skinService.updateSkin(id, updatedSkin);
    }

    /**
     * Elimina una skin del sistema.
     * 
     * @param id el ID de la skin a eliminar
     * @return una respuesta HTTP con el resultado de la operación
     */
    @DeleteMapping("/skins/{id}")
    public ResponseEntity<Void> deleteSkin(@PathVariable String id) {
        if (skinService.deleteSkin(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Solicitud de actualización de usuario.
     */
    public static class UsuarioRequest {
        /**
         * Rol del usuario.
         */
        @JsonProperty("rol")
        private Rol rol;
        /**
         * Monedas del usuario.
         */
        @JsonProperty("coins")
        private int coins;
        /**
         * IDs de las skins del usuario.
         */
        @JsonProperty("skinsId")
        private String[] skinsId;

        /**
         * Obtiene el rol del usuario.
         * 
         * @return el rol del usuario
         */
        public Rol getRol() {
            return rol;
        }

        /**
         * Establece el rol del usuario.
         * 
         * @param rol el rol del usuario
         */
        public void setRol(Rol rol) {
            this.rol = rol;
        }

        /**
         * Obtiene las monedas del usuario.
         * 
         * @return las monedas del usuario
         */
        public int getCoins() {
            return coins;
        }

        /**
         * Establece las monedas del usuario.
         * 
         * @param coins las monedas del usuario
         */
        public void setCoins(int coins) {
            this.coins = coins;
        }

        /**
         * Obtiene los IDs de las skins del usuario.
         * 
         * @return los IDs de las skins del usuario
         */
        public String[] getSkins() {
            return skinsId;
        }

        /**
         * Establece los IDs de las skins del usuario.
         * 
         * @param skinsId los IDs de las skins del usuario
         */
        public void setSkins(String[] skinsId) {
            this.skinsId = skinsId;
        }
    }

    /**
     * Solicitud de actualización de skin.
     */
    public static class SkinRequest {
        /**
         * Nombre de la skin.
         */
        @JsonProperty("nombre")
        private String nombre;
        /**
         * Precio de la skin.
         */
        @JsonProperty("precio")
        private int precio;
        /**
         * Descripción de la skin.
         */
        @JsonProperty("description")
        private String description;
        /**
         * Reels de la skin.
         */
        @JsonProperty("reels")
        private String[] reels;
        /**
         * Indica si la skin es vendible.
         */
        @JsonProperty("vendible")
        private boolean vendible;

        /**
         * Obtiene el nombre de la skin.
         * 
         * @return el nombre de la skin
         */
        public String getNombre() {
            return nombre;
        }

        /**
         * Establece el nombre de la skin.
         * 
         * @param nombre el nombre de la skin
         */
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        /**
         * Obtiene el precio de la skin.
         * 
         * @return el precio de la skin
         */
        public int getPrecio() {
            return precio;
        }

        /**
         * Establece el precio de la skin.
         * 
         * @param precio el precio de la skin
         */
        public void setPrecio(int precio) {
            this.precio = precio;
        }

        /**
         * Obtiene la descripción de la skin.
         * 
         * @return la descripción de la skin
         */
        public String getDescription() {
            return description;
        }

        /**
         * Establece la descripción de la skin.
         * 
         * @param description la descripción de la skin
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * Obtiene los reels de la skin.
         * 
         * @return los reels de la skin
         */
        public String[] getReels() {
            return reels;
        }

        /**
         * Establece los reels de la skin.
         * 
         * @param reels los reels de la skin
         */
        public void setReels(String[] reels) {
            this.reels = reels;
        }

        /**
         * Indica si la skin es vendible.
         * 
         * @return true si la skin es vendible, false en caso contrario
         */
        public boolean isVendible() {
            return vendible;
        }

        /**
         * Establece si la skin es vendible.
         * 
         * @param vendible true si la skin es vendible, false en caso contrario
         */
        public void setVendible(boolean vendible) {
            this.vendible = vendible;
        }
    }
}
