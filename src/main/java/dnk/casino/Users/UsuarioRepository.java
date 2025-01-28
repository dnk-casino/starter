package dnk.casino.Users;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * 
 * @author Danikileitor
 */
@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param username nombre de usuario a buscar
     * @return el usuario encontrado, o un Optional vacío si no se encuentra
     */
    @Query("{'username' : { '$regex' : ?0 , $options: 'i' }}")
    Optional<Usuario> findByUsername(String username);

    /**
     * Busca un usuario por su correo electrónico.
     * 
     * @param email correo electrónico a buscar
     * @return el usuario encontrado, o un Optional vacío si no se encuentra
     */
    @Query("{'email' : { '$regex' : ?0 , $options: 'i' }}")
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca un usuario por su token de restablecimiento de contraseña.
     * 
     * @param tokenRestablecimientoContrasena token de restablecimiento de
     *                                        contraseña a buscar
     * @return el usuario encontrado, o un Optional vacío si no se encuentra
     */
    Optional<Usuario> findByTokenRestablecimientoContrasena(String tokenRestablecimientoContrasena);
}
