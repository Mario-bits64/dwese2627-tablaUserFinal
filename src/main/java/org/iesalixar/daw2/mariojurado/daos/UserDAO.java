
package org.iesalixar.daw2.mariojurado.daos;

import org.iesalixar.daw2.mariojurado.entities.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para gestionar las operaciones CRUD
 * de la tabla users.
 *
 * @author Mario
 * @version 1.0
 */

public interface UserDAO {

    /**
     * Obtiene a todos los usuarios listados.
     *
     * @return lista de usuarios
     * @throws SQLException
     */
    List<User> getAll() throws SQLException;

    /**
     * Busca un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @return usuario encontrado o null
     * @throws SQLException
     */
    User getById(Long id) throws SQLException;

    void insert(User user) throws SQLException;

    void update(User user) throws SQLException;

    void delete(Long id) throws SQLException;

    /**
     * Comprueba si existe un nombre de usuario.
     *
     * @param username nombre de usuario
     * @return true si existe, false si no existe
     * @throws SQLException si ocurre un error SQL
     */
    boolean existsByUsername(String username) throws SQLException;
}