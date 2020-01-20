package ru.stnk.resttestapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.stnk.resttestapi.repository.UserRepository

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    private val userRepository: UserRepository? = null

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {

        val userFromDB = userRepository!!.findByEmail(email)

        return if (userFromDB.isPresent) {

            UserDetailsImpl(userFromDB.get())

        } else {
            throw UsernameNotFoundException(email)
        }
    }
}
