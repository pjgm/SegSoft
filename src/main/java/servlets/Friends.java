package main.java.servlets;

import main.java.app.Authenticator;
import main.java.exceptions.EmptyFieldException;
import main.java.exceptions.SelfFriendRequestException;
import main.java.exceptions.UndefinedAccountException;
import main.java.exceptions.UndefinedFriendException;
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
import java.util.ArrayList;
import java.util.Enumeration;
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
            String pendingFriendList = "<table><tr><th>Name</th><th>Action</th></tr>";
            String friendList = "<table><tr><th>Name</th></tr>";
            List<String> plist = auth.get_pending_friends(username);
            List<String> flist = auth.get_friends(username);

            for (String friend: plist) {
                Account account = auth.get_account(friend);
                if (account.getLocked() == 1)
                    continue;

                pendingFriendList += "<tr>";
                pendingFriendList += "<td><a href=\"/User/" + friend + "\">" + friend + "</a>" + "</td>";
                pendingFriendList += "<td>" + "<input type=\"submit\" name=\"submitButton\" value=\"Accept-" +
                        friend + "\"/>" + "<input type=\"submit\" name=\"submitButton\" value=\"Decline\"/></td>";
                pendingFriendList += "</tr>";
            }

            pendingFriendList += "</table>";
            request.setAttribute("pendingFriendList", pendingFriendList);

            for (String friend : flist) {
                Account account = auth.get_account(friend);
                if (account.getLocked() == 1)
                    continue;

                friendList += "<tr>";
                friendList += "<td>" + "<a href=\"/User/" + friend + "\">" + friend + "</a>" + "</td>";
                friendList += "</tr>";
            }

            friendList += "</table>";
            request.setAttribute("friendlist", friendList);

        } catch (SQLException | NullPointerException | UndefinedAccountException e) {
            e.printStackTrace();
        } finally {
            request.getRequestDispatcher("/WEB-INF/friends.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String submitButton = request.getParameter("submitButton");

        HttpSession session = request.getSession(false);
        Account acc = (Account) session.getAttribute("USER");

        if(submitButton != null) {
            String friendName = submitButton.split("-")[1];
            if (submitButton.contains("Accept")) {
                try {
                    auth.add_friend(acc.getUsername(), friendName, 1);
                    auth.accept_friend_request(friendName, acc.getUsername());
                } catch (SQLException | UndefinedAccountException | SelfFriendRequestException | EmptyFieldException e) {
                    request.setAttribute("errorMessage", e.getMessage());
                }
            }
            if (submitButton.contains("Decline")) {
                try {
                    auth.remove_friend(friendName, acc.getUsername());
                } catch (SQLException | UndefinedFriendException | UndefinedAccountException | SelfFriendRequestException | EmptyFieldException e) {
                    request.setAttribute("errorMessage", e.getMessage());
                }
            }

            doGet(request, response);
        }

        String submit = request.getParameter("submitButton2");

        if (submit.equals("add")) {
            try {
                String friendName = request.getParameter("usernameAdd");
                auth.add_friend(acc.getUsername(), friendName, 0);
                LOGGER.log(Level.FINE, acc.getUsername() + "ADDED FRIEND " + friendName);
                request.setAttribute("errorMessage", "Friend request sent successfully");
            } catch(SQLException e){
                request.setAttribute("errorMessage", "User already added/friend request already sent");
            } catch (UndefinedAccountException | SelfFriendRequestException | EmptyFieldException e) {
                request.setAttribute("errorMessage", e.getMessage());
            }
        }
        if (submit.equals("remove")) {
            try {
                String friendName = request.getParameter("usernameRemove");
                auth.remove_friend(acc.getUsername(), friendName);
                auth.remove_friend(friendName, acc.getUsername());
                LOGGER.log(Level.FINE, acc.getUsername() + "REMOVED FRIEND " + friendName);
                request.setAttribute("errorMessage2", "Friend removed successfully");
            } catch (SQLException | UndefinedFriendException | UndefinedAccountException | SelfFriendRequestException | EmptyFieldException e) {
                request.setAttribute("errorMessage2", e.getMessage());
            }
        }

        doGet(request, response);
    }
}