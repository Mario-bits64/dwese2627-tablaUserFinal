package org.iesalixar.daw2.mariojurado.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;

@WebServlet("/changeLanguage")
public class LanguageServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(LanguageServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        logger.info("Entrando en el método doGet del servlet LanguageServlet");

        try {
            String language = request.getParameter("lang");

            if (language != null) {
                Locale locale;
                if (language.equals("es")) {
                    locale = new Locale("es");
                    logger.info("Idioma seleccionado: Español (es)");
                } else {
                    locale = new Locale("en");
                    logger.info("Idioma seleccionado: Inglés (en)");
                }

                request.getSession().setAttribute("locale", locale);
                logger.info("El locale '{}' ha sido guardado en la sesión", locale);
            } else {
                logger.warn("No se ha recibido ningún parámetro 'lang'");
            }

            String referer = request.getHeader("Referer");
            if (referer != null) {
                logger.info("Redirigiendo al usuario a la página anterior: {}", referer);
                response.sendRedirect(referer);
            } else {
                logger.warn("No se encontró la cabecera Referer, redirigiendo a la página principal.");
                response.sendRedirect(request.getContextPath() + "/");
            }

        } catch (Exception e) {
            logger.error("Se ha producido un error en el método doGet de LanguageServlet: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cambiar el idioma");
        }

        logger.info("Saliendo del método doGet del servlet LanguageServlet");
    }
}
