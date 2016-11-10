package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import app.*;

@WebServlet("/Logout")
public class Logout extends AbstractServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGetRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/logout.jsp").forward(request, response);
	}

	@Override
	protected void processPostRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String username = session.getAttribute("USER").toString();
		Account a;
		try {
			a = auth.get_account(username);
			auth.logout(a);
			session.invalidate();
			LOGGER.log(Level.FINE, "LOGOUT " + username);
			response.getOutputStream().print("Logout successful");
		} catch (SQLException e) {
			System.out.println("Error in SQL Query");
		} catch (UndefinedAccountException e) {
			System.out.println("Account doesn't exist, cannot logout");
		}
		finally {
			auth.closeDatabaseConnection();
		}
	}
}