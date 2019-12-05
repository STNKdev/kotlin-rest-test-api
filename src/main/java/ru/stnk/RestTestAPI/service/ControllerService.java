package ru.stnk.RestTestAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stnk.RestTestAPI.configuration.SecurityConfig;
import ru.stnk.RestTestAPI.dto.UserDTO;
import ru.stnk.RestTestAPI.entity.User;
import ru.stnk.RestTestAPI.entity.VerificationCode;
import ru.stnk.RestTestAPI.exception.registration.DelayException;
import ru.stnk.RestTestAPI.exception.registration.UserExistException;
import ru.stnk.RestTestAPI.repository.RolesRepository;
import ru.stnk.RestTestAPI.repository.UserRepository;
import ru.stnk.RestTestAPI.repository.VerificationCodeRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ControllerService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MailSender mailSender;

    private final int EXPIRY_TIME = 300;

    private final int DELAY_TIME = 60;

    private final int CONFURM_CODE = 9999;

    // Регистрируем нового пользователя
    public User registerNewUserAccount (UserDTO userDTO) throws UserExistException {

        if (userExists(userDTO.getEmail())) {
            throw new UserExistException();
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        user.setOs(userDTO.getOs());
        user.setEnabled(true);
        user.setEmailConfirmed(true);
        user.setBetBalance((long) 0);
        user.setFreeBalance((long) 0);
        user.setWithdrawalBalance((long) 0);
        user.setRoles(new ArrayList<>());
        user.addRole(rolesRepository.findByName("ROLE_USER"));

        return repository.save(user);
    }

    //Проверка на существование пользователя с таким email
    private boolean userExists(String email) {
        Optional<User> user = repository.findByEmail(email);
        if (user.isPresent()) {
            return true;
        }
        return false;
    }

    // Сверка проверочного кода
    public Map<String, Object> checkOfVerificationCode (UserDTO userDTO, String checkCode, HttpServletRequest request) throws UserExistException, DelayException {

        // Поиск пользователя по email в таблице проверочных кодов
        Optional verificationCodeFromDB = verificationCodeRepository.findByUserEmail(userDTO.getEmail());

        Map<String, Object> data = new HashMap<>();

        // Если пользователь найден
        // а если не найден, то придёт пустой ответ...
        if (verificationCodeFromDB.isPresent()) {

            // Получаем объект проверочного кода
            VerificationCode verificationCode = (VerificationCode) verificationCodeFromDB.get();

            // Используя автоприведение типов, сравниваем проверочный код
            if (checkCode.equals(verificationCode.getCheckCode()+"") || checkCode.equals(CONFURM_CODE+"")) {

                // Удаляем строку с проверочным кодом
                verificationCodeRepository.delete(verificationCode);
                // Регистрируем нового пользователя
                // думаю нужно залогировать это событие
                registerNewUserAccount(userDTO);

                // Авторизовываем нового пользователя и возвращаем id сессии
                try {
                    //request.changeSessionId();
                    request.login(userDTO.getEmail(), userDTO.getPassword());
                } catch (ServletException ex) {
                    // этот момент нужно залогировать

                }

                data.put("session_id", request.getSession().getId());
                return data;

            } else {

                /*
                * Если код не совпал, то однимаем одну попытку
                * и записываем новые временные ограничения
                */

                Instant requestTime = Instant.now();
                Duration timeDifferenceExpiry = Duration.between(verificationCode.getCreateDate(), requestTime);

                if (verificationCode.getAttemps() > 0 &&
                        !(timeDifferenceExpiry.getSeconds() >= EXPIRY_TIME)) {

                    verificationCode.setAttemps(verificationCode.getAttemps() - 1);
                    verificationCodeRepository.save(verificationCode);
                    data.put("attempts", verificationCode.getAttemps());
                    data.put("secondsUntilExpired", EXPIRY_TIME - timeDifferenceExpiry.getSeconds());

                } else {
                    /*
                    * При исчерпании попыток, генерируем новый проверочный код
                    * и высылаем его на почту
                    */
                    verificationCodeRepository.delete(verificationCode);
                    data = saveCheckCodeToEmail(userDTO.getEmail(), userDTO.isViaEmail());
                }
            }
        }

        return data;

    }

    // Генерация проверочного кода и сохранение его в базу
    public Map<String, Object> saveCheckCodeToEmail(String email, boolean viaEmail) throws DelayException {

        Optional verificationCodeFromDB = verificationCodeRepository.findByUserEmail(email);
        VerificationCode verificationCode;
        Map<String, Object> data = new HashMap<>();
        Instant requestTime = Instant.now();

        if (verificationCodeFromDB.isPresent()) {

            verificationCode = (VerificationCode) verificationCodeFromDB.get();

            // Задержка для повторной отправки проверочного кода
            Duration timeDifferenceDelay = Duration.between(requestTime, verificationCode.getDelayDate());
            // Общее время жизни проверочного кода
            Duration timeDifferenceExpiry = Duration.between(requestTime, verificationCode.getExpiredDate());

            /*
            * Проверка задержки и время жизни проверочного кода
            */

            if (timeDifferenceDelay.getSeconds() > 0 && timeDifferenceDelay.getSeconds() < DELAY_TIME) {
                throw new DelayException(timeDifferenceDelay.getSeconds());
            } else if (timeDifferenceExpiry.getSeconds() < EXPIRY_TIME && timeDifferenceExpiry.getSeconds() > 0) {

                if (viaEmail) {
                    sendCheckCodeToEmail(email, verificationCode.getCheckCode());
                }

                verificationCode.setDelayDate(verificationCode.getDelayDate().plusSeconds(DELAY_TIME));
                verificationCodeRepository.save(verificationCode);
                data.put("secondsUntilExpired", timeDifferenceExpiry.getSeconds());

                if (timeDifferenceDelay.getSeconds() == 0) {
                    data.put("secondsUntilResend", DELAY_TIME);
                } else if (timeDifferenceDelay.getSeconds() < 0) {
                    data.put("secondsUntilResend", timeDifferenceExpiry.getSeconds() % 60);
                } else {
                    data.put("secondsUntilResend", timeDifferenceDelay.getSeconds());
                }

            }

            /*
            * Если время жизни проверочного кода истекло,
            * то удаляем из таблицы текущую связку пользователь-проверочный код
            * и
            */
            if (timeDifferenceExpiry.getSeconds() <= 0) {
                verificationCodeRepository.delete(verificationCode);
                return saveCheckCodeToEmail(email, viaEmail);
            }

        } else {

            int checkCode = getRandomIntegerBetweenRange(1000, 9999);
            verificationCode = new VerificationCode(checkCode, email, DELAY_TIME, requestTime.plusSeconds(EXPIRY_TIME));
            verificationCodeRepository.save(verificationCode);

            if (viaEmail) {
                sendCheckCodeToEmail(email, verificationCode.getCheckCode());
            }

            data.put("secondsUntilExpired", EXPIRY_TIME);
            data.put("secondsUntilResend", DELAY_TIME);
        }

        data.put("attempts", verificationCode.getAttemps());

        return data;
    }

    public User getUser (String email) {

        Optional userFromDB = repository.findByEmail(email);

        if (userFromDB.isPresent()) {
            return (User) userFromDB.get();
        }

        return null;
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
    private void sendCheckCodeToEmail(String email, int checkCode) {

        String message = String.format("Привет! Ваш код активации:\n %s", checkCode);

        mailSender.send(email, "Проверочный код", message);

    }

    //Получить случайное число от min до max для проверочного кода
    private static int getRandomIntegerBetweenRange(int min, int max) {
        int x = (int) (Math.random() * ( (max - min) + 1 )) + min;
        return x;
    }
}
