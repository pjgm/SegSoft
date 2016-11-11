package app;

import exceptions.*;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Authenticator {
	public void closeDatabaseConnection();

	boolean isSetupDone() throws SQLException;

	void create_account(String name, String pwd1, String pwd2)
			throws SQLException, PasswordMismatchException, ExistingAccountException, EmptyFieldException;

	void delete_account(String name)
			throws SQLException, UndefinedAccountException, LockedAccountException, AccountConnectionException;

	Account get_account(String name) throws SQLException, UndefinedAccountException;

	void change_pwd(String name, String pwd1, String pwd2)
			throws SQLException, PasswordMismatchException, EmptyFieldException;

	Account login(String name, String pwd) throws SQLException, UndefinedAccountException, LockedAccountException,
			EmptyFieldException, AuthenticationErrorException;

	void logout(Account acc) throws SQLException;

	public Account login(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, UndefinedAccountException, LockedAccountException, AuthenticationErrorException;
}