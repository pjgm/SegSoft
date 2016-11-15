package servlets;

import exceptions.EmptyFieldException;
import exceptions.ExistingAccountException;
import exceptions.PasswordMismatchException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "Setup", urlPatterns = { "/Setup" })
public class Setup extends AbstractServlet {

    private static final String PASSWORDPATTERN = "((?=.*[a-z]).{8,64})";

    @Override
    protected void processGetRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/setup.jsp").forward(request, response);
    }

    @Override
    protected void processPostRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    }
}
