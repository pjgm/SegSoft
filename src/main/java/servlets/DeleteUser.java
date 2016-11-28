package servlets;

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

import app.Authenticator;
import exceptions.AccountConnectionException;
import exceptions.LockedAccountException;
import exceptions.UndefinedAccountException;
import model.Account;

@WebServlet(name = "DeleteUser", urlPatterns = { "/DeleteUser" })
public class DeleteUser extends HttpServlet {

	private static final Logger LOGGER = Logger.getLogger(DeleteUser.class.getName());
	private Authenticator auth;

	public void init() {
		this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
	}

	private boolean isRoot(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Account acc = (Account) session.getAttribute("USER");
		return acc.getUsername().equals("root");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (!isRoot(request)) {
			response.getOutputStream().print("Error: You have no permission to access this page");
			return;
		}
		request.getRequestDispatcher("/WEB-INF/deleteuser.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (!isRoot(request)) {
			response.getOutputStream().print("Error: You have no permission to access this page");
			return;
		}

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