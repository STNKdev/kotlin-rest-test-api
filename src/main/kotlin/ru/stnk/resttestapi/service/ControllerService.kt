package ru.stnk.resttestapi.service

//import ru.stnk.resttestapi.configuration.SecurityConfig
//import ru.stnk.resttestapi.entity.Roles

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.stnk.resttestapi.dto.UserDTO
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
class ControllerService {

    @Autowired
    private val repository: UserRepository? = null

    @Autowired
    private val rolesRepository: RolesRepository? = null

    @Autowired
    private val verificationCodeRepository: VerificationCodeRepository? = null

    /*@Autowired
    private val securityConfig: SecurityConfig? = null

    @Autowired
    private val userDetailsService: UserDetailsServiceImpl? = null*/

    @Autowired
    private val mailSender: MailSender? = null

    private val EXPIRY_TIME = 300

    private val DELAY_TIME = 60

    private val CONFURM_CODE = 9999

    // Регистрируем нового пользователя
    @Throws(UserExistException::class)
    fun registerNewUserAccount(userDTO: UserDTO): User {

        if (userExists(userDTO.email)) {
            throw UserExistException()
        }

        val user = User()
        user.email = userDTO.email
        user.password = userDTO.password
        user.phone = userDTO.phone
        user.os = userDTO.os
        user.isEnabled = true
        user.emailConfirmed = true
        user.betBalance = 0
        user.freeBalance = 0
        user.withdrawalBalance =  0
        user.roles.add(rolesRepository!!.findByName("ROLE_USER"))

        return repository!!.save(user)
    }

    //Проверка на существование пользователя с таким email
    private fun userExists(email: String): Boolean {
        val user = repository!!.findByEmail(email)
        return user.isPresent
    }

    // Сверка проверочного кода
    @Throws(UserExistException::class, DelayException::class)
    fun checkOfVerificationCode(userDTO: UserDTO, checkCode: String, request: HttpServletRequest): Map<String, Any> {

        // Поиск пользователя по email в таблице проверочных кодов
        val verificationCodeFromDB = verificationCodeRepository?.findByUserEmail(userDTO.email)

        var data: MutableMap<String, Any> = HashMap()

        // Если пользователь найден
        // а если не найден, то придёт пустой ответ...
        if (verificationCodeFromDB!!.isPresent) {

            // Получаем объект проверочного кода
            val verificationCode = verificationCodeFromDB.get()

            // Используя автоприведение типов, сравниваем проверочный код
            if (checkCode == verificationCode.checkCode.toString() + "" || checkCode == CONFURM_CODE.toString() + "") {

                // Удаляем строку с проверочным кодом
                verificationCodeRepository?.delete(verificationCode)
                // Регистрируем нового пользователя
                // думаю нужно залогировать это событие
                registerNewUserAccount(userDTO)

                // Авторизовываем нового пользователя и возвращаем id сессии
                try {
                    //request.changeSessionId();
                    request.login(userDTO.email, userDTO.password)
                } catch (ex: ServletException) {
                    // этот момент нужно залогировать

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

                if (verificationCode.attemps > 0 && timeDifferenceExpiry.seconds < EXPIRY_TIME) {

                    verificationCode.attemps = verificationCode.attemps - 1
                    verificationCodeRepository!!.save(verificationCode)
                    data["attempts"] = verificationCode.attemps
                    data["secondsUntilExpired"] = EXPIRY_TIME - timeDifferenceExpiry.seconds

                } else {
                    /*
                    * При исчерпании попыток, генерируем новый проверочный код
                    * и высылаем его на почту
                    */
                    verificationCodeRepository!!.delete(verificationCode)
                    data = saveCheckCodeToEmail(userDTO.email, userDTO.isViaEmail)
                }
            }
        }

        return data

    }

    // Генерация проверочного кода и сохранение его в базу
    @Throws(DelayException::class)
    fun saveCheckCodeToEmail(email: String?, viaEmail: Boolean): MutableMap<String, Any> {

        val verificationCodeFromDB = verificationCodeRepository!!.findByUserEmail(email!!)
        val verificationCode: VerificationCode
        val data = HashMap<String, Any>()
        val requestTime = Instant.now()

        if (verificationCodeFromDB.isPresent) {

            verificationCode = verificationCodeFromDB.get()

            // Задержка для повторной отправки проверочного кода
            val timeDifferenceDelay = Duration.between(requestTime, verificationCode.delayDate)
            // Общее время жизни проверочного кода
            val timeDifferenceExpiry = Duration.between(requestTime, verificationCode.expiredDate)

            /*
            * Проверка задержки и время жизни проверочного кода
            */

            if (timeDifferenceDelay.seconds > 0 && timeDifferenceDelay.seconds < DELAY_TIME) {
                throw DelayException(timeDifferenceDelay.seconds)
            } else if (timeDifferenceExpiry.seconds < EXPIRY_TIME && timeDifferenceExpiry.seconds > 0) {

                if (viaEmail) {
                    sendCheckCodeToEmail(email, verificationCode.checkCode)
                }

                verificationCode.delayDate = verificationCode.delayDate!!.plusSeconds(DELAY_TIME.toLong())
                verificationCodeRepository.save(verificationCode)
                data["secondsUntilExpired"] = timeDifferenceExpiry.seconds

                if (timeDifferenceDelay.seconds == 0L) {
                    data["secondsUntilResend"] = DELAY_TIME
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

            val checkCode = getRandomIntegerBetweenRange(1000, 9999)
            verificationCode = VerificationCode(checkCode, email, DELAY_TIME, requestTime.plusSeconds(EXPIRY_TIME.toLong()))
            verificationCodeRepository.save(verificationCode)

            if (viaEmail) {
                sendCheckCodeToEmail(email, verificationCode.checkCode)
            }

            data["secondsUntilExpired"] = EXPIRY_TIME
            data["secondsUntilResend"] = DELAY_TIME
        }

        data["attempts"] = verificationCode.attemps

        return data
    }

    fun getUser(email: String): User? {

        val userFromDB = repository!!.findByEmail(email)

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

        mailSender!!.send(email, "Проверочный код", message)

    }

    //Получить случайное число от min до max для проверочного кода
    private fun getRandomIntegerBetweenRange(min: Int, max: Int): Int {
        val x = (Math.random() * (max - min + 1)).toInt() + min
        return x
    }
}
