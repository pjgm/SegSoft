package servlets;

import app.Authenticator;
import exceptions.EmptyFieldException;
import exceptions.ExistingAccountException;
import exceptions.PasswordMismatchException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "Setup", urlPatterns = { "/Setup" })
public class Setup extends HttpServlet {

    private static final String PASSWORDPATTERN = "((?=.*[a-z]).{8,64})";
    private static final Logger LOGGER = Logger.getLogger(Setup.class.getName());

    private Authenticator auth;

    @Override
    public void init() {
        this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/setup.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String password = request.getParameter("password");
        Pattern p2 = Pattern.compile(PASSWORDPATTERN);
        Matcher m2 = p2.matcher(password);
        if (!m2.matches()) {
            request.setAttribute("errorMessage", "The password must contain at least 8 characters");
            request.getRequestDispatcher("/WEB-INF/setup.jsp").forward(request, response);
            return;
        }

        ServletContext sc = getServletContext();

        try {
            auth.create_account("root", password, password);
            LOGGER.log(Level.FINE, "ROOT SETUP SUCCESSFUL");
            sc.setAttribute("isSetupDone", auth.isSetupDone());
            request.setAttribute("errorMessage", "Admin created successfully");
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        } catch (SQLException | PasswordMismatchException | ExistingAccountException | EmptyFieldException | ClassNotFoundException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/setup.jsp").forward(request, response);
        }
    }
}
