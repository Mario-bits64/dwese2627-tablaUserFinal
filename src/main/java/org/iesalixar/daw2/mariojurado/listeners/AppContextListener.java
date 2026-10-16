
package org.iesalixar.daw2.mariojurado.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.iesalixar.daw2.mariojurado.daos.DataInitializer;
import java.io.InputStream;
import java.io.IOException;
import org.iesalixar.daw2.mariojurado.daos.DatabaseConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Listener que se ejecuta al iniciar y detener
 * la aplicación web.
 *
 * @author Mario Jurado
 * @version 1.0
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger logger =
            LoggerFactory.getLogger(AppContextListener.class);

    /**
     * Se ejecuta al iniciar la aplicación.
     *
     * @param sce evento del contexto del servlet
     */

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        logger.info("Entrando en contextInitialized");

        try {
            Connection connection =
                    DatabaseConnectionManager.getConnection();

            if (connection != null && !connection.isClosed()) {
                logger.info("Conexión con MariaDB establecida correctamente");
            }

            // Obtenemos el archivo data.sql desde resources
            try (InputStream sqlFileStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("data.sql")) {

                // Ejecutamos las sentencias SQL
                DataInitializer.loadDataFromSQL(sqlFileStream);

                logger.info("Base de datos inicializada correctamente");
            }

        } catch (SQLException | IOException e) {
            logger.error("Error al inicializar la base de datos", e);
        }

        logger.info("Saliendo de contextInitialized");
    }


    /**
     * Se ejecuta al detener la aplicación.
     *
     * @param sce evento del contexto del servlet
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        logger.info("Entrando en contextDestroyed");
        logger.info("Aplicación web detenida");
        logger.info("Saliendo de contextDestroyed");
    }
}