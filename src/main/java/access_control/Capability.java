package main.java.access_control;

public interface Capability {

    String getOwner();

    void setOwner(String owner);

    String getGrantee();

    void setGrantee(String grantee);

    String getResource();

    void setResource(String resource);

    String getOperation();

    void setOperation(String operation);

    String getCreationTime();

    void setCreationTime(String creationTime);
}