package ru.stnk.resttestapi.configuration.jwt

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import ru.stnk.resttestapi.service.user.UserDetailsServiceImpl
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthTokenFilter: OncePerRequestFilter() {

    @Autowired
    private val tokenProvider: JwtProvider? = null

    @Autowired
    private val userDetailsService: UserDetailsServiceImpl? = null

    private val loggerWriter = LoggerFactory.getLogger(JwtAuthTokenFilter::class.java)

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  filterChain: FilterChain) {
        try {

            val jwt = getJwt(request)

            if (jwt != null && tokenProvider!!.validateJwtToken(jwt)) {

                val username: String = tokenProvider.getUserNameFromJwtToken(jwt)

                val userDetails: UserDetails = userDetailsService!!.loadUserByUsername(username)

                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = authentication
            }

        } catch (e: Exception) {
            loggerWriter.error("Не удается установить аутентификацию пользователя -> Message: {}", e)
        }

        filterChain.doFilter(request, response)
    }

    private fun getJwt(request: HttpServletRequest): String? {
        //String authHeader = request.getHeader("Authorization");
        /*if (authHeader != null && authHeader.startsWith("Bearer ")) {
        	return authHeader.replace("Bearer ","");
        }*/return request.getHeader("-X-api-key")
    }
}