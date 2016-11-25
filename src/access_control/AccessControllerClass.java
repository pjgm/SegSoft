package access_control;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;
import java.util.List;

public class AccessControllerClass implements AccessController {

    private static final String INSERTCAPABILITYSQL = "insert into capability (grantee, owner, resource, operation) " +
            "values (?, ?, ?, ?)";

    private QueryRunner qr;
    private ResultSetHandler rsh;

    public AccessControllerClass(BasicDataSource dataSource) {
        this.qr = new QueryRunner(dataSource);
        rsh = new BeanHandler<>(CapabilityClass.class);
        try {
            createCapability("root", "paulo", "webapp", "test_operation");
            Capability cap = (CapabilityClass) qr.query("select * from capability where grantee = paulo", rsh);
            System.out.println("Owner: " + cap.getOwner());
            System.out.println("Grantee: " + cap.getGrantee());
            System.out.println("resource: " + cap.getResource());
            System.out.println("operation: " + cap.getOperation());
            System.out.println("creation time: " + cap.getCreationTime());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Capability createCapability(String owner, String grantee, String resource, String operation) throws SQLException {
        Capability cap = (CapabilityClass) qr.insert(INSERTCAPABILITYSQL, rsh, grantee, owner, resource, operation);
        return cap;
    }

    public boolean checkPermission(String user, Capability cap, String resource, String operation) {
        return false;
    }

    public List<Capability> getCapabilities(String user) {
        return null;
    }
}
