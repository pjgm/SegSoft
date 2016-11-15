package servlets;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import app.*;
import model.Account;

@WebServlet(name = "AbstractServlet")
public abstract class AbstractServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static final Logger LOGGER = Logger.getLogger(AbstractServlet.class.getName());

    Authenticator auth;
    private boolean isSetupDone;

    @Override
    public void init() {
        this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
        this.isSetupDone = (boolean) getServletContext().getAttribute("isSetupDone");
    }

    /**
     * Checks if the user associated with this session is logged in
     */
    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(session == null)
            return false;

        return session.getAttribute("USER") != null;
    }

    /**
     * Checks if the logged in user is root
     */
    boolean isRoot(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Account acc = (Account) session.getAttribute("USER");
        return acc.getUsername().equals("root");
    }
    //TODO: Servlet filters for auth/setup
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isSetupDone) {
            request.getRequestDispatcher("/WEB-INF/setup.jsp").forward(request, response);
            return;
        }

        if (!isLoggedIn(request)) {
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            return;
        }

        processGetRequest(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isSetupDone) {
            request.getRequestDispatcher("/WEB-INF/setup.jsp").forward(request, response);
            return;
        }

        if (!isLoggedIn(request)) {
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            return;
        }

        processPostRequest(request, response);
    }

    protected abstract void processGetRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    protected abstract void processPostRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}