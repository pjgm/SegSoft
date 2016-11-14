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
import java.sql.SQLException;

public class Application implements Authenticator {

    private static final String CREATETABLESQL = "create table if not exists account (username string primary key, password string, loggedIn integer, locked integer, salt string)";
    private static final String SELECTBYNAMESQL = "select * from account where username LIKE ?";
    private static final String INSERTUSERSQL = "insert into account (username, password, loggedIn, locked, salt) values (?, ?, ?, ?, ?)";

    private QueryRunner qr;
    private ResultSetHandler rsh;

    public Application(BasicDataSource dataSource) throws SQLException {
        this.qr = new QueryRunner(dataSource);
        qr.update(CREATETABLESQL);
        rsh = new BeanHandler(AccountClass.class);
        try {
            create_account("root", "teste123", "teste123");
        } catch (PasswordMismatchException e) {
            e.printStackTrace();
        } catch (ExistingAccountException e) {
            e.printStackTrace();
        } catch (EmptyFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isSetupDone() throws SQLException, ClassNotFoundException {
        return account_exists("root");
    }

    private boolean account_exists(String name) throws SQLException {
        AccountClass acc = (AccountClass) qr.query(SELECTBYNAMESQL, rsh, name);
        return acc != null;
    }

    public void create_account(String name, String pwd1, String pwd2) throws SQLException, PasswordMismatchException, ExistingAccountException, EmptyFieldException, ClassNotFoundException {
        if (name.isEmpty() || pwd1.isEmpty() || pwd2.isEmpty())
            throw new EmptyFieldException();

        if (!pwd1.equals(pwd2))
            throw new PasswordMismatchException();

        PasswordHashGenerator phg = new PasswordHashGenerator(pwd1);
        pwd1 = phg.getHash();
        String salt = phg.getSalt();

        Account acc = (Account) qr.insert(INSERTUSERSQL, rsh, name, pwd1, 0, 0, salt);

        boolean success = acc != null;
        if(success)
            System.out.println("Successfully inserted user");
        else
            System.out.println("Error inserting user");
    }

    public void delete_account(String name) throws SQLException, UndefinedAccountException, LockedAccountException, AccountConnectionException, ClassNotFoundException {

    }

    public Account get_account(String name) throws SQLException, UndefinedAccountException, ClassNotFoundException {
        return null;
    }

    public void change_pwd(String name, String pwd1, String pwd2) throws SQLException, PasswordMismatchException, EmptyFieldException, ClassNotFoundException {

    }

    public Account login(String name, String pwd) throws SQLException, UndefinedAccountException, LockedAccountException, EmptyFieldException, AuthenticationErrorException, ClassNotFoundException {
        return null;
    }

    public void logout(Account acc) throws SQLException, ClassNotFoundException {

    }

    public Account login(HttpServletRequest req, HttpServletResponse resp) throws SQLException, UndefinedAccountException, LockedAccountException, AuthenticationErrorException, ClassNotFoundException {
        return null;
    }
}
