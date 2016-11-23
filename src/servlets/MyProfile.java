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
import java.util.List;
import java.util.logging.Logger;

@WebServlet(name = "MyProfile", urlPatterns = { "/MyProfile" })
public class MyProfile extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MyProfile.class.getName());
    private Authenticator auth;

    public void init() {
        this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Account acc = (AccountClass) session.getAttribute("USER");
        String username = acc.getUsername();
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

    }
}
