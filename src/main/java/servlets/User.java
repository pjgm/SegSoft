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
import java.util.logging.Logger;

@WebServlet(name = "User", urlPatterns = {"/User/*"})
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

        try {
            isFriend = auth.isFriend(username, profileName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Account acc = auth.get_account(profileName);
            if(acc.getLocked() == 1) {
                response.sendRedirect("/Friends");
                return;
            }

            String userInfo;

            if (isFriend) {
                userInfo = acc.getUsername() + "<br/>";
                if(!acc.getEmailPL().equals("private"))
                    userInfo += acc.getEmail() + "<br/>";
                if(!acc.getPhonePL().equals("private"))
                    userInfo += acc.getPhone() + "<br/>";
                if(!acc.getPIPL().equals("private"))
                    userInfo += acc.getPublicInfo() + "<br/>";
                if(!acc.getIIPL().equals("private"))
                    userInfo += acc.getInternalInfo() + "<br/>";
                if(!acc.getSIPL().equals("private"))
                    userInfo += acc.getSecretInfo() + "<br/>";
            }
            else {
                userInfo = acc.getUsername() + "<br/>";
                if(acc.getEmailPL().equals("public") || profileName.equals(username))
                    userInfo += acc.getEmail() + "<br/>";
                if(acc.getPhonePL().equals("public") || profileName.equals(username))
                    userInfo += acc.getPhone() + "<br/>";
                if(acc.getPIPL().equals("public") || profileName.equals(username))
                    userInfo += acc.getPublicInfo() + "<br/>";
                if(acc.getIIPL().equals("public") || profileName.equals(username))
                    userInfo += acc.getInternalInfo() + "<br/>";
                if(acc.getSIPL().equals("public") || profileName.equals(username))
                    userInfo += acc.getSecretInfo() + "<br/>";
            }

            request.setAttribute("msg", userInfo);
        } catch (SQLException | UndefinedAccountException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        request.getRequestDispatcher("/WEB-INF/user.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}