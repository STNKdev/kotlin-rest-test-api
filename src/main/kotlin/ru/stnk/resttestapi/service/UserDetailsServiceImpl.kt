package ru.stnk.resttestapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.stnk.resttestapi.repository.UserRepository

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    val userRepository: UserRepository? = null

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {

        val userFromDB = userRepository!!.findByEmail(email)

        if (userFromDB.isPresent) {

            return UserDetailsImpl.build(userFromDB.get())

        } else {
            throw UsernameNotFoundException(email)
        }
    }
}
