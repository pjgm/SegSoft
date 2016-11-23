package app;

import crypto.PasswordHashGenerator;
import exceptions.*;
import model.Account;
import model.AccountClass;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import java.sql.SQLException;
import java.util.List;

public class AuthenticatorClass implements Authenticator {

    private static final String CREATEACCTABLESQL = "create table if not exists account (username string primary key," +
            " password string, email string, phone string, bio string, secretinfo string, role string, loggedIn " +
            "integer, locked integer, salt string)";
    private static final String CREATEFRIENDTABLESQL = "create table if not exists friend (username string, " +
            "friendname string, primary key(username, friendname), foreign key(username) references account(username)" +
            ", foreign key (friendname) references account(username), check (username != friendname))";
    private static final String SELECTBYNAMESQL = "select * from account where username LIKE ?";
    private static final String INSERTUSERSQL = "insert into account (username, password, email, phone, bio, " +
            "secretinfo, role, loggedIn, locked, salt) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETEBYNAMESQL = "delete from account where username LIKE ?";
    private static final String UPDATEPASSWORDSQL = "update account set password = ?, salt = ? where username LIKE ?";
    private static final String LOGINBYNAMESQL = "update account set loggedIn = 1 where username LIKE ?";
    private static final String LOGOUTBYNAMESQL = "update account set loggedIn = 0 where username LIKE ?";
    private static final String GETFRIENDSSQL = "select * from friend where username = ?";
    private static final String ADDFRIENDSQL = "insert into friend (username, friendname) values (?, ?)";

    private QueryRunner qr;
    private ResultSetHandler rsh;

    public AuthenticatorClass(BasicDataSource dataSource) throws SQLException {
        this.qr = new QueryRunner(dataSource);
        qr.update(CREATEACCTABLESQL);
        qr.update(CREATEFRIENDTABLESQL);
        rsh = new BeanHandler<>(AccountClass.class);
    }

    public boolean isSetupDone() throws SQLException {
        return account_exists("root");
    }

    private boolean account_exists(String name) throws SQLException {
        Account acc = (AccountClass) qr.query(SELECTBYNAMESQL, rsh, name);
        return acc != null;
    }

    public void create_account(String name, String pwd1, String pwd2, String email, String phone, String role) throws
            SQLException, PasswordMismatchException, EmptyFieldException, ExistingAccountException {

        if (name == null || name.isEmpty() || pwd1.isEmpty() || pwd2.isEmpty() || role.isEmpty() || email.isEmpty()
                || phone.isEmpty())
            throw new EmptyFieldException();

        if (!pwd1.equals(pwd2))
            throw new PasswordMismatchException();

        PasswordHashGenerator phg = new PasswordHashGenerator(pwd1);
        pwd1 = phg.getHash();
        String salt = phg.getSalt();

        Account acc = (AccountClass) qr.insert(INSERTUSERSQL, rsh, name, pwd1, email, phone, "Hello my name is " + name
                + " and this is my bio", "My private information goes here", role, 0, 0, salt);

        if (acc == null)
            throw new ExistingAccountException();
    }

    public void delete_account(String name) throws SQLException, UndefinedAccountException, LockedAccountException, AccountConnectionException {
        Account acc = get_account(name);
        if (acc == null)
            throw new UndefinedAccountException();
        if (acc.getLoggedIn() == 1)
            throw new AccountConnectionException();
        if (acc.getLocked() == 0)
            throw new LockedAccountException("The account is not locked");

        qr.update(DELETEBYNAMESQL, name);
    }

    public Account get_account(String name) throws SQLException, UndefinedAccountException {
        Account acc = (AccountClass) qr.query(SELECTBYNAMESQL, rsh, name);
        if (acc == null)
            throw new UndefinedAccountException();
        return acc;
    }

    public void change_pwd(String name, String pwd1, String pwd2) throws SQLException, PasswordMismatchException, EmptyFieldException {
        if (name.isEmpty() || pwd1.isEmpty() || pwd2.isEmpty())
            throw new EmptyFieldException();

        if (!pwd1.equals(pwd2))
            throw new PasswordMismatchException();

        PasswordHashGenerator phg = new PasswordHashGenerator(pwd1);
        pwd1 = phg.getHash();
        String salt = phg.getSalt();

        qr.update(UPDATEPASSWORDSQL, pwd1, salt, name);
    }

    public void add_friend(String username, String friendName) throws SQLException {
        //TODO: what resultsethandler to use?
        qr.insert(ADDFRIENDSQL, new ColumnListHandler<String>(), username, friendName);
    }

    //TODO: improve code
    public List<String> get_friends(String name) throws SQLException {
        return qr.query(GETFRIENDSSQL, new ColumnListHandler<String>("friendname"), name);
    }

    public Account login(String name, String pwd) throws SQLException, UndefinedAccountException, LockedAccountException, EmptyFieldException, AuthenticationErrorException {
        if (name == null || pwd == null)
            throw new AuthenticationErrorException();

        if (name.isEmpty() || pwd.isEmpty())
            throw new EmptyFieldException();

        Account acc = (AccountClass) qr.query(SELECTBYNAMESQL, rsh, name);

        if (acc == null)
            throw new UndefinedAccountException();

        PasswordHashGenerator phg = new PasswordHashGenerator(pwd);
        String salt = acc.getSalt();
        pwd = phg.createNewHash(salt, pwd);

        if (!acc.getPassword().equals(pwd))
            throw new AuthenticationErrorException();

        if(acc.getLocked() == 1)
            throw new LockedAccountException("This account is locked");

        qr.update(LOGINBYNAMESQL, name);

        return acc;
    }

    public void logout(Account acc) throws SQLException {
        qr.update(LOGOUTBYNAMESQL, acc.getUsername());
    }
}