package ru.stnk.RestTestAPI.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import ru.stnk.RestTestAPI.repository.RolesRepository;

public class RolesDTO implements GrantedAuthority {

    /*@Autowired
    RolesRepository rolesRepository;*/

    @Override
    public String getAuthority() {
        return null;
    }
}