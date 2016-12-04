package main.java.app;

import main.java.exceptions.*;
import main.java.model.Account;

import java.sql.SQLException;
import java.util.List;

public interface Authenticator {

	boolean isSetupDone() throws SQLException;

	void create_account(String name, String pwd1, String pwd2, String email, String phone, String role)
			throws SQLException, PasswordMismatchException, EmptyFieldException;

	void delete_account(String name)
			throws SQLException, UndefinedAccountException, LockedAccountException, AccountConnectionException;

	Account get_account(String name) throws SQLException, UndefinedAccountException;

	void change_pwd(String name, String pwd1, String pwd2)
			throws SQLException, PasswordMismatchException, EmptyFieldException;

	void lock_account(String name) throws SQLException, UndefinedAccountException, EmptyFieldException;

	void unlock_account(String name) throws SQLException, UndefinedAccountException, EmptyFieldException;

	void add_friend(String username, String friendName, int status)
			throws SQLException, UndefinedAccountException, SelfFriendException, EmptyFieldException;

	void remove_friend(String username, String friendName) throws SQLException,
			UndefinedFriendException, UndefinedAccountException, SelfFriendException, EmptyFieldException;

	boolean isFriend(String username, String friendName) throws SQLException;

	List<String> get_friends(String name) throws SQLException;

	void accept_friend_request(String username, String friendName) throws SQLException;

	List<String> get_pending_friends(String name) throws SQLException;

	void change_email(String username, String email) throws SQLException, EmptyFieldException;

	void change_email_privacy_level(String username, String lvl) throws SQLException, EmptyFieldException;

	void change_phone(String username, String phone) throws SQLException, EmptyFieldException;

	void change_phone_privacy_level(String username, String lvl) throws SQLException, EmptyFieldException;

	void change_publicInfo(String username, String publicInfo) throws SQLException, EmptyFieldException;

	void change_publicInfo_privacy_level(String username, String lvl) throws SQLException, EmptyFieldException;

	void change_internalInfo(String username, String internalInfo) throws SQLException, EmptyFieldException;

	void change_internalInfo_privacy_level(String username, String lvl) throws SQLException, EmptyFieldException;

	void change_secretInfo(String username, String secretInfo) throws SQLException, EmptyFieldException;

	void change_secretInfo_privacy_level(String username, String lvl) throws SQLException, EmptyFieldException;

	Account login(String name, String pwd) throws SQLException,
			UndefinedAccountException, LockedAccountException, EmptyFieldException, AuthenticationErrorException;

	void logout(Account acc) throws SQLException;
}