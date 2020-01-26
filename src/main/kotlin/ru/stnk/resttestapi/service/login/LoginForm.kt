package ru.stnk.resttestapi.service.login

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class LoginForm {

    @NotBlank
    @Email(message = "Некорректный email")
    var email: String? = null

    @NotBlank
    @Size(min = 3, message = "Длина пароля должна быть не меньше трёх символов")
    var password: String? = null
}