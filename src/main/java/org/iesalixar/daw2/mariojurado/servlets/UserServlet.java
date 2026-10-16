
package org.iesalixar.daw2.mariojurado.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.iesalixar.daw2.mariojurado.daos.UserDAO;
import org.iesalixar.daw2.mariojurado.daos.UserDAOImpl;
import org.iesalixar.daw2.mariojurado.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Servlet encargado de gestionar el CRUD de usuarios.
 *
 * @author Mario Jurado
 * @version 1.0
 */
@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private static final Logger logger =
            LoggerFactory.getLogger(UserServlet.class);

    /**
     * Gestiona las peticiones GET del CRUD.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("Entrando en doGet de UserServlet");

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        try {
            UserDAO userDAO = new UserDAOImpl();

            switch (action) {

                case "new":
                    request.getRequestDispatcher("/view/user/user-form.jsp")
                            .forward(request, response);
                    break;

                case "edit":
                    Long editId = Long.valueOf(request.getParameter("id"));
                    User user = userDAO.getById(editId);

                    request.setAttribute("user", user);
                    request.getRequestDispatcher("/view/user/user-form.jsp")
                            .forward(request, response);
                    break;

                case "delete":
                    Long deleteId = Long.valueOf(request.getParameter("id"));
                    userDAO.delete(deleteId);
                    response.sendRedirect(request.getContextPath() + "/users");
                    break;

                default:
                    List<User> users = userDAO.getAll();

                    request.setAttribute("users", users);
                    request.setAttribute("userCount", users.size());
                    request.getRequestDispatcher("/view/user/user-list.jsp")
                            .forward(request, response);
                    break;
            }

        } catch (SQLException | NumberFormatException e) {
            logger.error("Error al gestionar la petición GET", e);
            throw new ServletException("Error al gestionar usuarios", e);
        }

        logger.info("Saliendo de doGet de UserServlet");
    }


    /**
     * Gestiona la creación y modificación de usuarios.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error del Servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("Entrando en doPost de UserServlet");

        request.setCharacterEncoding("UTF-8");

        // El borrado se recibe por POST desde el formulario de la vista.
        if ("delete".equals(request.getParameter("action"))) {
            try {
                Long deleteId = Long.valueOf(request.getParameter("id"));
                new UserDAOImpl().delete(deleteId);
                response.sendRedirect(request.getContextPath() + "/users");
            } catch (NumberFormatException | SQLException e) {
                logger.error("Error al eliminar el usuario", e);
                throw new ServletException("Error al eliminar el usuario", e);
            }
            return;
        }

        String idParam = request.getParameter("id");
        String username = request.getParameter("username");
        String passwordHash = request.getParameter("passwordHash");
        String lastPasswordChangeParam =
                request.getParameter("lastPasswordChange");
        String failedLoginAttemptsParam =
                request.getParameter("failedLoginAttempts");

        String[] requiredFields = {
                username,
                passwordHash,
                lastPasswordChangeParam,
                failedLoginAttemptsParam
        };

        String error = null;

        // Validar campos obligatorios mediante un array
        for (String field : requiredFields) {
            if (field == null || field.isBlank()) {
                error = "Todos los campos obligatorios deben estar rellenos.";
                break;
            }
        }

        User user = new User();

        try {
            Long id = (idParam == null || idParam.isBlank())
                    ? null : Long.valueOf(idParam);

            user.setId(id);
            user.setUsername(username);
            user.setPasswordHash(passwordHash);

            // Los checkbox no marcados no se envían en el formulario
            user.setActive(Boolean.valueOf(request.getParameter("active")));
            user.setAccountNonLocked(
                    Boolean.valueOf(request.getParameter("accountNonLocked")));
            user.setEmailVerified(
                    Boolean.valueOf(request.getParameter("emailVerified")));
            user.setMustChangePassword(
                    Boolean.valueOf(request.getParameter("mustChangePassword")));

            // Validar longitud de los campos VARCHAR
            if (error == null && username.length() > 40) {
                error = "El username no puede superar los 40 caracteres.";
            }

            if (error == null && passwordHash.length() > 500) {
                error = "La contraseña no puede superar los 500 caracteres.";
            }

            if (error == null) {
                LocalDateTime lastPasswordChange =
                        LocalDateTime.parse(lastPasswordChangeParam);

                Integer failedLoginAttempts =
                        Integer.valueOf(failedLoginAttemptsParam);

                if (failedLoginAttempts < 0) {
                    error = "Los intentos fallidos no pueden ser negativos.";
                }

                user.setLastPasswordChange(lastPasswordChange);

                // Caducidad: tres meses después del último cambio
                user.setPasswordExpiresAt(lastPasswordChange.plusMonths(3));

                user.setFailedLoginAttempts(failedLoginAttempts);
            }

            UserDAO userDAO = new UserDAOImpl();

            // Comprobar username duplicado
            if (error == null && userDAO.existsByUsername(username)) {
                User existingUser = id == null ? null : userDAO.getById(id);

                if (existingUser == null ||
                        !existingUser.getUsername().equals(username)) {
                    error = "Ya existe un usuario con ese username.";
                }
            }

            if (error != null) {
                logger.warn("Error de validación: {}", error);

                request.setAttribute("error", error);
                request.setAttribute("user", user);

                request.getRequestDispatcher("/view/user/user-form.jsp")
                        .forward(request, response);
                return;
            }

            if (id == null) {
                userDAO.insert(user);
                logger.info("Usuario creado: {}", username);
            } else {
                userDAO.update(user);
                logger.info("Usuario actualizado: {}", username);
            }

            response.sendRedirect(request.getContextPath() + "/users");

        } catch (NumberFormatException | DateTimeParseException e) {
            logger.error("Error al convertir los datos del formulario", e);

            request.setAttribute("error",
                    "La fecha o los intentos fallidos no tienen un formato válido.");
            request.setAttribute("user", user);

            request.getRequestDispatcher("/view/user/user-form.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            logger.error("Error al guardar el usuario", e);
            throw new ServletException("Error de base de datos", e);
        }

        logger.info("Saliendo de doPost de UserServlet");
    }

}
