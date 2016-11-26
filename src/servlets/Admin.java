package servlets;

import app.Authenticator;
import exceptions.EmptyFieldException;
import exceptions.UndefinedAccountException;
import model.Account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebServlet(name = "Admin", urlPatterns = {"/Admin"})
public class Admin extends HttpServlet {

    private Authenticator auth;
    private static final Logger LOGGER = Logger.getLogger(Admin.class.getName());

    @Override
    public void init() {
        this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
    }

    private boolean isRoot(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Account acc = (Account) session.getAttribute("USER");
        return acc.getUsername().equals("root");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!isRoot(request)) {
            response.getOutputStream().print("Error: You have no permission to access this page");
            return;
        }

        String submitButton = request.getParameter("submitButton");
        try {
            if (submitButton.equals("lock")) {
                String lockUsername = request.getParameter("lockUsername");
                auth.lock_account(lockUsername);
                request.setAttribute("errorMessage", "User locked successfully");
            }
            if (submitButton.equals("unlock")) {
                String unlockUsername = request.getParameter("unlockUsername");
                auth.unlock_account(unlockUsername);
                request.setAttribute("errorMessage", "User unlocked successfully");
            }
        }
        catch (EmptyFieldException | SQLException |UndefinedAccountException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }
        request.getRequestDispatcher("/WEB-INF/admin.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!isRoot(request)) {
            response.getOutputStream().print("Error: You have no permission to access this page");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/admin.jsp").forward(request,response);
    }
}