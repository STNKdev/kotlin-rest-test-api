package ru.stnk.RestTestAPI.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

// Класс для проверки данных пользователя при регистрации
public class UserDTO {

    @Email(message = "Некорректный email")
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 3, message = "Длина пароля должна быть не меньше трёх символов")
    private String password;

    @NotBlank
    @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$",
            message = "Некорректный формат номера телефона")
    private String phone;

    @NotBlank
    private String os;

    private boolean viaEmail;

    private String code;

    public UserDTO() {
    }

    public UserDTO(String email, String phone, String password) {
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.os = "";
        this.viaEmail = true;
        this.code = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOs() {
        return os;
    }

    public void setOs (String os) {
        this.os = os;
    }

    public boolean isViaEmail() {
        return viaEmail;
    }

    public void setViaEmail(boolean viaEmail) {
        this.viaEmail = viaEmail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
