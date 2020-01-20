package ru.stnk.resttestapi.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer
import org.springframework.session.web.http.CookieHttpSessionIdResolver
import org.springframework.session.web.http.DefaultCookieSerializer
import org.springframework.session.web.http.HttpSessionIdResolver

@Configuration
//Из-за включенной аннотации не создаются таблицы Spring_Session
//@EnableJdbcHttpSession
class SessionConfig : AbstractHttpSessionApplicationInitializer() {
    /*@Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }*/

    @Bean
    fun httpSessionIdResolver(): HttpSessionIdResolver {
        val resolver = CookieHttpSessionIdResolver()
        val cookieSerializer = DefaultCookieSerializer()
        cookieSerializer.setUseBase64Encoding(false)
        cookieSerializer.setUseHttpOnlyCookie(false)
        resolver.setCookieSerializer(cookieSerializer)
        return resolver
    }

    @Bean
    fun rememberMeServices(): SpringSessionRememberMeServices {
        val rememberMeServices = SpringSessionRememberMeServices()
        rememberMeServices.setAlwaysRemember(true)
        return rememberMeServices
    }

}
