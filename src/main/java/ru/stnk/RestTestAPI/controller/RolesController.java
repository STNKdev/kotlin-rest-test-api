package ru.stnk.RestTestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.stnk.RestTestAPI.entity.Roles;
import ru.stnk.RestTestAPI.repository.RolesRepository;

import java.util.List;

@RestController
public class RolesController {

    @Autowired
    private RolesRepository rolesRepository;

    @GetMapping("/add-role")
    @ResponseStatus(HttpStatus.CREATED)
    public String createRole (@RequestParam(required = false) String name) {
        if (name == null) {

            List<Roles> roles = rolesRepository.findAll();

            if (roles.isEmpty()) {
                Roles roleAdmin = new Roles();
                roleAdmin.setName("ROLE_ADMIN");
                rolesRepository.save(roleAdmin);

                Roles roleUser = new Roles();
                roleUser.setName("ROLE_USER");
                rolesRepository.save(roleUser);
            }
        } else {
            rolesRepository.save(new Roles(name));
        }
        return "CREATED";
    }
}
