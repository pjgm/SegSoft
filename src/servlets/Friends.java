package servlets;

import app.Authenticator;
import model.Account;
import model.AccountClass;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet(name = "Friends", urlPatterns = { "/Friends" })
public class Friends extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Friends.class.getName());
    private Authenticator auth;

    public void init() {
        this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO: fix bad code
        try {
            HttpSession session = request.getSession(false);
            Account acc = (AccountClass) session.getAttribute("USER");
            String username = acc.getUsername();
            String friendList = "<table>";
            List<String> flist = auth.get_friends(username);

            for (String friend : flist)
                friendList += "<tr><td>" + friend + "</td></tr>";

            friendList += "</table>";
            request.setAttribute("friendlist", friendList);

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            request.getRequestDispatcher("/WEB-INF/friends.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String friendName = request.getParameter("username");
        HttpSession session = request.getSession(false);
        Account acc = (Account) session.getAttribute("USER");
        try {
            auth.add_friend(acc.getUsername(), friendName);
            LOGGER.log(Level.FINE, acc.getUsername() + "ADDED FRIEND " + friendName);
            request.setAttribute("errorMessage", "Friend added successfully");
        } catch (SQLException e) {
            request.setAttribute("errorMessage", e.getMessage());
        } finally {
            doGet(request, response);
        }
    }
}
