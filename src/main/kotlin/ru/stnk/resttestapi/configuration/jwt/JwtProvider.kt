package ru.stnk.resttestapi.configuration.jwt

import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import ru.stnk.resttestapi.service.UserDetailsImpl
import java.util.*

@Component
class JwtProvider {

    @Value("\${stnk.app.jwtSecret}")
    private val jwtSecret: String? = null

    @Value("\${stnk.app.jwtExpiration}")
    private val jwtExpiration = 0

    private val logger = LoggerFactory.getLogger(JwtProvider::class.java)

    fun generateJwtToken(authentication: Authentication): String {
        val userDetails = authentication.principal as UserDetailsImpl
        return Jwts.builder()
                .setSubject(userDetails.username)
                .setIssuedAt(Date())
                .setExpiration(Date(Date().time + jwtExpiration * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact()
    }

    fun validateJwtToken(authToken: String?): Boolean {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(authToken)
            return true
        } catch (e: SignatureException) {
            logger.error("Invalid JWT signature -> Message: {} ", e)
        } catch (e: MalformedJwtException) {
            logger.error("Invalid JWT token -> Message: {}", e)
        } catch (e: ExpiredJwtException) {
            logger.error("Expired JWT token -> Message: {}", e)
        } catch (e: UnsupportedJwtException) {
            logger.error("Unsupported JWT token -> Message: {}", e)
        } catch (e: IllegalArgumentException) {
            logger.error("JWT claims string is empty -> Message: {}", e)
        }
        return false
    }

    fun getUserNameFromJwtToken(token: String?): String {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .body.subject
    }

}