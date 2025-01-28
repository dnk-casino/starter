package dnk.casino.Users;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Representa un usuario en el sistema.
 * 
 * @author Danikileitor
 */
@Document(collection = "users")
public class Usuario {

    /**
     * Identificador único del usuario.
     */
    @Id
    private String id;

    /**
     * Nombre de usuario.
     */
    private String username;

    /**
     * Contraseña del usuario (encriptada).
     */
    private String password;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Rol del usuario en el sistema.
     */
    private Rol rol;

    /**
     * Cantidad de monedas que posee el usuario.
     */
    private int coins;

    /**
     * Número de victorias del usuario.
     */
    private int wins;

    /**
     * Número de victorias en blackjack del usuario.
     */
    private int bjwins;

    /**
     * Conjunto de skins desbloqueadas por el usuario.
     */
    private Set<String> skins;

    /**
     * Fecha de último inicio de sesión del usuario.
     */
    private Date lastLoginDate;

    /**
     * Token de restablecimiento de contraseña.
     */
    private String tokenRestablecimientoContrasena;

    /**
     * Fecha de expiración del token de restablecimiento de contraseña.
     */
    private LocalDateTime fechaExpiracionTokenRestablecimientoContrasena;

    /**
     * Enumeración de roles posibles para un usuario.
     */
    public enum Rol {
        /**
         * Rol de usuario estándar.
         */
        ROLE_USER,
        /**
         * Rol de administrador.
         */
        ROLE_ADMIN,
        /**
         * Rol de usuario VIP.
         */
        ROLE_VIP;
    }

    /**
     * Constructor vacío.
     */
    public Usuario() {
    }

    /**
     * Constructor que inicializa el usuario con un nombre de usuario, contraseña y
     * correo electrónico.
     * 
     * @param username nombre de usuario
     * @param password contraseña
     * @param email    correo electrónico
     */
    public Usuario(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.rol = Rol.ROLE_USER;
        this.coins = 0;
        this.wins = 0;
        this.bjwins = 0;
        this.skins = new HashSet<>();
        this.lastLoginDate = new Date(0);
        this.tokenRestablecimientoContrasena = null;
        this.fechaExpiracionTokenRestablecimientoContrasena = null;
    }

    /**
     * Constructor que inicializa el usuario con un nombre de usuario, contraseña,
     * correo electrónico, rol y skins.
     * 
     * @param username nombre de usuario
     * @param password contraseña
     * @param email    correo electrónico
     * @param rol      rol del usuario
     * @param skins    skins desbloqueadas
     */
    public Usuario(String username, String password, String email, Rol rol, Set<String> skins) {
        this(username, password, email);
        this.rol = rol;
        this.skins = skins;
    }

    /**
     * Obtiene el identificador único del usuario.
     * 
     * @return identificador único
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     * 
     * @param id identificador único
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de usuario.
     * 
     * @return nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     * 
     * @param username nombre de usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña del usuario.
     * 
     * @return contraseña
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * 
     * @param password contraseña
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * 
     * @return correo electrónico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     * 
     * @param email correo electrónico
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el rol del usuario.
     * 
     * @return rol
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario.
     * 
     * @param rol rol
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Obtiene la cantidad de monedas que posee el usuario.
     * 
     * @return cantidad de monedas
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Establece la cantidad de monedas que posee el usuario.
     * 
     * @param coins cantidad de monedas
     */
    public void setCoins(int coins) {
        this.coins = coins;
    }

    /**
     * Obtiene el número de victorias del usuario.
     * 
     * @return número de victorias
     */
    public int getWins() {
        return wins;
    }

    /**
     * Establece el número de victorias del usuario.
     * 
     * @param wins número de victorias
     */
    public void setWins(int wins) {
        this.wins = wins;
    }

    /**
     * Obtiene el número de victorias en blackjack del usuario.
     * 
     * @return número de victorias en blackjack
     */
    public int getBjwins() {
        return bjwins;
    }

    /**
     * Establece el número de victorias en blackjack del usuario.
     * 
     * @param bjwins número de victorias en blackjack
     */
    public void setBjwins(int bjwins) {
        this.bjwins = bjwins;
    }

    /**
     * Obtiene el conjunto de skins desbloqueadas por el usuario.
     * 
     * @return conjunto de skins
     */
    public Set<String> getSkins() {
        return skins;
    }

    /**
     * Establece el conjunto de skins desbloqueadas por el usuario.
     * 
     * @param skins conjunto de skins
     */
    public void setSkins(Set<String> skins) {
        this.skins = skins;
    }

    /**
     * Obtiene la fecha de último inicio de sesión del usuario.
     * 
     * @return fecha de último inicio de sesión
     */
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    /**
     * Establece la fecha de último inicio de sesión del usuario.
     * 
     * @param lastLoginDate fecha de último inicio de sesión
     */
    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    /**
     * Obtiene el token de restablecimiento de contraseña.
     * 
     * @return token de restablecimiento de contraseña
     */
    public String getTokenRestablecimientoContrasena() {
        return tokenRestablecimientoContrasena;
    }

    /**
     * Establece el token de restablecimiento de contraseña.
     * 
     * @param tokenRestablecimientoContrasena token de restablecimiento de
     *                                        contraseña
     */
    public void setTokenRestablecimientoContrasena(String tokenRestablecimientoContrasena) {
        this.tokenRestablecimientoContrasena = tokenRestablecimientoContrasena;
    }

    /**
     * Obtiene la fecha de expiración del token de restablecimiento de contraseña.
     * 
     * @return fecha de expiración
     */
    public LocalDateTime getFechaExpiracionTokenRestablecimientoContrasena() {
        return fechaExpiracionTokenRestablecimientoContrasena;
    }

    /**
     * Establece la fecha de expiración del token de restablecimiento de contraseña.
     * 
     * @param fechaExpiracionTokenRestablecimientoContrasena fecha de expiración
     */
    public void setFechaExpiracionTokenRestablecimientoContrasena(
            LocalDateTime fechaExpiracionTokenRestablecimientoContrasena) {
        this.fechaExpiracionTokenRestablecimientoContrasena = fechaExpiracionTokenRestablecimientoContrasena;
    }

    /**
     * Desbloquea una skin para el usuario.
     * 
     * @param skin skin a desbloquear
     * @return true si la skin se desbloqueó con éxito, false en caso contrario
     */
    public boolean desbloquearSkin(String skin) {
        if (skins != null && !getSkins().contains(skin)) {
            skins.add(skin);
            return true;
        }
        return false;
    }

    /**
     * Verifica si es el primer inicio de sesión del día para el usuario.
     * 
     * @return true si es el primer inicio de sesión del día, false en caso
     *         contrario
     */
    public boolean isFirstLoginOfDay() {
        Calendar today = Calendar.getInstance();
        Calendar lastLogin = Calendar.getInstance();
        today.setTime(new Date());
        lastLogin.setTime(lastLoginDate);

        return lastLogin.get(Calendar.DAY_OF_YEAR) != today.get(Calendar.DAY_OF_YEAR)
                || lastLogin.get(Calendar.YEAR) != today.get(Calendar.YEAR);
    }

    /**
     * Registra una victoria para el usuario.
     */
    public void victoria() {
        wins++;
    }

    /**
     * Registra una victoria en blackjack para el usuario.
     */
    public void bjvictoria() {
        bjwins++;
    }

    /**
     * Reduce una cantidad de monedas al usuario.
     * 
     * @param coins cantidad de monedas a pagar
     */
    public void pagar(int coins) {
        this.coins -= coins;
    }

    /**
     * Aumenta una cantidad de monedas al usuario.
     * 
     * @param coins cantidad de monedas a cobrar
     */
    public void cobrar(int coins) {
        this.coins += coins;
    }
}
