package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import access_control.AccessController;
import access_control.Capability;
import access_control.CapabilityClass;
import app.Authenticator;
import exceptions.EmptyFieldException;
import exceptions.ExistingAccountException;
import exceptions.PasswordMismatchException;
import model.Account;
import model.Roles;
import validation.Validator;

@WebServlet(name = "CreateUser", urlPatterns = { "/CreateUser" })
public class CreateUser extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CreateUser.class.getName());
    private Authenticator auth;
    private AccessController ac;
    private Validator val;

    public void init() {
        this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
        this.ac = (AccessController) getServletContext().getAttribute("accesscontroller");
        this.val = new Validator();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/createuser.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        //TODO: Fix duplicate code
        if (!val.validateUsername(username)) {
            request.setAttribute("errorMessage", "Username has invalid format");
            request.getRequestDispatcher("/WEB-INF/createuser.jsp").forward(request, response);
            return;
        }

        if (!val.validatePassword(password)) {
            request.setAttribute("errorMessage", "Password has invalid format");
            request.getRequestDispatcher("/WEB-INF/createuser.jsp").forward(request, response);
            return;
        }

        if (!val.validateEmail(email)) {
            request.setAttribute("errorMessage", "Email has invalid format");
            request.getRequestDispatcher("/WEB-INF/createuser.jsp").forward(request, response);
            return;
        }

        if (!val.validatePhone(phone)) {
            request.setAttribute("errorMessage", "Phone has invalid format");
            request.getRequestDispatcher("/WEB-INF/createuser.jsp").forward(request, response);
            return;
        }

        try {
            LOGGER.log(Level.FINE, "CREATED ACCOUNT " + username);
            auth.create_account(username, password, password2, email, phone, Roles.USER.name());
            ac.createCapability("root", username, "MyProfile", "RWX");
            ac.createCapability("root", username, "Friends", "RWX");
            ac.createCapability("root", username, "ChangePassword", "RWX");
            ac.createCapability("root", username, "Logout", "RWX");
            request.setAttribute("errorMessage", "User created successfully");
        } catch (SQLException | PasswordMismatchException | ExistingAccountException | EmptyFieldException e) {
            request.setAttribute("errorMessage", e.getMessage());
        } finally {
            request.getRequestDispatcher("/WEB-INF/createuser.jsp").forward(request, response);
        }
    }
}