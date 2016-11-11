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

import exceptions.EmptyFieldException;
import exceptions.ExistingAccountException;
import exceptions.PasswordMismatchException;

@WebServlet("/CreateUser")
public class CreateUser extends AbstractServlet {
	private static final long serialVersionUID = 1L;

	private static final String USERNAMEPATTERN = "^[a-z0-9]{2,16}$";
	private static final String PASSWORDPATTERN = "((?=.*[a-z]).{8,64})";

	@Override
	protected void processGetRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isRoot(request)) {
			response.getOutputStream().print("Error: You have no permission to access this page");
			return;
		}

		request.getRequestDispatcher("/WEB-INF/createuser.jsp").forward(request, response);
	}

	@Override
	protected void processPostRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isRoot(request)) {
			response.getOutputStream().print("Error: You have no permission to access this page");
			return;
		}

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");

		Pattern p1 = Pattern.compile(USERNAMEPATTERN);
		Matcher m1 = p1.matcher(username);
		if (!m1.matches()) {
			request.setAttribute("errorMessage", "Username has invalid format");
			request.getRequestDispatcher("/WEB-INF/createuser.jsp").forward(request, response);
			return;
		}

		Pattern p2 = Pattern.compile(PASSWORDPATTERN);
		Matcher m2 = p2.matcher(password);
		if (!m2.matches()) {
			request.setAttribute("errorMessage", "Password has invalid format");
			request.getRequestDispatcher("/WEB-INF/createuser.jsp").forward(request, response);
			return;
		}

		try {
			LOGGER.log(Level.FINE, "CREATED ACCOUNT " + username);
			auth.create_account(username, password, password2);
			request.setAttribute("errorMessage", "User created successfully");
		} catch (SQLException | PasswordMismatchException | ExistingAccountException | EmptyFieldException e) {
			request.setAttribute("errorMessage", e.getMessage());
		}
		finally {
			request.getRequestDispatcher("/WEB-INF/createuser.jsp").forward(request, response);
			auth.closeDatabaseConnection();
		}
	}
}