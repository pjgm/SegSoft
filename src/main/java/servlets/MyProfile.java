package main.java.servlets;

import main.java.app.Authenticator;
import main.java.exceptions.EmptyFieldException;
import main.java.exceptions.UndefinedAccountException;
import main.java.model.Account;
import main.java.model.AccountClass;
import main.java.validation.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebServlet(name = "MyProfile", urlPatterns = { "/MyProfile" })
public class MyProfile extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MyProfile.class.getName());
    private Authenticator auth;
    private Validator val;

    public void init() {
        this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
        this.val = new Validator();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Account acc = (AccountClass) session.getAttribute("USER");
        String username = acc.getUsername();

        try {
            acc = auth.get_account(username);
        } catch (SQLException | UndefinedAccountException e) {
            e.printStackTrace();
        }

        request.setAttribute("username", username);

        String info = "<table><tr><th>Name</th><th>Email</th><th>Phone</th><th>Bio</th><th>Private Info</th></tr>";
        info += "<tr>";
        info += "<td>" + "<a href=\"/User/" + username + "\">" + username + "</a>" + "</td>";
        info += "<td>" + acc.getEmail() + "</td>";
        info += "<td>" + acc.getPhone() + "</td>";
        info += "<td>" + acc.getBio() + "</td>";
        info += "<td>" + acc.getSecretInfo() + "</td>";
        info += "</tr>";
        info += "</table>";

        request.setAttribute("info", info);
        request.getRequestDispatcher("/WEB-INF/myprofile.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String submitButton = request.getParameter("submitButton");

        HttpSession session = request.getSession(false);
        Account acc = (AccountClass) session.getAttribute("USER");
        String username = acc.getUsername();

        try {
            if (submitButton.equals("phone")) {
                String newPhone = request.getParameter("phone");
                if (val.validatePhone(newPhone)) {
                    auth.change_phone(username, newPhone);
                    request.setAttribute("errorMessage", "Phone changed successfully");
                }
                else
                    request.setAttribute("errorMessage", "Phone has invalid format");
            }
            if (submitButton.equals("email")) {
                String newEmail = request.getParameter("email");
                if (val.validateEmail(newEmail)) {
                    auth.change_email(username, newEmail);
                    request.setAttribute("errorMessage", "Email changed successfully");
                }
                else
                    request.setAttribute("errorMessage", "Email has invalid format");
            }
            if (submitButton.equals("bio")) {
                String newBio = request.getParameter("bio");
                auth.change_bio(username, newBio);
                request.setAttribute("errorMessage", "Bio changed successfully");
            }
            if (submitButton.equals("private")) {
                String newSecretInfo = request.getParameter("secretInfo");
                auth.change_secretInfo(username, newSecretInfo);
                request.setAttribute("errorMessage", "Secret info changed successfully");
            }
        }
        catch (EmptyFieldException | SQLException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        doGet(request, response);
    }
}
