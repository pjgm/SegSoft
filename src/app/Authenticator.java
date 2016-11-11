package app;

import exceptions.*;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Authenticator {
	public void closeDatabaseConnection() throws SQLException;

	boolean isSetupDone() throws SQLException, ClassNotFoundException;

	void create_account(String name, String pwd1, String pwd2)
			throws SQLException, PasswordMismatchException, ExistingAccountException, EmptyFieldException, ClassNotFoundException;

	void delete_account(String name)
			throws SQLException, UndefinedAccountException, LockedAccountException, AccountConnectionException, ClassNotFoundException;

	Account get_account(String name) throws SQLException, UndefinedAccountException, ClassNotFoundException;

	void change_pwd(String name, String pwd1, String pwd2)
			throws SQLException, PasswordMismatchException, EmptyFieldException, ClassNotFoundException;

	Account login(String name, String pwd) throws SQLException, UndefinedAccountException, LockedAccountException,
			EmptyFieldException, AuthenticationErrorException, ClassNotFoundException;

	void logout(Account acc) throws SQLException, ClassNotFoundException;

	public Account login(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, UndefinedAccountException, LockedAccountException, AuthenticationErrorException, ClassNotFoundException;
}