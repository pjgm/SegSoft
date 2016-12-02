package main.java.servlets;

import main.java.app.Authenticator;
import main.java.exceptions.AccountConnectionException;
import main.java.exceptions.LockedAccountException;
import main.java.exceptions.UndefinedAccountException;
import main.java.model.Account;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "DeleteUser", urlPatterns = { "/DeleteUser" })
public class DeleteUser extends HttpServlet {

	private static final Logger LOGGER = Logger.getLogger(DeleteUser.class.getName());
	private Authenticator auth;

	public void init() {
		this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/deleteuser.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");

		try {
			auth.delete_account(username);
			LOGGER.log(Level.FINE, "DELETED ACCOUNT " + username);
			request.setAttribute("errorMessage", "User deleted successfully");
		} catch (SQLException | UndefinedAccountException | LockedAccountException | AccountConnectionException e) {
			request.setAttribute("errorMessage", e.getMessage());
		} finally {
			request.getRequestDispatcher("/WEB-INF/deleteuser.jsp").forward(request, response);
        }
    }
}