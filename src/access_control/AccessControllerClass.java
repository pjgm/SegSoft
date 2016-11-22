package access_control;

import java.util.List;

public class AccessControllerClass implements AccessController {

    public Capability createCapability(String owner, String grantee, String resource, String operation) {
        return null;
    }

    public boolean checkPermission(String user, Capability cap, String resource, String operation) {
        return false;
    }

    public List<Capability> getCapabilities(String user) {
        return null;
    }
}
