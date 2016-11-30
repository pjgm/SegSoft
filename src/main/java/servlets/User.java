package main.java.servlets;

import main.java.app.Authenticator;
import main.java.exceptions.UndefinedAccountException;
import main.java.model.Account;
import main.java.model.AccountClass;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(name = "User", urlPatterns = { "/User/*"} )
public class User extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(User.class.getName());
    private Authenticator auth;

    public void init() {
        this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String username = ((AccountClass) session.getAttribute("USER")).getUsername();

        String profileName = request.getPathInfo();

        if(profileName == null || profileName.length() == 1) {
            response.sendRedirect("/Friends");
            return;
        }
        profileName = profileName.substring(1);

        boolean isFriend = false;
        List<String> friendList = null;

        try {
            friendList = auth.get_friends(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String friend: friendList) {
            if (friend.equals(profileName)) {
                isFriend = true;
                break;
            }
        }

        try {
            Account acc = auth.get_account(profileName);
            if(acc.getLocked() == 1) {
                response.sendRedirect("/Friends");
                return;
            }
            String userInfo;


            if (isFriend)
                userInfo = acc.getUsername() + "<br/>" + acc.getEmail() + "<br/>" + acc.getPhone() + "<br/>"
                    + acc.getBio() + "<br/>";
            else
                userInfo = acc.getUsername() + "<br/>" + acc.getEmail() + "<br/>" + acc.getPhone() + "<br/>";

            request.setAttribute("msg", userInfo);
        } catch (SQLException | UndefinedAccountException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/user.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
