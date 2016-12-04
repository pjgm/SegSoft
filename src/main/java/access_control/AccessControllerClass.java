package main.java.access_control;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class AccessControllerClass implements AccessController {

    private static final String INSERTCAPSQL = "insert into capability (grantee, owner, resource, operation) " +
            "values (?, ?, ?, ?)";
    private static final String SELECTCAPSQL = "select * from capability where owner = ? and grantee = ? and resource " +
            "= ? and operation = ?";
    private static final String GETCAPSSQL = "select * from capability where grantee = ?";

    private QueryRunner qr;
    private ResultSetHandler rsh;

    public AccessControllerClass(BasicDataSource dataSource) {
        this.qr = new QueryRunner(dataSource);
        rsh = new BeanHandler<>(CapabilityClass.class);
    }

    public void createCapability(String owner, String grantee, String resource, String operation) throws SQLException {
        qr.insert(INSERTCAPSQL, rsh, grantee, owner, resource, operation);
    }

    public Capability getCapability(String owner, String grantee, String resource, String operation) throws SQLException {
        return (CapabilityClass) qr.query(SELECTCAPSQL, rsh, owner, grantee, resource, operation);
    }

    public boolean checkPermission(Capability capability) throws SQLException {
        Capability cap = getCapability(capability.getOwner(), capability.getGrantee(), capability.getResource(),
                capability.getOperation());
        return cap != null;
    }

    public List<CapabilityClass> getCapabilities(String username) throws SQLException {
        return qr.query(GETCAPSSQL, new BeanListHandler<>(CapabilityClass.class), username);
    }
}