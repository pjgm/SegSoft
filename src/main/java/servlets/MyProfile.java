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

        String info = "<table><tr><th>Name</th><th>Email</th><th>Phone</th><th>Public Info</th>" +
                "<th>Bio</th><th>Private Info</th></tr>";
        info += "<tr>";
        info += "<td>" + "<a href=\"/User/" + username + "\">" + username + "</a>" + "</td>";
        info += "<td>" + acc.getEmail() + "</td>";
        info += "<td>" + acc.getPhone() + "</td>";
        info += "<td>" + acc.getPublicInfo() + "</td>";
        info += "<td>" + acc.getInternalInfo() + "</td>";
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
            if (submitButton.equals("email")) {
                String newEmail = request.getParameter("email");
                if (val.validateEmail(newEmail)) {
                    auth.change_email(username, newEmail);
                    request.setAttribute("errorMessage", "Email changed successfully");
                }
                else
                    request.setAttribute("errorMessage", "Email has invalid format");
            }
            if (submitButton.equals("phone")) {
                String newPhone = request.getParameter("phone");
                if (val.validatePhone(newPhone)) {
                    auth.change_phone(username, newPhone);
                    request.setAttribute("errorMessage", "Phone changed successfully");
                }
                else
                    request.setAttribute("errorMessage", "Phone has invalid format");
            }
            if (submitButton.equals("public")) {
                String newPublicInfo = request.getParameter("publicInfo");
                auth.change_publicInfo(username, newPublicInfo);
                request.setAttribute("errorMessage", "Public info changed successfully");
            }
            if (submitButton.equals("internal")) {
                String newInternalInfo = request.getParameter("internalInfo");
                auth.change_internalInfo(username, newInternalInfo);
                request.setAttribute("errorMessage", "Internal info changed successfully");
            }
            if (submitButton.equals("secret")) {
                String newSecretInfo = request.getParameter("secretInfo");
                auth.change_secretInfo(username, newSecretInfo);
                request.setAttribute("errorMessage", "Secret info changed successfully");
            }
            if (submitButton.equals("emailpl")) {
                String newEmailPL = request.getParameter("emailPL");
                if(newEmailPL.equals("public")) {
                    auth.change_email_privacy_level(username, "public");
                    request.setAttribute("errorMessage", "Email is now public");
                }
                else if(newEmailPL.equals("internal")) {
                    auth.change_email_privacy_level(username, "internal");
                    request.setAttribute("errorMessage", "Email is now internal");
                }
                else if(newEmailPL.equals("private")) {
                    auth.change_email_privacy_level(username, "private");
                    request.setAttribute("errorMessage", "Email is now private");
                }
                else
                    request.setAttribute("errorMessage", "Invalid input: please insert either public, internal or private");
            }
            if (submitButton.equals("phonepl")) {
                String newPhonePL = request.getParameter("phonePL");
                if(newPhonePL.equals("public")) {
                    auth.change_phone_privacy_level(username, "public");
                    request.setAttribute("errorMessage", "Phone is now public");
                }
                else if(newPhonePL.equals("internal")) {
                    auth.change_phone_privacy_level(username, "internal");
                    request.setAttribute("errorMessage", "Phone is now internal");
                }
                else if(newPhonePL.equals("private")) {
                    auth.change_phone_privacy_level(username, "private");
                    request.setAttribute("errorMessage", "Phone is now private");
                }
                else
                    request.setAttribute("errorMessage", "Invalid input: please insert either public, internal or private");
            }
            if (submitButton.equals("pipl")) {
                String newPublicInfoPL = request.getParameter("publicInfoPL");
                if(newPublicInfoPL.equals("public")) {
                    auth.change_publicInfo_privacy_level(username, "public");
                    request.setAttribute("errorMessage", "Public info is now public");
                }
                else if(newPublicInfoPL.equals("internal")) {
                    auth.change_publicInfo_privacy_level(username, "internal");
                    request.setAttribute("errorMessage", "Public info is now internal");
                }
                else if(newPublicInfoPL.equals("private")) {
                    auth.change_publicInfo_privacy_level(username, "private");
                    request.setAttribute("errorMessage", "Public info is now private");
                }
                else
                    request.setAttribute("errorMessage", "Invalid input: please insert either public, internal or private");
            }
            if (submitButton.equals("iipl")) {
                String newInternalInfoPL = request.getParameter("internalInfoPL");
                if(newInternalInfoPL.equals("public")) {
                    auth.change_internalInfo_privacy_level(username, "public");
                    request.setAttribute("errorMessage", "Internal info is now public");
                }
                else if(newInternalInfoPL.equals("internal")) {
                    auth.change_internalInfo_privacy_level(username, "internal");
                    request.setAttribute("errorMessage", "Internal info is now internal");
                }
                else if(newInternalInfoPL.equals("private")) {
                    auth.change_internalInfo_privacy_level(username, "private");
                    request.setAttribute("errorMessage", "Internal info is now private");
                }
                else
                    request.setAttribute("errorMessage", "Invalid input: please insert either public, internal or private");
            }
            if (submitButton.equals("sipl")) {
                String newSecretInfoPL = request.getParameter("secretInfoPL");
                if(newSecretInfoPL.equals("public")) {
                    auth.change_secretInfo_privacy_level(username, "public");
                    request.setAttribute("errorMessage", "Secret info is now public");
                }
                else if(newSecretInfoPL.equals("internal")) {
                    auth.change_secretInfo_privacy_level(username, "internal");
                    request.setAttribute("errorMessage", "Secret info is now internal");
                }
                else if(newSecretInfoPL.equals("private")) {
                    auth.change_secretInfo_privacy_level(username, "private");
                    request.setAttribute("errorMessage", "Secret info is now private");
                }
                else
                    request.setAttribute("errorMessage", "Invalid input: please insert either public, internal or private");
            }
        } catch (EmptyFieldException | SQLException e) {
            request.setAttribute("errorMessage", e.getMessage());
        }

        doGet(request, response);
    }
}