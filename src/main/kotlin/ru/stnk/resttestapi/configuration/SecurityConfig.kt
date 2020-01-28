package ru.stnk.resttestapi.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import ru.stnk.resttestapi.configuration.jwt.JwtAuthTokenFilter
import ru.stnk.resttestapi.configuration.jwt.RestAuthenticationEntryPoint
//import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices
import ru.stnk.resttestapi.service.user.UserDetailsServiceImpl

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    private val userDetailsService: UserDetailsServiceImpl? = null

    @Autowired
    private val restAuthenticationEntryPoint: RestAuthenticationEntryPoint? = null

    /*@Autowired
    private val authenticationSuccessHandler: CustomSimpleUrlAuthenticationSuccessHandler? = null

    @Autowired
    private val authenticationFailureHandler: CustomAuthenticationFailureHandler? = null*/

    /*@Autowired
    private val rememberMeServices: SpringSessionRememberMeServices? = null*/

    /*@Autowired
    private FindByIndexNameSessionRepository<S> sessionRepository;*/

    /*@Autowired
    private JdbcOperations jdbcOperations;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    private FindByIndexNameSessionRepository sessionRepository = new JdbcOperationsSessionRepository(jdbcOperations, platformTransactionManager);*/

    //private SimpleUrlAuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun authenticationJwtTokenFilter(): JwtAuthTokenFilter {
        return JwtAuthTokenFilter()
    }

    @Throws(Exception::class)
    override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {

        /*auth.jdbcAuthentication().dataSource(dataSource)
                //проверка существования пользователя и его состояние enable_user
                .usersByUsernameQuery("select email,password,enabled from users where email=?")
                //запрос роли пользователя
                .authoritiesByUsernameQuery("select user_email,role_name from user_roles where user_email=?")
                //нужно для проверки пароля
                .passwordEncoder(new BCryptPasswordEncoder());*/

        //auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(new BCryptPasswordEncoder());

        //auth!!.authenticationProvider(authenticationProvider())

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(BCryptPasswordEncoder())
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {

        http
                //.sessionManagement()
                //.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                //.maximumSessions(2).sessionRegistry(sessionRegistry())
                //.sessionFixation().migrateSession()
                //.and()
                //HTTP Basic authentication
                /*.httpBasic()
                .and()*/
                //.rememberMe()
                //.rememberMeServices(rememberMeServices)
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/reg-start").permitAll()
                .antMatchers("/reg-confirm").permitAll()
                //.antMatchers(HttpMethod.GET, "/add-role").permitAll()
                //.antMatchers(HttpMethod.GET, HttpMethod.POST, "/login").permitAll()
                //.antMatchers(HttpMethod.GET, "/auth").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/currtime").permitAll()
                .antMatchers(HttpMethod.GET, "/quotations").permitAll()
                .antMatchers(HttpMethod.GET, "/coins").permitAll()
                .antMatchers(HttpMethod.GET, "/candles-one").permitAll()
                .antMatchers(HttpMethod.GET, "/candles-five").permitAll()
                .antMatchers(HttpMethod.GET, "/userinfo").authenticated()
                .antMatchers("/logout").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .disable()
                //.usernameParameter("email")
                //.successHandler(authenticationSuccessHandler)
                //.failureHandler(authenticationFailureHandler)
                //.and()
                .logout().disable()//.and()

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    /*@Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsServiceImpl)
        authProvider.setPasswordEncoder(BCryptPasswordEncoder())
        return authProvider
    }*/

    //https://docs.spring.io/spring-session/docs/2.1.6.BUILD-SNAPSHOT/reference/html5/#spring-security-concurrent-sessions
    /*@Bean
    public SpringSessionBackedSessionRegistry<S> sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }*/

    /*
     * Взято отсюда
     * https://alexkosarev.name/2016/05/19/spring-security-token-authentication-part-1/
     */
    /*@Bean
    public RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() throws Exception {
        RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter = new RequestHeaderAuthenticationFilter();
        requestHeaderAuthenticationFilter.setPrincipalRequestHeader("X-AUTH-TOKEN");
        requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManager());
        //requestHeaderAuthenticationFilter.setExceptionIfHeaderMissing(false);

        return requestHeaderAuthenticationFilter;
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService()));

        return preAuthenticatedAuthenticationProvider;
    }*/


    /*@Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }*/

}