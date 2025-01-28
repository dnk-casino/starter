package dnk.casino.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dnk.casino.Skins.Skin;
import dnk.casino.Skins.SkinRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Servicio para la gestión de usuarios.
 * 
 * @author Danikileitor
 */
@Service
public class UsuarioService {

    /**
     * Repositorio de usuarios.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Repositorio de skins.
     */
    @Autowired
    private SkinRepository skinRepository;

    /**
     * Codificador de contraseñas.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param username nombre de usuario
     * @param password contraseña
     * @param email    correo electrónico
     * @param rol      rol del usuario
     * @return el usuario registrado
     */
    public Usuario registrarUsuario(String username, String password, String email, Usuario.Rol rol) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        Usuario usuario = new Usuario(username, passwordEncoder.encode(password), email);
        usuario.setRol(rol);
        usuario.desbloquearSkin(skinRepository.findByName("Comida Basura").get().getId());

        return usuarioRepository.save(usuario);
    }

    /**
     * Inicia sesión un usuario en el sistema.
     * 
     * @param username nombre de usuario
     * @param password contraseña
     * @return el usuario logueado, o un Optional vacío si no se loguea con éxito
     */
    public Optional<Usuario> login(String username, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
        if (usuario.isPresent() && passwordEncoder.matches(password, usuario.get().getPassword())) {
            return usuario;
        }
        return Optional.empty();
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param username nombre de usuario a buscar
     * @return el usuario encontrado, o un Optional vacío si no se encuentra
     */
    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    /**
     * Busca un usuario por su correo electrónico.
     * 
     * @param email correo electrónico a buscar
     * @return el usuario encontrado, o un Optional vacío si no se encuentra
     */
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Obtiene una lista de todos los usuarios en el sistema.
     * 
     * @return lista de usuarios
     */
    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    /**
     * Obtiene un conjunto de skins a partir de una lista de IDs de skins.
     * 
     * @param skinsId lista de IDs de skins
     * @return conjunto de skins
     */
    public Set<Skin> getSkins(String[] skinsId) {
        return Stream.of(skinsId)
                .map(skinId -> {
                    return skinRepository.findById(skinId).get();
                })
                .collect(Collectors.toSet());
    }

    /**
     * Actualiza la información de un usuario.
     * 
     * @param id          ID del usuario a actualizar
     * @param updatedUser información actualizada del usuario
     * @return el usuario actualizado
     */
    public Usuario updateUser(String id, Usuario updatedUser) {
        Usuario user = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!updatedUser.getRol().equals(null)) {
            user.setRol(updatedUser.getRol());
        }
        if (updatedUser.getSkins().size() > 0) {
            user.setSkins(updatedUser.getSkins());
        }
        if (updatedUser.getCoins() >= 0) {
            user.setCoins(updatedUser.getCoins());
        }
        return usuarioRepository.save(user);
    }

    /**
     * Actualiza la fecha de último inicio de sesión de un usuario.
     * 
     * @param id ID del usuario a actualizar
     * @return el usuario actualizado
     */
    public Usuario updateUserLoginDate(String id) {
        Usuario user = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setLastLoginDate(new Date());
        return usuarioRepository.save(user);
    }

    /**
     * Elimina un usuario del sistema.
     * 
     * @param id ID del usuario a eliminar
     * @return true si el usuario se elimina con éxito, false en caso contrario
     */
    public boolean deleteUser(String id) {
        Optional<Usuario> user = usuarioRepository.findById(id);
        if (user.isPresent()) {
            usuarioRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Reduce una cantidad de monedas a un usuario.
     * 
     * @param id    ID del usuario a pagar
     * @param coins cantidad de monedas a pagar
     * @return el usuario actualizado
     */
    public Usuario pagar(String id, int coins) {
        Usuario user = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.pagar(coins);
        return usuarioRepository.save(user);
    }

    /**
     * Aumenta una cantidad de monedas a un usuario.
     * 
     * @param id    ID del usuario a cobrar
     * @param coins cantidad de monedas a cobrar
     * @return el usuario actualizado
     */
    public Usuario cobrar(String id, int coins) {
        Usuario user = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.cobrar(coins);
        return usuarioRepository.save(user);
    }

    /**
     * Obtiene el número de victorias de un usuario.
     * 
     * @param id ID del usuario
     * @return número de victorias
     */
    public int getWins(String id) {
        Usuario user = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return user.getWins();
    }

    /**
     * Obtiene el número de victorias en blackjack de un usuario.
     * 
     * @param id ID del usuario
     * @return número de victorias en blackjack
     */
    public int getBjwins(String id) {
        Usuario user = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return user.getBjwins();
    }

    /**
     * Registra una victoria para un usuario.
     * 
     * @param id ID del usuario
     * @return el usuario actualizado
     */
    public Usuario victoria(String id) {
        Usuario user = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.victoria();
        return usuarioRepository.save(user);
    }

    /**
     * Registra una victoria en blackjack para un usuario.
     * 
     * @param id ID del usuario
     * @return el usuario actualizado
     */
    public Usuario bjvictoria(String id) {
        Usuario user = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.bjvictoria();
        return usuarioRepository.save(user);
    }

    /**
     * Obtiene la lista de los 5 usuarios con más victorias.
     * 
     * @return lista de usuarios
     */
    public List<Usuario> getTop5Winners() {
        List<Usuario> usuarios = getAllUsers();

        usuarios.sort((u1, u2) -> {
            long wins1 = getWins(u1.getId());
            long wins2 = getWins(u2.getId());
            return Long.compare(wins2, wins1);
        });

        return usuarios.subList(0, Math.min(5, usuarios.size()));
    }

    /**
     * Obtiene la lista de los 5 usuarios con más victorias en blackjack.
     * 
     * @return lista de usuarios
     */
    public List<Usuario> getTop5BJWinners() {
        List<Usuario> usuarios = getAllUsers();

        usuarios.sort((u1, u2) -> {
            long wins1 = getBjwins(u1.getId());
            long wins2 = getBjwins(u2.getId());
            return Long.compare(wins2, wins1);
        });

        return usuarios.subList(0, Math.min(5, usuarios.size()));
    }

    /**
     * Guarda un token de restablecimiento de contraseña para un usuario.
     * 
     * @param usuario usuario al que se le guarda el token
     * @param token   token de restablecimiento de contraseña
     */
    public void guardarTokenRestablecimientoContrasena(Usuario usuario, String token) {
        usuario.setTokenRestablecimientoContrasena(token);
        usuario.setFechaExpiracionTokenRestablecimientoContrasena(LocalDateTime.now().plusHours(1)); // Token válido por
                                                                                                     // 1 hora
        usuarioRepository.save(usuario);
    }

    /**
     * Busca un usuario por su token de restablecimiento de contraseña.
     * 
     * @param token token de restablecimiento de contraseña a buscar
     * @return el usuario encontrado, o un Optional vacío si no se encuentra
     */
    public Optional<Usuario> findByTokenRestablecimientoContrasena(String token) {
        return usuarioRepository.findByTokenRestablecimientoContrasena(token);
    }

    /**
     * Actualiza la contraseña de un usuario.
     * 
     * @param usuario         usuario al que se le actualiza la contraseña
     * @param nuevaContrasena nueva contraseña
     */
    public void actualizarContrasena(Usuario usuario, String nuevaContrasena) {
        usuario.setPassword(passwordEncoder.encode(nuevaContrasena));
        usuario.setTokenRestablecimientoContrasena(null);
        usuario.setFechaExpiracionTokenRestablecimientoContrasena(null);
        usuarioRepository.save(usuario);
    }
}
