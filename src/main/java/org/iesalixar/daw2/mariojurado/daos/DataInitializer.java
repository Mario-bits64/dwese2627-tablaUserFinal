
package org.iesalixar.daw2.mariojurado.daos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase encargada de inicializar la base de datos
 * ejecutando las instrucciones del archivo data.sql.
 *
 * @author Mario Jurado
 * @version 1.0
 */
public class DataInitializer {

    private static final Logger logger =
            LoggerFactory.getLogger(DataInitializer.class);

    /**
     * Lee y ejecuta las instrucciones SQL de un archivo.
     *
     * @param sqlFileStream archivo SQL como InputStream
     * @throws SQLException si ocurre un error SQL
     * @throws IOException si ocurre un error de lectura
     */
    public static void loadDataFromSQL(InputStream sqlFileStream)
            throws SQLException, IOException {

        logger.info("Entrando en loadDataFromSQL");

        if (sqlFileStream == null) {
            logger.error("No se ha encontrado el archivo SQL");
            throw new IOException("No se ha encontrado data.sql");
        }

        StringBuilder sql = new StringBuilder();

        // Leemos el contenido del archivo SQL
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(sqlFileStream, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {

                // Ignoramos comentarios y líneas vacías
                String trimmedLine = line.trim();

                if (trimmedLine.isEmpty() ||
                        trimmedLine.startsWith("--")) {
                    continue;
                }

                sql.append(line).append("\n");
            }
        }

        // Obtenemos la conexión con MariaDB
        Connection connection =
                DatabaseConnectionManager.getConnection();

        // Ejecutamos cada sentencia SQL
        try (Statement statement = connection.createStatement()) {

            String[] sqlStatements = sql.toString().split(";");

            for (String sqlStatement : sqlStatements) {

                if (!sqlStatement.isBlank()) {
                    logger.info("Ejecutando sentencia SQL");
                    statement.execute(sqlStatement.trim());
                }
            }
        }

        logger.info("Saliendo de loadDataFromSQL");
    }
}