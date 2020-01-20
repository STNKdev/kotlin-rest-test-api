package ru.stnk.resttestapi.service

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.stnk.resttestapi.entity.Roles
import ru.stnk.resttestapi.entity.User
import java.util.*

class UserDetailsImpl(private val user: User) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {

        val listRoles = ArrayList<Roles>()

        val userRoles = user.roles

        listRoles.addAll(userRoles)

        return listRoles
    }

    override fun getUsername(): String? {
        return user.email
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
        return user.isEnabled
    }

    override fun getPassword(): String? {
        return user.password
    }

}
