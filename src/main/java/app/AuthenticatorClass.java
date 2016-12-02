package main.java.app;

import main.java.crypto.PasswordHashGenerator;
import main.java.exceptions.*;
import main.java.model.Account;
import main.java.model.AccountClass;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import java.sql.SQLException;
import java.util.List;

public class AuthenticatorClass implements Authenticator {

    private static final String CREATEACCTABLESQL = "create table if not exists account (username string primary key, " +
            "password string, email string, emailpl string, phone string, phonepl string, publicinfo string, " +
            "pipl string, internalinfo string, iipl string, secretinfo string, sipl string, " +
            "role string, loggedIn integer, locked integer, salt string)";
    private static final String CREATEFRIENDTABLESQL = "create table if not exists friend (username string, " +
            "friendname string, status integer, primary key(username, friendname), foreign key(username) references " +
            "account (username) ON DELETE CASCADE, foreign key (friendname) references account(username) ON DELETE " +
            "CASCADE, check (username != friendname))";
    private static final String SELECTBYNAMESQL = "select * from account where username LIKE ?";
    private static final String INSERTUSERSQL = "insert into account (username, password, email, emailpl, phone, " +
            "phonepl, publicinfo, pipl, internalinfo, iipl, secretinfo, sipl, role, loggedIn, locked, salt) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETEBYNAMESQL = "delete from account where username LIKE ?";
    private static final String UPDATEPASSWORDSQL = "update account set password = ?, salt = ? where username LIKE ?";
    private static final String SETLOCKSTATUSSQL = "update account set locked = ? where username LIKE ?";
    private static final String ADDFRIENDSQL = "insert into friend (username, friendname, status) values (?, ?, ?)";
    private static final String REMOVEFRIENDSQL = "delete from friend where username = ? and friendname = ?";
    private static final String GETFRIENDSSQL = "select * from friend where username = ? and status = 1";
    private static final String GETPENDINGFRIENDSSQL = "select * from friend where friendname = ? and status = 0";
    private static final String ACCEPTFRIENDSQL = "update friend set status = 1 where username = ? and friendname = ?";
    private static final String UPDATEEMAILSQL = "update account set email = ? where username = ?";
    private static final String UPDATEEMAILPLSQL = "update account set emailpl = ? where username = ?";
    private static final String UPDATEPHONESQL = "update account set phone = ? where username = ?";
    private static final String UPDATEPHONEPLSQL = "update account set phonepl = ? where username = ?";
    private static final String UPDATEPUBLICSQL = "update account set publicinfo = ? where username = ?";
    private static final String UPDATEPUBLICPLSQL = "update account set pipl = ? where username = ?";
    private static final String UPDATEINTERNALSQL = "update account set internalinfo = ? where username = ?";
    private static final String UPDATEINTERNALPLSQL = "update account set iipl = ? where username = ?";
    private static final String UPDATESECRETSQL = "update account set secretinfo = ? where username = ?";
    private static final String UPDATESECRETPLSQL = "update account set sipl = ? where username = ?";
    private static final String LOGINBYNAMESQL = "update account set loggedIn = 1 where username LIKE ?";
    private static final String LOGOUTBYNAMESQL = "update account set loggedIn = 0 where username LIKE ?";

    // Capabilities
    private static final String CREATECAPTABLESQL = "create table if not exists capability (grantee string, owner " +
            "string, resource string, operation string, creationTime timestamp default current_timestamp not null, " +
            "foreign key (grantee) references account(username) ON DELETE CASCADE, foreign key (owner) references " +
            "account (username) ON DELETE CASCADE " +
            "primary key (grantee, owner, resource, operation), check (grantee != owner))";

    private QueryRunner qr;
    private ResultSetHandler rsh;

    public AuthenticatorClass(BasicDataSource dataSource) throws SQLException {
        this.qr = new QueryRunner(dataSource);
        qr.update(CREATEACCTABLESQL);
        qr.update(CREATEFRIENDTABLESQL);
        qr.update(CREATECAPTABLESQL);
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

        Account acc = (AccountClass) qr.insert(INSERTUSERSQL, rsh, name, pwd1, email, "public", phone, "public",
                "Hello world!", "public", "Hello friends!", "internal", "My secret is...", "private", role, 0, 0, salt);

        if (acc == null)
            throw new ExistingAccountException();
    }

    public void delete_account(String name) throws SQLException, UndefinedAccountException, LockedAccountException,
            AccountConnectionException {
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

    public void change_pwd(String name, String pwd1, String pwd2) throws SQLException, PasswordMismatchException,
            EmptyFieldException {
        if (name.isEmpty() || pwd1.isEmpty() || pwd2.isEmpty())
            throw new EmptyFieldException();

        if (!pwd1.equals(pwd2))
            throw new PasswordMismatchException();

        PasswordHashGenerator phg = new PasswordHashGenerator(pwd1);
        pwd1 = phg.getHash();
        String salt = phg.getSalt();

        qr.update(UPDATEPASSWORDSQL, pwd1, salt, name);
    }

    public void lock_account(String name) throws EmptyFieldException, SQLException, UndefinedAccountException {
        if (name.isEmpty())
            throw new EmptyFieldException();

        int updates = qr.update(SETLOCKSTATUSSQL, 1, name);

        if (updates == 0)
            throw new UndefinedAccountException();
    }

    public void unlock_account(String name) throws EmptyFieldException, SQLException, UndefinedAccountException {
        if (name.isEmpty())
            throw new EmptyFieldException();

        int updates = qr.update(SETLOCKSTATUSSQL, 0, name);

        if (updates == 0)
            throw new UndefinedAccountException();
    }

    public void add_friend(String username, String friendName, int status) throws SQLException {
        qr.insert(ADDFRIENDSQL, new ColumnListHandler<String>(), username, friendName, status);
    }

    public void remove_friend(String username, String friendName) throws SQLException {
        qr.update(REMOVEFRIENDSQL, username, friendName);
    }

    public List<String> get_friends(String name) throws SQLException {
        return qr.query(GETFRIENDSSQL, new ColumnListHandler<String>("friendname"), name);
    }

    public List<String> get_pending_friends(String name) throws SQLException {
        return qr.query(GETPENDINGFRIENDSSQL, new ColumnListHandler<String>("username"), name);
    }

    public void accept_friend_request(String name, String friendName) throws SQLException {
        qr.update(ACCEPTFRIENDSQL, name, friendName);
    }

    public void change_email(String username, String email) throws SQLException, EmptyFieldException {
        if (email.isEmpty())
            throw new EmptyFieldException();
        qr.update(UPDATEEMAILSQL, email, username);
    }

    public void change_email_privacy_level(String username, String lvl) throws SQLException, EmptyFieldException {
        if (lvl.isEmpty())
            throw new EmptyFieldException();
        qr.update(UPDATEEMAILPLSQL, lvl, username);
    }

    public void change_phone(String username, String phone) throws SQLException, EmptyFieldException {
        if (phone.isEmpty())
            throw new EmptyFieldException();
        qr.update(UPDATEPHONESQL, phone, username);
    }

    public void change_phone_privacy_level(String username, String lvl) throws SQLException, EmptyFieldException {
        if (lvl.isEmpty())
            throw new EmptyFieldException();
        qr.update(UPDATEPHONEPLSQL, lvl, username);
    }

    public void change_publicInfo(String username, String publicInfo) throws SQLException, EmptyFieldException {
        if (publicInfo.isEmpty())
            throw new EmptyFieldException();
        qr.update(UPDATEPUBLICSQL, publicInfo, username);
    }

    public void change_publicInfo_privacy_level(String username, String lvl) throws SQLException, EmptyFieldException {
        if (lvl.isEmpty())
            throw new EmptyFieldException();
        qr.update(UPDATEPUBLICPLSQL, lvl, username);
    }

    public void change_internalInfo(String username, String internalInfo) throws SQLException, EmptyFieldException {
        if (internalInfo.isEmpty())
            throw new EmptyFieldException();
        qr.update(UPDATEINTERNALSQL, internalInfo, username);
    }

    public void change_internalInfo_privacy_level(String username, String lvl) throws SQLException, EmptyFieldException {
        if (lvl.isEmpty())
            throw new EmptyFieldException();
        qr.update(UPDATEINTERNALPLSQL, lvl, username);
    }

    public void change_secretInfo(String username, String secretInfo) throws SQLException, EmptyFieldException {
        if (secretInfo.isEmpty())
            throw new EmptyFieldException();
        qr.update(UPDATESECRETSQL, secretInfo, username);
    }

    public void change_secretInfo_privacy_level(String username, String lvl) throws SQLException, EmptyFieldException {
        if (lvl.isEmpty())
            throw new EmptyFieldException();
        qr.update(UPDATESECRETPLSQL, lvl, username);
    }

    public Account login(String name, String pwd) throws SQLException, UndefinedAccountException,
            LockedAccountException, EmptyFieldException, AuthenticationErrorException {
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