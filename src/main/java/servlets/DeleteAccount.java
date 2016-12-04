package main.java.servlets;

import main.java.app.Authenticator;
import main.java.exceptions.AccountConnectionException;
import main.java.exceptions.LockedAccountException;
import main.java.exceptions.UndefinedAccountException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DeleteAccount", urlPatterns = {"/DeleteAccount"})
public class DeleteAccount extends HttpServlet {

	private static final Logger LOGGER = Logger.getLogger(DeleteAccount.class.getName());
	private Authenticator auth;

	public void init() {
		this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/deleteaccount.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");

		try {
			auth.delete_account(username);
			LOGGER.log(Level.FINE, "DELETED ACCOUNT " + username);
			request.setAttribute("errorMessage", "Account deleted successfully");
		} catch (SQLException | UndefinedAccountException | LockedAccountException | AccountConnectionException e) {
			request.setAttribute("errorMessage", e.getMessage());
		} finally {
			request.getRequestDispatcher("/WEB-INF/deleteaccount.jsp").forward(request, response);
        }
    }
}