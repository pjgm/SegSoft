package app;

import exceptions.*;
import model.Account;

import java.sql.SQLException;

public interface Authenticator {

	boolean isSetupDone() throws SQLException;

	void create_account(String name, String pwd1, String pwd2)
			throws SQLException, PasswordMismatchException, EmptyFieldException, ExistingAccountException;

	void delete_account(String name)
			throws SQLException, UndefinedAccountException, LockedAccountException, AccountConnectionException, ClassNotFoundException;

	Account get_account(String name) throws SQLException;

	void change_pwd(String name, String pwd1, String pwd2)
			throws SQLException, PasswordMismatchException, EmptyFieldException;

	Account login(String name, String pwd) throws SQLException, UndefinedAccountException, LockedAccountException,
			EmptyFieldException, AuthenticationErrorException;

	void logout(Account acc) throws SQLException;

}