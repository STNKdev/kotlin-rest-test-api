package ru.stnk.RestTestAPI.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserCreate {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String phone;

    private boolean emailConfirmed;

    private String os;

    private long freeBalance;

    private long betBalance;

    private long withdrawalBalance;

    private boolean enableUser;

    public UserCreate() {
    }

    public UserCreate(String email, String phone, String password, String os) {
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.os = os;
        this.emailConfirmed = false;
        this.freeBalance = 0;
        this.betBalance = 0;
        this.withdrawalBalance = 0;
        this.enableUser = true;
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

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public long getFreeBalance() {
        return freeBalance;
    }

    public void setFreeBalance(long freeBalance) {
        this.freeBalance = freeBalance;
    }

    public long getBetBalance() {
        return betBalance;
    }

    public void setBetBalance(long betBalance) {
        this.betBalance = betBalance;
    }

    public long getWithdrawalBalance() {
        return withdrawalBalance;
    }

    public void setWithdrawalBalance(long withdrawalBalance) {
        this.withdrawalBalance = withdrawalBalance;
    }

    public boolean isEnableUser() {
        return enableUser;
    }

    public void setEnableUser(boolean enableUser) {
        this.enableUser = enableUser;
    }

}
