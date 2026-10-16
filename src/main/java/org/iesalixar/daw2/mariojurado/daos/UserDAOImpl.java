
package org.iesalixar.daw2.mariojurado.daos;

import org.iesalixar.daw2.mariojurado.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Implementación del DAO para gestionar los usuarios
 *
 * @author Mario Jurado
 * @version 1.0
 */
public class UserDAOImpl implements UserDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(UserDAOImpl.class);

    private final Connection connection;

    /**
     * Constructor que obtiene la conexión con la base de datos.
     *
     * @throws SQLException si ocurre un error de conexión
     */
    public UserDAOImpl() throws SQLException {
        logger.info("Entrando en el constructor de UserDAOImpl");

        this.connection = DatabaseConnectionManager.getConnection();

        logger.info("Saliendo del constructor de UserDAOImpl");
    }
    @Override
    public List<User> getAll() throws SQLException {
        logger.info("Entrando en getAll");

        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                User user = new User();

                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("passwordHash"));
                user.setActive(rs.getBoolean("active"));
                user.setAccountNonLocked(rs.getBoolean("accountNonLocked"));
                user.setLastPasswordChange(
                        rs.getTimestamp("lastPasswordChange").toLocalDateTime());
                user.setPasswordExpiresAt(
                        rs.getTimestamp("passwordExpiresAt").toLocalDateTime());
                user.setFailedLoginAttempts(rs.getInt("failedLoginAttempts"));
                user.setEmailVerified(rs.getBoolean("emailVerified"));
                user.setMustChangePassword(rs.getBoolean("mustChangePassword"));

                users.add(user);
            }
        } catch (SQLException e) {
            logger.error("Error al obtener los usuarios", e);
            throw e;
        }

        logger.info("Saliendo de getAll");
        return users;
    }

    @Override
    public User getById(Long id) throws SQLException {
        logger.info("Entrando en getById");

        String sql = "SELECT * FROM users WHERE id = ?";
        User user = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    user = new User();

                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("passwordHash"));
                    user.setActive(rs.getBoolean("active"));
                    user.setAccountNonLocked(rs.getBoolean("accountNonLocked"));
                    user.setLastPasswordChange(
                            rs.getTimestamp("lastPasswordChange").toLocalDateTime());
                    user.setPasswordExpiresAt(
                            rs.getTimestamp("passwordExpiresAt").toLocalDateTime());
                    user.setFailedLoginAttempts(rs.getInt("failedLoginAttempts"));
                    user.setEmailVerified(rs.getBoolean("emailVerified"));
                    user.setMustChangePassword(rs.getBoolean("mustChangePassword"));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar usuario con ID {}", id, e);
            throw e;
        }

        logger.info("Saliendo de getById");
        return user;
    }


    @Override
    public void insert(User user) throws SQLException {
        logger.info("Entrando en insert");

        String sql = """
            INSERT INTO users
            (username, passwordHash, active, accountNonLocked,
             lastPasswordChange, passwordExpiresAt, failedLoginAttempts,
             emailVerified, mustChangePassword)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setBoolean(3, user.getActive());
            statement.setBoolean(4, user.getAccountNonLocked());
            statement.setTimestamp(5,
                    Timestamp.valueOf(user.getLastPasswordChange()));
            statement.setTimestamp(6,
                    Timestamp.valueOf(user.getPasswordExpiresAt()));
            statement.setInt(7, user.getFailedLoginAttempts());
            statement.setBoolean(8, user.getEmailVerified());
            statement.setBoolean(9, user.getMustChangePassword());

            statement.executeUpdate();

            logger.info("Usuario insertado correctamente");

        } catch (SQLException e) {
            logger.error("Error al insertar usuario", e);
            throw e;
        }

        logger.info("Saliendo de insert");
    }


    @Override
    public void update(User user) throws SQLException {
        logger.info("Entrando en update");

        String sql = """
            UPDATE users SET
            username = ?, passwordHash = ?, active = ?,
            accountNonLocked = ?, lastPasswordChange = ?,
            passwordExpiresAt = ?, failedLoginAttempts = ?,
            emailVerified = ?, mustChangePassword = ?
            WHERE id = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setBoolean(3, user.getActive());
            statement.setBoolean(4, user.getAccountNonLocked());
            statement.setTimestamp(5,
                    Timestamp.valueOf(user.getLastPasswordChange()));
            statement.setTimestamp(6,
                    Timestamp.valueOf(user.getPasswordExpiresAt()));
            statement.setInt(7, user.getFailedLoginAttempts());
            statement.setBoolean(8, user.getEmailVerified());
            statement.setBoolean(9, user.getMustChangePassword());
            statement.setLong(10, user.getId());

            statement.executeUpdate();

            logger.info("Usuario actualizado correctamente");

        } catch (SQLException e) {
            logger.error("Error al actualizar usuario", e);
            throw e;
        }

        logger.info("Saliendo de update");
    }



    @Override
    public void delete(Long id) throws SQLException {
        logger.info("Entrando en delete");

        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

            logger.info("Usuario eliminado correctamente: {}", id);

        } catch (SQLException e) {
            logger.error("Error al eliminar usuario", e);
            throw e;
        }

        logger.info("Saliendo de delete");
    }



    @Override
    public boolean existsByUsername(String username) throws SQLException {
        logger.info("Entrando en existsByUsername");

        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    boolean exists = rs.getInt(1) > 0;

                    logger.info("Saliendo de existsByUsername");
                    return exists;
                }
            }

        } catch (SQLException e) {
            logger.error("Error al comprobar el username", e);
            throw e;
        }

        logger.info("Saliendo de existsByUsername");
        return false;
    }

}