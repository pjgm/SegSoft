package access_control;

import java.util.List;

public interface AccessController {

    Capability createCapability(String owner, String grantee, String resource, String operation);

    boolean checkPermission(String user, Capability cap, String resource, String operation);

    List<Capability> getCapabilities(String user);
}
