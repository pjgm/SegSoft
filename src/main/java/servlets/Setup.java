package servlets;

import access_control.AccessController;
import access_control.AccessControllerClass;
import app.Authenticator;
import exceptions.EmptyFieldException;
import exceptions.ExistingAccountException;
import exceptions.PasswordMismatchException;
import model.Roles;
import validation.Validator;

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

@WebServlet(name = "Setup", urlPatterns = { "/Setup" })
public class Setup extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Setup.class.getName());
    private Authenticator auth;
    private AccessController ac;
    private Validator val;

    @Override
    public void init() {
        this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
        this.ac = (AccessController) getServletContext().getAttribute("accesscontroller");
        this.val = new Validator();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/setup.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String password = request.getParameter("password");

        if (!val.validatePassword(password)) {
            request.setAttribute("errorMessage", "Password has invalid format. Check if it has at least 8 characters.");
            request.getRequestDispatcher("/WEB-INF/setup.jsp").forward(request, response);
            return;
        }

        ServletContext sc = getServletContext();

        try {
            auth.create_account("system", password, password, "system@system.com" , "00000", "system");
            auth.create_account("root", password, password, "admin@admin.com", "911111111", Roles.ADMIN.name());
            ac.createCapability("system", "root", "", "RWX");
            ac.createCapability("system", "root", "Admin", "RWX");
            ac.createCapability("system", "root", "Home", "RWX");
            ac.createCapability("system", "root", "CreateUser", "RWX");
            ac.createCapability("system", "root", "DeleteUser", "RWX");
            ac.createCapability("system", "root", "MyProfile", "RWX");
            ac.createCapability("system", "root", "Friends", "RWX");
            ac.createCapability("system", "root", "ChangePassword", "RWX");
            ac.createCapability("system", "root", "Logout", "RWX");
            LOGGER.log(Level.FINE, "ROOT SETUP SUCCESSFUL");
            sc.setAttribute("isSetupDone", auth.isSetupDone());
            request.setAttribute("errorMessage", "Admin created successfully");
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        } catch (SQLException | PasswordMismatchException | ExistingAccountException | EmptyFieldException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/setup.jsp").forward(request, response);
        }
    }
}
