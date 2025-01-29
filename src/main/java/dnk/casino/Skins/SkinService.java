package dnk.casino.Skins;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio de skins para la Tragaperras.
 * 
 * @author Danikileitor
 */
@Service
public class SkinService {

  /**
   * Repositorio de skins.
   */
  @Autowired
  private SkinRepository skinRepository;

  /**
   * Crea una nueva skin en el sistema.
   * 
   * @param skin la skin a crear
   * @return la skin creada
   */
  public Skin createSkin(Skin skin) {
    if (skinRepository.findByName(skin.getName()).isPresent()) {
      throw new IllegalArgumentException("El nombre de la skin ya existe");
    }
    Skin newSkin = new Skin(skin.getName(), skin.getPrecio(), skin.getDescription(), skin.getReels(),
        skin.isVendible());
    skinRepository.save(newSkin);
    return newSkin;
  }

  /**
   * Actualiza una skin existente en el sistema.
   * 
   * @param id          el ID de la skin a actualizar
   * @param updatedSkin la skin actualizada
   * @return la skin actualizada
   */
  public Skin updateSkin(String id, Skin updatedSkin) {
    Skin skin = skinRepository.findById(id).orElseThrow(() -> new RuntimeException("Skin no encontrada"));
    if (updatedSkin.getName() != null) {
      skin.setName(updatedSkin.getName());
    }
    if (updatedSkin.getPrecio() >= 0) {
      skin.setPrecio(updatedSkin.getPrecio());
    }
    if (updatedSkin.getDescription() != null) {
      skin.setDescription(updatedSkin.getDescription());
    }
    if (updatedSkin.getReels() != null) {
      if (updatedSkin.getReels().length > 0) {
        skin.setReels(updatedSkin.getReels());
      }
    }
    if (updatedSkin.isVendible() == true || updatedSkin.isVendible() == false) {
      skin.setVendible(updatedSkin.isVendible());
    }
    return skinRepository.save(skin);
  }

  /**
   * Elimina una skin del sistema.
   * 
   * @param id el ID de la skin a eliminar
   * @return true si la skin se elimina con éxito, false en caso contrario
   */
  public boolean deleteSkin(String id) {
    Optional<Skin> skin = skinRepository.findById(id);
    if (skin.isPresent()) {
      skinRepository.deleteById(id);
      return true;
    } else {
      return false;
    }
  }
}
