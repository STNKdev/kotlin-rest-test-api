package ru.stnk.resttestapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.stnk.resttestapi.configuration.jwt.JwtProvider

@Service
class LoginControllerService (
        @Autowired val authenticationManager: AuthenticationManager,
        @Autowired val jwtProvider: JwtProvider
) {
    fun authenticateUser(email: String, password: String): String {
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        )
        SecurityContextHolder.getContext().authentication = authentication
        return jwtProvider.generateJwtToken(authentication)
    }
}