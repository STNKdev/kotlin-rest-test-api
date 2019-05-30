package ru.stnk.RestTestAPI.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends AuditModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    @JsonIgnore
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "email_confirmed")
    private boolean emailConfirmed;

    @Column(name = "os")
    @JsonIgnore
    private String os;

    @Column(name = "free_balance")
    private long freeBalance;

    @Column(name = "bet_balance")
    private long betBalance;

    @Column(name = "withdrawal_balance")
    private long withdrawalBalance;

    @Column(name = "enabled")
    @JsonIgnore
    private boolean enabled;

    /*
    * По умолчанию ManyToMany(fetch = FetchType.LAZY) получается ошибка
    * org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role
    *
    * если оставить fetch = FetchType.EAGER , то ошибки нет
    *
    * почему?????!!!! Подумать над решением с fetch = FetchType.LAZY
    *
    * */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_roles",
            joinColumns = {@JoinColumn(name="user_id", referencedColumnName="id"),
                            @JoinColumn(name="user_email", referencedColumnName="email")},
            inverseJoinColumns = {@JoinColumn(name="role_id", referencedColumnName="id"),
                    @JoinColumn(name="role_name", referencedColumnName="name")}
    )
    @JsonIgnore
    private List<Roles> roles;

    private String passwordEncoder (String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public void setPassword (String password) {
        this.password = passwordEncoder(password);
    }

    public String getPassword() {
        return password;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles (List<Roles> roles) {
        this.roles = roles;
    }

    public void addRole (Roles name) {
        roles.add(name);
    }
}