package app;

import exceptions.*;
import model.Account;
import model.AccountClass;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public class Application implements Authenticator {

    private static final String CREATETABLESQL = "create table if not exists account (username string primary key, password string, loggedIn integer, locked integer, salt string)";
    private static final String SELECTBYNAMESQL = "select * from account where username LIKE ?";
    private static final String INSERTUSERSQL = "insert into account (username, password, loggedIn, locked, salt) values (?, ?, ?, ?, ?)";
    private static final String DELETEBYNAMESQL = "delete from account where username LIKE ?";
    private static final String UPDATEPWDSQL = "update account set password = ? where username LIKE ?";
    private static final String UPDATESALTSQL = "update account set salt = ? where username LIKE ?";
    private static final String LOGINBYNAMESQL = "update account set loggedIn = 1 where username LIKE ?";
    private static final String LOGOUTBYNAMESQL = "update account set loggedIn = 0 where username LIKE ?";

    private QueryRunner qr;
    private ResultSetHandler rsh;

    public Application(BasicDataSource dataSource) throws SQLException {
        this.qr = new QueryRunner(dataSource);
        qr.update(CREATETABLESQL);
        rsh = new BeanHandler(AccountClass.class);
    }

    public boolean isSetupDone() throws SQLException, ClassNotFoundException {
        return account_exists("root");
    }

    private boolean account_exists(String name) throws SQLException {
        Account acc = (AccountClass) qr.query(SELECTBYNAMESQL, rsh, name);
        return acc != null;
    }

    public void create_account(String name, String pwd1, String pwd2) throws SQLException, PasswordMismatchException,
            ExistingAccountException, EmptyFieldException, ClassNotFoundException {
        if (name.isEmpty() || pwd1.isEmpty() || pwd2.isEmpty())
            throw new EmptyFieldException();

        if (!pwd1.equals(pwd2))
            throw new PasswordMismatchException();

        PasswordHashGenerator phg = new PasswordHashGenerator(pwd1);
        pwd1 = phg.getHash();
        String salt = phg.getSalt();

        qr.insert(INSERTUSERSQL, rsh, name, pwd1, 0, 0, salt);
    }

    public void delete_account(String name) throws SQLException, UndefinedAccountException, LockedAccountException, AccountConnectionException, ClassNotFoundException {
        Account acc = get_account(name);
        if (acc == null)
            throw new UndefinedAccountException();
        if (acc.getLoggedIn() == 1)
            throw new AccountConnectionException();
        if (acc.getLocked() == 0)
            throw new LockedAccountException("The account is not locked");

        qr.update(DELETEBYNAMESQL, name);
    }

    public Account get_account(String name) throws SQLException, UndefinedAccountException, ClassNotFoundException {
        return (AccountClass) qr.query(SELECTBYNAMESQL, rsh, name);
    }

    public void change_pwd(String name, String pwd1, String pwd2) throws SQLException, PasswordMismatchException, EmptyFieldException, ClassNotFoundException {
        if (name.isEmpty() || pwd1.isEmpty() || pwd2.isEmpty())
            throw new EmptyFieldException();

        if (!pwd1.equals(pwd2))
            throw new PasswordMismatchException();

        PasswordHashGenerator phg = new PasswordHashGenerator(pwd1);
        pwd1 = phg.getHash();
        String salt = phg.getSalt();

        qr.update(UPDATEPWDSQL, pwd1, name);
        qr.update(UPDATESALTSQL, salt, name);
    }

    public Account login(String name, String pwd) throws SQLException, UndefinedAccountException, LockedAccountException, EmptyFieldException, AuthenticationErrorException, ClassNotFoundException {
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

    public void logout(Account acc) throws SQLException, ClassNotFoundException {
        if(acc.getLoggedIn() == 1)
            qr.update(LOGOUTBYNAMESQL, acc.getUsername());
    }

    public Account login(HttpServletRequest req, HttpServletResponse resp) throws SQLException, UndefinedAccountException, LockedAccountException, AuthenticationErrorException, ClassNotFoundException {
        Account a;

        HttpSession session = req.getSession(false);
        String username = session.getAttribute("USER").toString();
        String pwhash = session.getAttribute("PWD").toString();
        a = get_account(username);

        if (a == null)
            throw new UndefinedAccountException();

        if (!a.getPassword().equals(pwhash))
            throw new AuthenticationErrorException();

        if (a.getLocked() == 1)
            throw new LockedAccountException("The account is locked");

        return a;
    }
}
