package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import app.Authenticator;
import exceptions.EmptyFieldException;
import exceptions.PasswordMismatchException;
import model.Account;
import model.AccountClass;

@WebServlet(name = "ChangePassword", urlPatterns = { "/ChangePassword" })
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Authenticator auth;
	private static final Logger LOGGER = Logger.getLogger(ChangePassword.class.getName());
	private static final String PASSWORDPATTERN = "((?=.*[a-z]).{8,64})";

	@Override
	public void init() {
		this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/changepassword.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");

		Pattern p2 = Pattern.compile(PASSWORDPATTERN);
		Matcher m2 = p2.matcher(password);
		if (!m2.matches()) {
			request.setAttribute("errorMessage", "Password has invalid format");
			request.getRequestDispatcher("/WEB-INF/changepassword.jsp").forward(request, response);
			return;
		}

		try {
			HttpSession session = request.getSession(false);
			Account acc = (AccountClass) session.getAttribute("USER");
			String username = acc.getUsername();
			auth.change_pwd(username, password, password2);
			LOGGER.log(Level.FINE, "PASSWORD CHANGE FOR USER " + username);
			auth.logout(acc);
			session.invalidate();
			request.setAttribute("errorMessage", "Password changed sucessfully");
		} catch (SQLException | PasswordMismatchException | EmptyFieldException | ClassNotFoundException e) {
			request.setAttribute("errorMessage", e.getMessage());
		} finally {
			request.getRequestDispatcher("/WEB-INF/changepassword.jsp").forward(request, response);
		}
	}
}