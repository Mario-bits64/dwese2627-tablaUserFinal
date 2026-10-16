
package org.iesalixar.daw2.mariojurado.daos;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión con la base de datos MariaDB.
 * Utiliza el patrón Singleton para centralizar
 * el acceso a la conexión.
 *
 * @author Mario Jurado
 * @version 1.0
 */
public class DatabaseConnectionManager {

    // Instancia única de la conexión
    private static Connection connection = null;

    // Logger para registrar los eventos
    private static final Logger logger =
            LoggerFactory.getLogger(DatabaseConnectionManager.class);

    // Carga las variables del archivo .env
    private static final Dotenv dotenv = Dotenv.load();

    /**
     * Constructor privado para evitar crear instancias.
     */
    private DatabaseConnectionManager() {
    }

    /**
     * Obtiene la conexión con MariaDB.
     *
     * @return conexión a la base de datos
     * @throws SQLException si ocurre un error de conexión
     */
    public static Connection getConnection() throws SQLException {

        logger.info("Entrando en getConnection");

        if (connection == null || connection.isClosed()) {

            logger.info("Iniciando conexión con MariaDB...");

            String dbUrl = dotenv.get("DB_URL");
            String dbUser = dotenv.get("DB_USER");
            String dbPassword = dotenv.get("DB_PASSWORD");

            connection = DriverManager.getConnection(
                    dbUrl, dbUser, dbPassword
            );

            logger.info("Conexión establecida correctamente");
        }

        logger.info("Saliendo de getConnection");

        return connection;
    }
}