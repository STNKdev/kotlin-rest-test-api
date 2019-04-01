package ru.stnk.RestTestAPI.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.stnk.RestTestAPI.dto.RolesDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends AuditModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id", nullable = false)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email_confirmed")
    private boolean emailConfirmed;

    @Column(name = "os")
    private String os;

    @Column(name = "free_balance")
    private long freeBalance;

    @Column(name = "bet_balance")
    private long betBalance;

    @Column(name = "withdrawal_balance")
    private long withdrawalBalance;

    @Column(name = "enable_user")
    private boolean enableUser;

    @ManyToMany
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
        this.password = passwordEncoder(password);
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

    public void addRole (Roles name) {
        roles.add(name);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getEnableUser();
    }
}