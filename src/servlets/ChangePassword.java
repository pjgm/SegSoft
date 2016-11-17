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
import exceptions.EmptyFieldException;
import exceptions.PasswordMismatchException;
import model.Account;
import model.AccountClass;
import validator.Validator;

@WebServlet(name = "ChangePassword", urlPatterns = { "/ChangePassword" })
public class ChangePassword extends HttpServlet {

	private Authenticator auth;
	private static final Logger LOGGER = Logger.getLogger(ChangePassword.class.getName());
	private Validator val;

	@Override
	public void init() {
		this.auth = (Authenticator) getServletContext().getAttribute("authenticator");
		this.val = new Validator();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/changepassword.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");

		if (!val.validatePassword(password)) {
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
		} catch (SQLException | PasswordMismatchException | EmptyFieldException e) {
			request.setAttribute("errorMessage", e.getMessage());
		} finally {
			request.getRequestDispatcher("/WEB-INF/changepassword.jsp").forward(request, response);
		}
	}
}