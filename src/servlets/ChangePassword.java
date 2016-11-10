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
import javax.servlet.http.HttpSession;

import app.*;

@WebServlet("/ChangePassword")
public class ChangePassword extends AbstractServlet {
	private static final long serialVersionUID = 1L;

	private static final String USERNAMEPATTERN = "^[a-z0-9]{2,16}$";
	private static final String PASSWORDPATTERN = "((?=.*[a-z]).{8,64})";

	@Override
	protected void processGetRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/changepassword.jsp").forward(request, response);
	}

	@Override
	protected void processPostRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");

		Pattern p1 = Pattern.compile(USERNAMEPATTERN);
		Matcher m1 = p1.matcher(username);
		if (!m1.matches()) {
			request.getSession().setAttribute("errorMessage", "Username has invalid format");
			return;
		}

		Pattern p2 = Pattern.compile(PASSWORDPATTERN);
		Matcher m2 = p2.matcher(password);
		if (!m2.matches()) {
			request.getSession().setAttribute("errorMessage", "Password has invalid format");
			return;
		}

		try {
			auth.change_pwd(username, password, password2);
			LOGGER.log(Level.FINE, "PASSWORD CHANGE FOR USER " + username);
			HttpSession session = request.getSession();
			session.invalidate();
			request.getSession().setAttribute("errorMessage", "Password changed sucessfully");
		} catch (SQLException e) {
			request.getSession().setAttribute("errorMessage", "Error in SQL query");
		} catch (PasswordMismatchException e) {
			request.getSession().setAttribute("errorMessage", "Passwords do not match");
		} catch (EmptyFieldException e) {
			request.getSession().setAttribute("errorMessage", "A required field is empty");
		} finally {
			response.sendRedirect(request.getHeader("Referer"));
			auth.closeDatabaseConnection();
		}
	}
}