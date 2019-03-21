package ru.stnk.RestTestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.stnk.RestTestAPI.model.Roles;
import ru.stnk.RestTestAPI.repository.RolesRepository;

@RestController
public class RolesController {

    @Autowired
    private RolesRepository rolesRepository;

    @GetMapping("/add-role")
    @ResponseStatus(HttpStatus.CREATED)
    public Roles createRole (@RequestParam String name) {
        Roles role = new Roles();
        role.setRole(name);
        return rolesRepository.save(role);
    }
}
