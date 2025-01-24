package dnk.casino.Users;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    @Query("{'username' : { '$regex' : ?0 , $options: 'i' }}")
    Optional<Usuario> findByUsername(String username);

    @Query("{'email' : { '$regex' : ?0 , $options: 'i' }}")
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByTokenRestablecimientoContrasena(String tokenRestablecimientoContrasena);
}