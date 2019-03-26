package ru.stnk.RestTestAPI.model;

public class RoleCreate {

    private String roleName;

    public RoleCreate() {
    }

    public RoleCreate(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
