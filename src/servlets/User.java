package servlets;

import app.Authenticator;
import exceptions.UndefinedAccountException;
import model.Account;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebServlet(name = "User", urlPatterns = { "/User/*"} )
public class User extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(User.class.getName());
    private Authenticator auth;

    public void init() {
        this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getPathInfo();

        if(username == null || username.length() == 1) {
            response.sendRedirect("/Friends");
            return;
        }
        username = username.substring(1);

        try {
            Account acc = auth.get_account(username);
            String userInfo = acc.getUsername() + "<br/>" + acc.getEmail() + "<br/>" + acc.getPhone() + "<br/>"
                    + acc.getBio() + "<br/>" + acc.getSecretInfo();
            request.setAttribute("msg", userInfo);
        } catch (SQLException | UndefinedAccountException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/user.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
