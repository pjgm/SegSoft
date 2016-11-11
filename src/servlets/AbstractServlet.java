package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import app.*;
import exceptions.*;

@WebServlet(name = "AbstractServlet")
public abstract class AbstractServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int SESSIONTIMEOUT = 15 * 60;

    protected static final Logger LOGGER = Logger.getLogger(AbstractServlet.class.getName());
    protected Authenticator auth = new AuthenticatorClass();

    private static final String PASSWORDPATTERN = "((?=.*[a-z]).{8,64})";

    private boolean isSetupDone() throws SQLException, ClassNotFoundException {
        return auth.isSetupDone();
    }

    private Account sessionLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("USER") == null)
            return null;

        try {
            Account user = auth.login(request, response);
            LOGGER.log(Level.FINE, "LOGIN " + user.get_account_name());
            return user;
        } catch (AuthenticationErrorException | SQLException | UndefinedAccountException | LockedAccountException | ClassNotFoundException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        return null;
    }

    protected boolean isRoot(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session.getAttribute("USER").toString().equals("root");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (!isSetupDone()) {
                request.getRequestDispatcher("/WEB-INF/setup.jsp").forward(request, response);
                return;
            }
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            return;
        }
        Account a = sessionLogin(request, response);
        if (a == null) {
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            return;
        }

        processGetRequest(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean isSetupDone = false;
        try {
            isSetupDone = isSetupDone();
        } catch (SQLException | ClassNotFoundException e) {
            request.setAttribute("errorMessage" , e.getMessage());
            request.getRequestDispatcher("/WEB-INF/setup.jsp").forward(request, response);
            return;
        }

        if (!isSetupDone) {
            String password = request.getParameter("password");
            Pattern p2 = Pattern.compile(PASSWORDPATTERN);
            Matcher m2 = p2.matcher(password);
            if (!m2.matches()) {
                request.setAttribute("errorMessage", "The password must contain at least 8 characters");
                request.getRequestDispatcher("/WEB-INF/setup.jsp").forward(request, response);
                return;
            }

            try {
                auth.create_account("root", password, password);
                LOGGER.log(Level.FINE, "ROOT SETUP SUCCESSFUL");
                request.setAttribute("errorMessage", "Admin created successfully");
            } catch (SQLException | PasswordMismatchException | ExistingAccountException | EmptyFieldException | ClassNotFoundException e) {
                request.setAttribute("errorMessage", e.getMessage());
            } finally {
                request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            }

            return;
        }

        Account acc = sessionLogin(request, response);
        if (acc == null) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            try {
                Account authUser = auth.login(username, password);
                LOGGER.log(Level.FINE, "LOGIN " + authUser.get_account_name());
                HttpSession session = request.getSession(true);
                session.setAttribute("USER", authUser.get_account_name());
                session.setAttribute("PWD", authUser.get_account_pwd());
                session.setMaxInactiveInterval(SESSIONTIMEOUT);
                request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
            } catch (SQLException | UndefinedAccountException | LockedAccountException | EmptyFieldException |
                    AuthenticationErrorException | ClassNotFoundException e) {
                request.setAttribute("errorMessage", e.getMessage());
                request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            }
            return;
        }
        processPostRequest(request, response);
    }

    protected abstract void processGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    protected abstract void processPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}