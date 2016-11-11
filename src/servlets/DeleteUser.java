package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.AccountConnectionException;
import exceptions.LockedAccountException;
import exceptions.UndefinedAccountException;

@WebServlet("/DeleteUser")
public class DeleteUser extends AbstractServlet {
	private static final long serialVersionUID = 1L;

	private static final String USERNAMEPATTERN = "^[a-z0-9]{2,16}$";

	@Override
	protected void processGetRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isRoot(request)) {
			response.getOutputStream().print("Error: You have no permission to access this page");
			return;
		}

		request.getRequestDispatcher("/WEB-INF/deleteuser.jsp").forward(request, response);
	}

	@Override
	protected void processPostRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isRoot(request)) {
			response.getOutputStream().print("Error: You have no permission to access this page");
			return;
		}

		String username = request.getParameter("username");

		Pattern p1 = Pattern.compile(USERNAMEPATTERN);
		Matcher m1 = p1.matcher(username);
		if (!m1.matches()) {
			request.setAttribute("errorMessage", "Username has invalid format");
			request.getRequestDispatcher("/WEB-INF/deleteuser.jsp").forward(request, response);
			return;
		}

		try {
			auth.delete_account(username);
			LOGGER.log(Level.FINE, "DELETED ACCOUNT " + username);
			request.setAttribute("errorMessage", "User deleted successfully");
		} catch (SQLException | UndefinedAccountException | LockedAccountException | AccountConnectionException | ClassNotFoundException e) {
			request.setAttribute("errorMessage", e.getMessage());
		} finally {
			request.getRequestDispatcher("/WEB-INF/deleteuser.jsp").forward(request, response);
        }
    }
}