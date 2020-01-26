package ru.stnk.resttestapi.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.stnk.resttestapi.results.RestResponse
import ru.stnk.resttestapi.service.LoginControllerService
import ru.stnk.resttestapi.service.login.LoginForm
import javax.validation.Valid

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
class LoginController (
        @Autowired val loginControllerService: LoginControllerService
) {
    @PostMapping("/login")
    fun authenticateUser(@RequestBody @Valid loginRequest: LoginForm): RestResponse {
        val data: HashMap<String, String> = HashMap()
        val response = RestResponse()
        data["api-key"] = loginControllerService.authenticateUser(loginRequest.email!!, loginRequest.password!!)
        return response
    }
}