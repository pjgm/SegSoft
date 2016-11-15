package servlets;

import exceptions.AuthenticationErrorException;
import exceptions.EmptyFieldException;
import exceptions.LockedAccountException;
import exceptions.UndefinedAccountException;
import model.Account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;


@WebServlet(description = "Login", urlPatterns = { "/Login" })
public class Login extends AbstractServlet {

    private static final int SESSIONTIMEOUT = 15 * 60;


    @Override
    protected void processGetRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

    @Override
    protected void processPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Account authUser = auth.login(username, password);
            LOGGER.log(Level.FINE, "LOGIN " + authUser.getUsername());
            HttpSession session = request.getSession(true);
            session.setAttribute("USER", authUser);
            session.setMaxInactiveInterval(SESSIONTIMEOUT);
            request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
        } catch (SQLException | UndefinedAccountException | LockedAccountException | EmptyFieldException |
                AuthenticationErrorException | ClassNotFoundException e) {
            //TODO: SQLExceptions may reveal sensitive information to the end user
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
    }
}
