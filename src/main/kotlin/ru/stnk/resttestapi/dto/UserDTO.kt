package ru.stnk.resttestapi.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

// Класс для проверки данных пользователя при регистрации
class UserDTO {

    @Email(message = "Некорректный email")
    @NotBlank
    var email: String = ""

    @NotBlank
    @Size(min = 3, message = "Длина пароля должна быть не меньше трёх символов")
    var password: String = ""

    @NotBlank
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$", message = "Некорректный формат номера телефона")
    var phone: String = ""

    @NotBlank(message="Не должно быть пустым")
    var os: String = ""

    var isViaEmail: Boolean = false

    var code: String = ""

    constructor() {}

    constructor(email: String, phone: String, password: String) {
        this.email = email
        this.phone = phone
        this.password = password
        this.os = ""
        this.isViaEmail = true
        this.code = ""
    }
}
