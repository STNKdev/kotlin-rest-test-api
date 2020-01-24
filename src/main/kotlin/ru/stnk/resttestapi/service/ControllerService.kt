package ru.stnk.resttestapi.service

//import ru.stnk.resttestapi.configuration.SecurityConfig
//import ru.stnk.resttestapi.entity.RoleName

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.stnk.resttestapi.entity.RoleName
import ru.stnk.resttestapi.message.request.UserLoginForm
import ru.stnk.resttestapi.entity.User
import ru.stnk.resttestapi.entity.VerificationCode
import ru.stnk.resttestapi.exception.registration.DelayException
import ru.stnk.resttestapi.exception.registration.UserExistException
import ru.stnk.resttestapi.repository.RolesRepository
import ru.stnk.resttestapi.repository.UserRepository
import ru.stnk.resttestapi.repository.VerificationCodeRepository
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest

//import java.util.Optional

@Service
@Transactional
class ControllerService (
        @Autowired val repository: UserRepository,
        @Autowired val rolesRepository: RolesRepository,
        @Autowired val verificationCodeRepository: VerificationCodeRepository,
        @Autowired val mailSender: MailSender
) {

    /*@Autowired
    private val securityConfig: SecurityConfig? = null

    @Autowired
    private val userDetailsService: UserDetailsServiceImpl? = null*/

    private val logger: Logger = LoggerFactory.getLogger(ControllerService::class.java)

    private val expiryTime = 300

    private val delayTime = 60

    private val confirmCode = 9999

    // Регистрируем нового пользователя
    @Throws(UserExistException::class)
    fun registerNewUserAccount(userLoginForm: UserLoginForm): User {

        val user = User()
        user.email = userLoginForm.email
        user.password = userLoginForm.password
        user.phone = userLoginForm.phone
        user.os = userLoginForm.os
        user.isEnabled = true
        user.emailConfirmed = true
        user.betBalance = 0
        user.freeBalance = 0
        user.withdrawalBalance =  0
        user.roles.add(rolesRepository.findByName(RoleName.ROLE_USER))

        return repository.save(user)
    }

    //Проверка на существование пользователя с таким email
    /*private*/ fun userExists(email: String): Boolean {
        return repository.existsByEmail(email)
    }

    // Сверка проверочного кода
    @Throws(UserExistException::class, DelayException::class)
    fun checkOfVerificationCode(userLoginForm: UserLoginForm, checkCode: String, request: HttpServletRequest): Map<String, Any> {

        // Поиск пользователя по email в таблице проверочных кодов
        val verificationCodeFromDB = verificationCodeRepository.findByUserEmail(userLoginForm.email)

        var data: MutableMap<String, Any> = HashMap()

        // Если пользователь найден
        // а если не найден, то придёт пустой ответ...
        if (verificationCodeFromDB.isPresent) {

            // Получаем объект проверочного кода
            val verificationCode = verificationCodeFromDB.get()

            // Используя автоприведение типов, сравниваем проверочный код <- так было удобно в java
            if (checkCode.equals(verificationCode.checkCode.toString()) || checkCode.equals(confirmCode.toString()) ) {

                // Удаляем строку с проверочным кодом
                verificationCodeRepository.delete(verificationCode)
                // Регистрируем нового пользователя
                val newUser: User = registerNewUserAccount(userLoginForm)

                logger.info("Зарегистрирован новый пользователь id ${newUser.id} email: ${newUser.email}")

                // Авторизовываем нового пользователя и возвращаем id сессии
                try {
                    //request.changeSessionId();
                    request.login(userLoginForm.email, userLoginForm.password)
                } catch (ex: ServletException) {
                    // этот момент нужно залогировать
                    logger.debug("Ошибка при авторизации пользователя id: ${newUser.id} и email: ${newUser.email}" + ex.localizedMessage)
                }

                data["session_id"] = request.session.id
                return data

            } else {

                /*
                * Если код не совпал, то отнимаем одну попытку
                * и записываем новые временные ограничения
                */

                val requestTime = Instant.now()
                val timeDifferenceExpiry = Duration.between(verificationCode.createDate, requestTime)

                if (verificationCode.attemps > 0 && timeDifferenceExpiry.seconds < expiryTime) {

                    verificationCode.attemps = verificationCode.attemps - 1
                    verificationCodeRepository.save(verificationCode)
                    data["attempts"] = verificationCode.attemps
                    data["secondsUntilExpired"] = expiryTime - timeDifferenceExpiry.seconds

                } else {
                    /*
                    * При исчерпании попыток, генерируем новый проверочный код
                    * и высылаем его на почту
                    */
                    verificationCodeRepository.delete(verificationCode)
                    data = saveCheckCodeToEmail(userLoginForm.email, userLoginForm.isViaEmail)
                }
            }
        }

        return data

    }

    // Генерация проверочного кода и сохранение его в базу
    @Throws(DelayException::class)
    fun saveCheckCodeToEmail(email: String, viaEmail: Boolean): MutableMap<String, Any> {

        val verificationCodeFromDB: Optional<VerificationCode> = verificationCodeRepository.findByUserEmail(email)
        val verificationCode: VerificationCode
        val data = HashMap<String, Any>()
        val requestTime = Instant.now()

        if (userExists(email)) {
            throw UserExistException()
        }

        if (verificationCodeFromDB.isPresent) {

            verificationCode = verificationCodeFromDB.get()

            // Задержка для повторной отправки проверочного кода
            val timeDifferenceDelay = Duration.between(requestTime, verificationCode.delayDate)
            // Общее время жизни проверочного кода
            val timeDifferenceExpiry = Duration.between(requestTime, verificationCode.expiredDate)

            /*
            * Проверка задержки и время жизни проверочного кода
            */

            if (timeDifferenceDelay.seconds > 0 && timeDifferenceDelay.seconds < delayTime) {
                throw DelayException(timeDifferenceDelay.seconds)
            } else if (timeDifferenceExpiry.seconds < expiryTime && timeDifferenceExpiry.seconds > 0) {

                if (viaEmail) {
                    sendCheckCodeToEmail(email, verificationCode.checkCode)
                }

                verificationCode.delayDate = verificationCode.delayDate!!.plusSeconds(delayTime.toLong())
                verificationCodeRepository.save(verificationCode)
                data["secondsUntilExpired"] = timeDifferenceExpiry.seconds

                if (timeDifferenceDelay.seconds == 0L) {
                    data["secondsUntilResend"] = delayTime
                } else if (timeDifferenceDelay.seconds < 0) {
                    data["secondsUntilResend"] = timeDifferenceExpiry.seconds % 60
                } else {
                    data["secondsUntilResend"] = timeDifferenceDelay.seconds
                }

            }

            /*
            * Если время жизни проверочного кода истекло,
            * то удаляем из таблицы текущую связку пользователь-проверочный код
            * и
            */
            if (timeDifferenceExpiry.seconds <= 0) {
                verificationCodeRepository.delete(verificationCode)
                return saveCheckCodeToEmail(email, viaEmail)
            }

        } else {

            // Задал функции параметры по умолчанию, в целом можно вообще не создавать переменную
            // val checkCode = getRandomIntegerBetweenRange(1000, 9999)
            val checkCode = getRandomIntegerBetweenRange()
            verificationCode = VerificationCode(checkCode, email, delayTime, requestTime.plusSeconds(expiryTime.toLong()))
            verificationCodeRepository.save(verificationCode)

            if (viaEmail) {
                sendCheckCodeToEmail(email, verificationCode.checkCode)
            }

            data["secondsUntilExpired"] = expiryTime
            data["secondsUntilResend"] = delayTime
        }

        data["attempts"] = verificationCode.attemps

        return data
    }

    fun getUser(email: String): User? {

        val userFromDB = repository.findByEmail(email)

        return if (userFromDB.isPresent) {
            userFromDB.get()
        } else null

    }

    //Оставлю как памятку
    /*public Session registerUserSecurityContext (String email, String pass, HttpServletRequest request) throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Session session = sessionRepository.createSession();
        Map<String, String> details = new HashMap<>();
        details.put("remoteAddress", request.getRemoteAddr());
        details.put("sessionId", session.getId());
        UsernamePasswordAuthenticationToken tokenAuth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), pass, userDetails.getAuthorities());
        //tokenAuth.setDetails(new WebAuthenticationDetails(request));
        tokenAuth.setDetails(details);

        Authentication authentication = securityConfig.authenticationManagerBean().authenticate(tokenAuth);

        *//*SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);*//*
        session.setAttribute("SPRING_SECURITY_CONTEXT", new SecurityContextImpl(authentication));
        sessionRepository.save(session);
        //Map<String, Object> mapSession = sessionRepository.findByPrincipalName(userDetails.getUsername());

        *//*if (!mapSession.isEmpty()) {
            return mapSession.toString();
        }*//*

        return session;
    }*/

    // Отправка проверочного кода (в асинхронном режиме)
    private fun sendCheckCodeToEmail(email: String, checkCode: Int) {

        val message = String.format("Привет! Ваш код активации:\n %s", checkCode)

        mailSender.send(email, "Код подтверждения", message)

    }

    //Получить случайное число от min до max для проверочного кода
    private fun getRandomIntegerBetweenRange(min: Int = 1000, max: Int = 9998): Int {
        return (Math.random() * (max - min + 1)).toInt() + min
    }
}
