package ru.stnk.RestTestAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "email", nullable = false)
    @NotBlank
    private String email;

    @Column(name = "phone", nullable = false)
    @NotBlank
    private String phone;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 3)
    @JsonIgnore
    private String password;

    @Column(name = "email_confirmed")
    private boolean emailConfirmed = false;

    @Column(name = "os")
    private String os;

    @Column(name = "free_balance")
    private long freeBalance;

    @Column(name = "bet_balance")
    private long betBalance;

    @Column(name = "withdrawal_balance")
    private long withdrawalBalance;

    @ManyToMany
    @JoinTable(name="user_roles",
            joinColumns = @JoinColumn(name="user_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="roles_id", referencedColumnName="id")
    )
    @JsonIgnore
    private List<Roles> roles;

    @Column(name = "enable_user")
    private boolean enableUser;

    public String getEmail() {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone (String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword (String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public boolean getEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed (boolean confirmed) {
        this.emailConfirmed = confirmed;
    }

    public String getOs() {
        return os;
    }

    public void setOs (String os) {
        this.os = os;
    }

    public Long getFreeBalance() {
        return freeBalance;
    }

    public void setFreeBalance (Long freeBalance) {
        this.freeBalance = freeBalance;
    }

    public Long getBetBalance() {
        return betBalance;
    }

    public void setBetBalance (Long betBalance) {
        this.betBalance = betBalance;
    }

    public Long getWithdrawalBalance() {
        return withdrawalBalance;
    }

    public void setWithdrawalBalance (Long withdrawalBalance) {
        this.withdrawalBalance = withdrawalBalance;
    }

    public boolean getEnableUser() {
        return enableUser;
    }

    public void setEnableUser (boolean enableUser) {
        this.enableUser = enableUser;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles (List<Roles> roles) {
        this.roles = roles;
    }
}