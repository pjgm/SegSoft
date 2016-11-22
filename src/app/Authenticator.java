package app;

import exceptions.*;
import model.Account;

import java.sql.SQLException;
import java.util.List;

public interface Authenticator {

	boolean isSetupDone() throws SQLException;

	void create_account(String name, String pwd1, String pwd2, String role)
			throws SQLException, PasswordMismatchException, EmptyFieldException, ExistingAccountException;

	void delete_account(String name)
			throws SQLException, UndefinedAccountException, LockedAccountException, AccountConnectionException;

	Account get_account(String name) throws SQLException;

	void change_pwd(String name, String pwd1, String pwd2)
			throws SQLException, PasswordMismatchException, EmptyFieldException;

	void add_friend(String username, String friendName) throws SQLException;

	List<String> get_friends(String name) throws SQLException;

	Account login(String name, String pwd) throws SQLException, UndefinedAccountException, LockedAccountException,
			EmptyFieldException, AuthenticationErrorException;

	void logout(Account acc) throws SQLException;


}