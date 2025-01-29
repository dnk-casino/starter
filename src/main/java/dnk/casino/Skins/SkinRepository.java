package dnk.casino.Skins;

import java.util.Optional;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Repositorio de skins para la Tragaperras.
 * 
 * @author Danikileitor
 */
public interface SkinRepository extends MongoRepository<Skin, String> {

    /**
     * Busca una skin por su nombre.
     * 
     * @param name nombre de la skin a buscar
     * @return la skin encontrada, o un Optional vacío si no se encuentra
     */
    @Query("{'name' : { '$regex' : ?0 , $options: 'i' }}")
    Optional<Skin> findByName(String name);

    /**
     * Obtiene una lista de skins que son vendibles o no, según el parámetro.
     * 
     * @param vendible true para obtener skins vendibles, false para obtener skins
     *                 no vendibles
     * @return lista de skins
     */
    List<Skin> findAllByVendible(boolean vendible);
}
