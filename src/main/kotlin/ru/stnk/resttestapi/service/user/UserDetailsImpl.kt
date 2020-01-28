package ru.stnk.resttestapi.service.user

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.stnk.resttestapi.entity.Role
import ru.stnk.resttestapi.entity.User
import java.util.stream.Collectors

class UserDetailsImpl(
        val id: Long?,
        val email: String,
        val phone: String,
        val os: String,
        val freeBalance: Long,
        val betBalance: Long,
        val withdrawalBalance: Long,
        @JsonIgnore val emailConfirmed: Boolean,
        @JsonIgnore private val password: String,
        private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getUsername(): String? {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return emailConfirmed
    }

    override fun getPassword(): String? {
        return password
    }

    // Как статические объекты в java
    companion object {

        private const val serialVersionUID = 1L

        fun build(user: User): UserDetailsImpl {

            val authorities: List<GrantedAuthority> = user.roles.stream().map {
                role: Role -> SimpleGrantedAuthority(role.name?.name)
            }.collect(Collectors.toList())

            return UserDetailsImpl(
                    id = user.id,
                    email = user.email,
                    phone = user.phone,
                    os = user.os,
                    emailConfirmed = user.emailConfirmed,
                    freeBalance = user.freeBalance,
                    betBalance = user.betBalance,
                    withdrawalBalance = user.withdrawalBalance,
                    password = user.password,
                    authorities = authorities
            )
        }
    }

}
