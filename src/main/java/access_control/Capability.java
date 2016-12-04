package main.java.access_control;

public interface Capability {

    String getGrantee();

    void setGrantee(String grantee);

    String getOwner();

    void setOwner(String owner);

    String getResource();

    void setResource(String resource);

    String getOperation();

    void setOperation(String operation);

    String getCreationTime();

    void setCreationTime(String creationTime);
}