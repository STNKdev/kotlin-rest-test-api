package ru.stnk.RestTestAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.stnk.RestTestAPI.model.Roles;
import ru.stnk.RestTestAPI.repository.RolesRepository;

@RestController
public class RolesController {

    @Autowired
    private RolesRepository rolesRepository;

    @GetMapping("/add-role")
    public Roles createRole (@RequestParam Roles param) {
        return ?;
    }
}
