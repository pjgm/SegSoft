package main.java.servlets;

import main.java.access_control.AccessController;
import main.java.app.Authenticator;
import main.java.exceptions.AuthenticationErrorException;
import main.java.exceptions.EmptyFieldException;
import main.java.exceptions.LockedAccountException;
import main.java.exceptions.UndefinedAccountException;
import main.java.model.Account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet(name = "Login", urlPatterns = { "/Login" })
public class Login extends HttpServlet {

    private static final int SESSIONTIMEOUT = 15 * 60; // 15 minutes
    private static final Logger LOGGER = Logger.getLogger(Login.class.getName());

    private Authenticator auth;
    private AccessController ac;

    @Override
    public void init() {
        this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
        this.ac = (AccessController) getServletContext().getAttribute("accesscontroller");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Account authUser = auth.login(username, password);
            LOGGER.log(Level.FINE, "LOGIN " + authUser.getUsername());
            HttpSession session = request.getSession(true);
            session.setAttribute("USER", authUser);
            session.setAttribute("AC", ac.getCapabilities(username));
            session.setMaxInactiveInterval(SESSIONTIMEOUT);
            request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
        } catch (SQLException | UndefinedAccountException | LockedAccountException | EmptyFieldException |
                AuthenticationErrorException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
    }
}
