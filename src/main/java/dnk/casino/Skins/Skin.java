package dnk.casino.Skins;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa una skin para la Tragaperras.
 * 
 * @author Danikileitor
 */
@Document(collection = "skins")
public class Skin {
    /**
     * Identificador único de la skin.
     */
    @Id
    private String id;

    /**
     * Nombre de la skin.
     */
    private String name;

    /**
     * Precio de la skin.
     */
    private int precio;

    /**
     * Descripción de la skin.
     */
    private String description;

    /**
     * Reels asociados a la skin.
     */
    private String[] reels;

    /**
     * Indica si la skin es vendible.
     */
    private boolean vendible;

    /**
     * Constructor que inicializa la skin con todos los atributos.
     * 
     * @param name        nombre de la skin
     * @param precio      precio de la skin
     * @param description descripción de la skin
     * @param reels       reels asociados a la skin
     * @param vendible    indica si la skin es vendible
     */
    public Skin(String name, int precio, String description, String[] reels, boolean vendible) {
        this.name = name;
        this.precio = precio;
        this.description = description;
        this.reels = reels;
        this.vendible = vendible;
    }

    /**
     * Constructor que inicializa la skin con nombre, descripción, reels y vendible,
     * y establece el precio en 0.
     * 
     * @param name        nombre de la skin
     * @param description descripción de la skin
     * @param reels       reels asociados a la skin
     * @param vendible    indica si la skin es vendible
     */
    public Skin(String name, String description, String[] reels, boolean vendible) {
        this(name, 0, description, reels, vendible);
    }

    /**
     * Constructor que inicializa la skin con nombre, precio, descripción y reels,
     * y establece la skin como vendible.
     * 
     * @param name        nombre de la skin
     * @param precio      precio de la skin
     * @param description descripción de la skin
     * @param reels       reels asociados a la skin
     */
    public Skin(String name, int precio, String description, String[] reels) {
        this(name, precio, description, reels, true);
    }

    /**
     * Constructor que inicializa la skin con nombre, descripción y reels,
     * y establece el precio en 0 y la skin como vendible.
     * 
     * @param name        nombre de la skin
     * @param description descripción de la skin
     * @param reels       reels asociados a la skin
     */
    public Skin(String name, String description, String[] reels) {
        this(name, 0, description, reels, true);
    }

    /**
     * Constructor vacío.
     */
    public Skin() {
    }

    /**
     * Obtiene el identificador único de la skin.
     * 
     * @return identificador único
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador único de la skin.
     * 
     * @param id identificador único
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la skin.
     * 
     * @return nombre
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre de la skin.
     * 
     * @param name nombre
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene el precio de la skin.
     * 
     * @return precio
     */
    public int getPrecio() {
        return precio;
    }

    /**
     * Establece el precio de la skin.
     * 
     * @param precio precio
     */
    public void setPrecio(int precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la descripción de la skin.
     * 
     * @return descripción
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción de la skin.
     * 
     * @param description descripción
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene los reels asociados a la skin.
     * 
     * @return reels
     */
    public String[] getReels() {
        return reels;
    }

    /**
     * Establece los reels asociados a la skin.
     * 
     * @param reels reels
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
