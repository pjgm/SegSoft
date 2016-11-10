package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import app.*;

@WebServlet(name = "AbstractServlet")
public abstract class AbstractServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int SESSIONTIMEOUT = 15 * 60;

	protected static final Logger LOGGER = Logger.getLogger(AbstractServlet.class.getName());
	protected Authenticator auth = new AuthenticatorClass();

	private static final String PASSWORDPATTERN = "((?=.*[a-z]).{8,64})";

	private boolean isSetupDone() {
		try {
			return auth.isSetupDone();
		} catch (SQLException e) {
			System.err.println("Malformed SQL Query");
		}

		return true;
	}

	private Account sessionLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("USER") == null) {
			return null;
		}

		try {
			Account user = auth.login(request, response);
			LOGGER.log(Level.FINE, "LOGIN " + user.get_account_name());
			return user;
		} catch (AuthenticationErrorException e) {
			response.getOutputStream().print("Wrong login information associated with this session");
		} catch (SQLException e) {
			response.getOutputStream().print("Error in SQL query");
		} catch (UndefinedAccountException e) {
			response.getOutputStream().print("Account associated with current session does not exist");
		} catch (LockedAccountException e) {
			response.getOutputStream().print("Your Account is locked");
		} finally {
			auth.closeDatabaseConnection();
		}

		return null;
	}

	protected boolean isRoot(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session.getAttribute("USER").toString().equals("root"))
			return true;

		return false;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isSetupDone()) {
			request.getRequestDispatcher("/WEB-INF/setup.jsp").forward(request, response);
			return;
		}

		Account a = sessionLogin(request, response);
		if (a == null) {
			request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
			return;
		}

		processGetRequest(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!isSetupDone()) {
			String password = request.getParameter("password");

			Pattern p2 = Pattern.compile(PASSWORDPATTERN);
			Matcher m2 = p2.matcher(password);
			if (!m2.matches()) {
				request.getSession().setAttribute("errorMessage", "The password must contain at least 8 characters");
				response.sendRedirect(request.getHeader("Referer"));
				return;
			}

			try {
				auth.create_account("root", password, password);
				LOGGER.log(Level.FINE, "ROOT SETUP SUCCESSFUL");
				request.getSession().setAttribute("errorMessage", "Admin created successfully");
			} catch (SQLException e) {
				request.getSession().setAttribute("errorMessage", "Error in SQL query");
			} catch (PasswordMismatchException e) {
				request.getSession().setAttribute("errorMessage", "Passwords do not match");
			} catch (ExistingAccountException e) {
				request.getSession().setAttribute("errorMessage", "User already exists");
			} catch (EmptyFieldException e) {
				request.getSession().setAttribute("errorMessage", "A required field is empty");
			} finally {
				response.sendRedirect(request.getHeader("Referer"));
				auth.closeDatabaseConnection();
			}

			return;
		}

		Account a = sessionLogin(request, response);
		if (a == null) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			try {
				Account authUser = auth.login(username, password);
				LOGGER.log(Level.FINE, "LOGIN " + authUser.get_account_name());
				HttpSession session = request.getSession(true);
				session.setAttribute("USER", authUser.get_account_name());
				session.setAttribute("PWD", authUser.get_account_pwd());
				session.setMaxInactiveInterval(SESSIONTIMEOUT);
			} catch (SQLException e) {
				request.getSession().setAttribute("errorMessage", "Error in SQL query");
			} catch (UndefinedAccountException e) {
				request.getSession().setAttribute("errorMessage", "User does not exist");
			} catch (LockedAccountException e) {
				request.getSession().setAttribute("errorMessage", "Account is locked");
			} catch (EmptyFieldException e) {
				request.getSession().setAttribute("errorMessage", "A required field is empty");
			} catch (AuthenticationErrorException e) {
				request.getSession().setAttribute("errorMessage", "Wrong password");
			} finally {
				response.sendRedirect(request.getHeader("Referer"));
				auth.closeDatabaseConnection();
			}

			return;
		}

		processPostRequest(request, response);
	}

	protected abstract void processGetRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;

	protected abstract void processPostRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;
}