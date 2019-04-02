package ru.stnk.RestTestAPI.dto;

public class RolesDTO {

    private String nameRole;

    public RolesDTO() {
    }

    public RolesDTO(String nameRole) {
        this.nameRole = nameRole;
    }

    public String getNameRole() {
        return nameRole;
    }

    public void setNameRole(String nameRole) {
        this.nameRole = nameRole;
    }
}