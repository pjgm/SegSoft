package main.java.servlets;

import main.java.app.Authenticator;
import main.java.exceptions.EmptyFieldException;
import main.java.exceptions.UndefinedAccountException;
import main.java.model.Account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebServlet(name = "Admin", urlPatterns = {"/Admin"})
public class Admin extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Admin.class.getName());
    private Authenticator auth;

    @Override
    public void init() {
        this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/admin.jsp").forward(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String submitButton = request.getParameter("submitButton");

        try {
            if (submitButton.equals("lock")) {
                String lockUsername = request.getParameter("lockUsername");

                if(lockUsername.isEmpty())
                    throw new EmptyFieldException();

                Account acc = auth.get_account(lockUsername);

                if(!acc.getRole().equals("ADMIN")) {
                    auth.lock_account(lockUsername);
                    request.setAttribute("errorMessage", "User locked successfully");
                }
                else request.setAttribute("errorMessage", "Can't lock admin accounts");
            }
            if (submitButton.equals("unlock")) {
                String unlockUsername = request.getParameter("unlockUsername");
                auth.unlock_account(unlockUsername);
                request.setAttribute("errorMessage", "User unlocked successfully");
            }
        }
        catch (EmptyFieldException | SQLException | UndefinedAccountException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/admin.jsp").forward(request, response);
    }
}