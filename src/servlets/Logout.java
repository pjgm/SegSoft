package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import app.Authenticator;
import model.Account;
import model.AccountClass;

@WebServlet(name = "Logout", urlPatterns = { "/Logout" })
public class Logout extends HttpServlet {

	private static final Logger LOGGER = Logger.getLogger(Logout.class.getName());
	private Authenticator auth;

	public void init() {
		this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/logout.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		Account acc = (AccountClass) session.getAttribute("USER");

		try {
			auth.logout(acc);
			session.invalidate();
			LOGGER.log(Level.FINE, "LOGOUT " + acc.getUsername());
			request.setAttribute("errorMessage", "Logout successful");
		} catch (SQLException e) {
			request.setAttribute("errorMessage", e.getMessage());
		} finally {
			request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
		}
	}
}