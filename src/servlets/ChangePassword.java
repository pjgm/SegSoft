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

import exceptions.EmptyFieldException;
import exceptions.PasswordMismatchException;

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

	//TODO: User gets logged out after password change, fix?
	@Override
	protected void processPostRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");

		Pattern p1 = Pattern.compile(USERNAMEPATTERN);
		Matcher m1 = p1.matcher(username);
		if (!m1.matches()) {
			request.setAttribute("errorMessage", "Username has invalid format");
			request.getRequestDispatcher("/WEB-INF/changepassword.jsp").forward(request, response);
			return;
		}

		Pattern p2 = Pattern.compile(PASSWORDPATTERN);
		Matcher m2 = p2.matcher(password);
		if (!m2.matches()) {
			request.setAttribute("errorMessage", "Password has invalid format");
			request.getRequestDispatcher("/WEB-INF/changepassword.jsp").forward(request, response);
			return;
		}

		try {
			auth.change_pwd(username, password, password2);
			LOGGER.log(Level.FINE, "PASSWORD CHANGE FOR USER " + username);
			HttpSession session = request.getSession();
			session.invalidate();
			request.setAttribute("errorMessage", "Password changed sucessfully");
		} catch (SQLException | PasswordMismatchException | EmptyFieldException e) {
			request.setAttribute("errorMessage", e.getMessage());
		} finally {
			request.getRequestDispatcher("/WEB-INF/changepassword.jsp").forward(request, response);
			auth.closeDatabaseConnection();
		}
	}
}