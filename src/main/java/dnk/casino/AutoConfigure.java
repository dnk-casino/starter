package dnk.casino;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Clase de configuración automática para la aplicación. Esta clase habilita la
 * configuración automática de Spring Boot,
 * el escaneo de componentes y la habilitación de repositorios de MongoDB.
 * 
 * @author Danikileitor
 */
@AutoConfiguration
@ComponentScan
@EnableMongoRepositories
public class AutoConfigure {}
