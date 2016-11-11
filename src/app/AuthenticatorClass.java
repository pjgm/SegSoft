package app;

import exceptions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticatorClass implements Authenticator {

    private Connection c;
    private static final String CREATETABLESQL = "create table if not exists account (name string, pwd string, logged integer, locked integer, salt string)";
    private static final String SELECTBYNAMESQL = "select * from account where name LIKE ?";
    private static final String INSERTUSERSQL = "insert into account (name, pwd, logged, locked, salt) values (?, ?, ?, ?, ?)";
    private static final String SELECTLOGGEDSQL = "select * from account where name LIKE ? and logged == 1";
    private static final String SELECTLOCKEDSQL = "select * from account where name LIKE ? and locked == 1";
    private static final String DELETEBYNAMESQL = "delete from account where name LIKE ?";
    private static final String UPDATEPWDSQL = "update account set pwd = ? where name LIKE ?";
    private static final String UPDATESALTSQL = "update account set salt = ? where name LIKE ?";
    private static final String SELECTBYPWDSQL = "select * from account where pwd LIKE ?";
    private static final String LOGINBYNAMESQL = "update account set logged = 1 where name LIKE ?";
    private static final String LOGOUTBYNAMESQL = "update account set logged = 0 where name LIKE ?";

    public AuthenticatorClass() {
        try {
            setupDatabase();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void connectToDatabase() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        this.c = DriverManager.getConnection("jdbc:sqlite:Auth.db");
    }

    public void closeDatabaseConnection() throws SQLException {
        if (c != null)
            c.close();
    }

    public void setupDatabase() throws ClassNotFoundException, SQLException {
        connectToDatabase();
        PreparedStatement ct = c.prepareStatement(CREATETABLESQL);
        ct.executeUpdate();
        closeDatabaseConnection();
    }

    public boolean isSetupDone() throws SQLException, ClassNotFoundException {
        return accountExists("root");
    }

    public boolean accountExists(String name) throws SQLException, ClassNotFoundException {
        connectToDatabase();
        PreparedStatement sbn;
        sbn = c.prepareStatement(SELECTBYNAMESQL);
        sbn.setString(1, name);
        boolean exists = sbn.executeQuery().next();
        closeDatabaseConnection();
        return exists;
    }

    public void create_account(String name, String pwd1, String pwd2)
            throws SQLException, PasswordMismatchException, ExistingAccountException, EmptyFieldException, ClassNotFoundException {

        if (name.isEmpty() || pwd1.isEmpty() || pwd2.isEmpty())
            throw new EmptyFieldException();

        if (!pwd1.equals(pwd2))
            throw new PasswordMismatchException();

        PasswordHashGenerator phg = new PasswordHashGenerator(pwd1);
        pwd1 = phg.getHash();
        String salt = phg.getSalt();

        connectToDatabase();

        PreparedStatement sbn = c.prepareStatement(SELECTBYNAMESQL);
        sbn.setString(1, name);
        ResultSet rs = sbn.executeQuery();
        if (!rs.next()) {
            PreparedStatement iu = c.prepareStatement(INSERTUSERSQL);
            iu.setString(1, name);
            iu.setString(2, pwd1);
            iu.setInt(3, 0);
            iu.setInt(4, 0);
            iu.setString(5, salt);
            iu.executeUpdate();
        } else {
            closeDatabaseConnection();
            throw new ExistingAccountException();
        }

        closeDatabaseConnection();
    }

    public void delete_account(String name)
            throws SQLException, UndefinedAccountException, LockedAccountException, AccountConnectionException, ClassNotFoundException {

        connectToDatabase();

        PreparedStatement sbn = c.prepareStatement(SELECTBYNAMESQL);
        sbn.setString(1, name);
        ResultSet rs = sbn.executeQuery();
        if (!rs.next()) {
            closeDatabaseConnection();
            throw new UndefinedAccountException();
        }

        PreparedStatement slg = c.prepareStatement(SELECTLOGGEDSQL);
        slg.setString(1, name);
        rs = slg.executeQuery();
        if (rs.next()) {
            closeDatabaseConnection();
            throw new AccountConnectionException();
        }

        PreparedStatement slc = c.prepareStatement(SELECTLOCKEDSQL);
        slc.setString(1, name);
        rs = slc.executeQuery();
        if (!rs.next()) {
            closeDatabaseConnection();
            throw new LockedAccountException();
        }

        PreparedStatement dbn = c.prepareStatement(DELETEBYNAMESQL);
        dbn.setString(1, name);
        dbn.executeUpdate();

        closeDatabaseConnection();
    }

    public Account get_account(String name) throws SQLException, UndefinedAccountException, ClassNotFoundException {

        Account a;

        connectToDatabase();

        PreparedStatement sbn = c.prepareStatement(SELECTBYNAMESQL);
        sbn.setString(1, name);
        ResultSet rs = sbn.executeQuery();
        if (rs.next()) {
            String n = rs.getString("name");
            String p = rs.getString("pwd");
            int log = rs.getInt("logged");
            int lock = rs.getInt("locked");

            a = new AccountClass(n, p);
            if (log == 1)
                a.log_in();
            if (lock == 1)
                a.lock();
        } else {
            closeDatabaseConnection();
            throw new UndefinedAccountException();
        }

        closeDatabaseConnection();

        return a;
    }

    public void change_pwd(String name, String pwd1, String pwd2)
            throws SQLException, PasswordMismatchException, EmptyFieldException, ClassNotFoundException {

        if (name.isEmpty() || pwd1.isEmpty() || pwd2.isEmpty())
            throw new EmptyFieldException();

        if (!pwd1.equals(pwd2))
            throw new PasswordMismatchException();

        PasswordHashGenerator phg = new PasswordHashGenerator(pwd1);
        pwd1 = phg.getHash();
        String salt = phg.getSalt();

        connectToDatabase();

        PreparedStatement sbn = c.prepareStatement(SELECTBYNAMESQL);
        sbn.setString(1, name);
        ResultSet rs = sbn.executeQuery();
        if (rs.next()) {
            PreparedStatement up = c.prepareStatement(UPDATEPWDSQL);
            up.setString(1, pwd1);
            up.setString(2, name);
            up.executeUpdate();

            PreparedStatement us = c.prepareStatement(UPDATESALTSQL);
            us.setString(1, salt);
            us.setString(2, name);
            us.executeUpdate();
        }

        closeDatabaseConnection();
    }

    public Account login(String name, String pwd) throws SQLException, UndefinedAccountException,
            LockedAccountException, EmptyFieldException, AuthenticationErrorException, ClassNotFoundException {

        if (name.isEmpty() || pwd.isEmpty())
            throw new EmptyFieldException();

        Account a = null;

        connectToDatabase();

        PreparedStatement sbn = c.prepareStatement(SELECTBYNAMESQL);
        sbn.setString(1, name);
        ResultSet rs = sbn.executeQuery();
        if (!rs.next()) {
            closeDatabaseConnection();
            throw new UndefinedAccountException();
        }

        PasswordHashGenerator phg = new PasswordHashGenerator(pwd);
        String salt = rs.getString("salt");
        pwd = phg.createNewHash(salt, pwd);

        PreparedStatement slc = c.prepareStatement(SELECTLOCKEDSQL);
        slc.setString(1, name);
        rs = slc.executeQuery();
        if (rs.next()) {
            closeDatabaseConnection();
            throw new LockedAccountException();
        }

        PreparedStatement sbp = c.prepareStatement(SELECTBYPWDSQL);
        sbp.setString(1, pwd);
        rs = sbp.executeQuery();
        if (!rs.next()) {
            closeDatabaseConnection();
            throw new AuthenticationErrorException();
        }

        rs = sbn.executeQuery();
        if (rs.next()) {
            String n = rs.getString("name");
            String p = rs.getString("pwd");

            PreparedStatement lbn = c.prepareStatement(LOGINBYNAMESQL);
            lbn.setString(1, name);
            lbn.executeUpdate();

            a = new AccountClass(n, p);
            a.log_in();
        }

        closeDatabaseConnection();

        return a;
    }

    public void logout(Account acc) throws SQLException, ClassNotFoundException {

        String name = acc.get_account_name();

        connectToDatabase();

        PreparedStatement slg = c.prepareStatement(SELECTLOGGEDSQL);
        slg.setString(1, name);
        ResultSet rs = slg.executeQuery();
        if (rs.next()) {
            PreparedStatement lbn = c.prepareStatement(LOGOUTBYNAMESQL);
            lbn.setString(1, name);
            lbn.executeUpdate();
        }

        closeDatabaseConnection();
    }

    public Account login(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, UndefinedAccountException, LockedAccountException, AuthenticationErrorException, ClassNotFoundException {
        Account a;

        HttpSession session = req.getSession(false);
        String username = session.getAttribute("USER").toString();
        String pwhash = session.getAttribute("PWD").toString();
        a = get_account(username);

        if (a == null)
            throw new UndefinedAccountException();

        if (!a.get_account_pwd().equals(pwhash))
            throw new AuthenticationErrorException();

        if (a.is_locked())
            throw new LockedAccountException();

        return a;
    }
}