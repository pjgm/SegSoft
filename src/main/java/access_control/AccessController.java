package main.java.access_control;

import java.sql.SQLException;
import java.util.List;

public interface AccessController {

    void createCapability(String owner, String grantee, String resource, String operation) throws SQLException;

    Capability getCapability(String owner, String grantee, String resource, String operation) throws SQLException;

    boolean checkPermission(Capability capability) throws SQLException;

    List<CapabilityClass> getCapabilities(String user) throws SQLException;
}
