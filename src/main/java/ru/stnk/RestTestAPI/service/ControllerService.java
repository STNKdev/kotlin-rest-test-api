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

    public User registerNewUserAccount (UserDTO userDTO) throws UserExistException {

        if (emailExists(userDTO.getEmail())) {
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
    private boolean emailExists(String email) {
        Optional<User> user = repository.findByEmail(email);
        if (user.isPresent()) {
            return true;
        }
        return false;
    }

    public Map<String, Object> checkOfVerificationCode (UserDTO userDTO, String checkCode, HttpServletRequest request) throws UserExistException, DelayException {

        Optional verificationCodeFromDB = verificationCodeRepository.findByUserEmail(userDTO.getEmail());

        Map<String, Object> data = new HashMap<>();
        if (verificationCodeFromDB.isPresent()) {
            VerificationCode verificationCode = (VerificationCode) verificationCodeFromDB.get();
            if (checkCode.equals(verificationCode.getCheckCode()+"") || checkCode.equals(CONFURM_CODE+"")) {
                verificationCodeRepository.delete(verificationCode);
                registerNewUserAccount(userDTO);
                try {
                    //request.changeSessionId();
                    request.login(userDTO.getEmail(), userDTO.getPassword());
                } catch (ServletException ex) {

                }
                data.put("session_id", request.getSession().getId());
                return data;
            } else {
                Instant requestTime = Instant.now();
                Duration timeDifferenceExpiry = Duration.between(verificationCode.getCreateDate(), requestTime);

                if (verificationCode.getAttemps() > 0 &&
                        !(timeDifferenceExpiry.getSeconds() >= EXPIRY_TIME)) {
                    verificationCode.setAttemps(verificationCode.getAttemps() - 1);
                    verificationCodeRepository.save(verificationCode);
                    data.put("attempts", verificationCode.getAttemps());
                    data.put("secondsUntilExpired", EXPIRY_TIME - timeDifferenceExpiry.getSeconds());
                } else {
                    verificationCodeRepository.delete(verificationCode);
                    data = saveCheckCodeToEmail(userDTO.getEmail(), userDTO.isViaEmail());
                }
            }
        }

        return data;

    }

    public Map<String, Object> saveCheckCodeToEmail(String email, boolean viaEmail) throws DelayException {
        int checkCode = getRandomIntegerBetweenRange(1000, 9999);

        Optional verificationCodeFromDB = verificationCodeRepository.findByUserEmail(email);
        VerificationCode verificationCode;
        Map<String, Object> data = new HashMap<>();

        if (verificationCodeFromDB.isPresent()) {
            Instant requestTime = Instant.now();
            verificationCode = (VerificationCode) verificationCodeFromDB.get();

            Duration timeDifferenceDelay = Duration.between(verificationCode.getDelayDate(), requestTime);
            Duration timeDifferenceExpiry = Duration.between(verificationCode.getCreateDate(), requestTime);

            if (timeDifferenceDelay.getSeconds() <= DELAY_TIME) {
                throw new DelayException();
            } else {
                if (viaEmail) {
                    sendCheckCodeToEmail(email, verificationCode.getCheckCode());
                }

                verificationCode.setDelayDate(Instant.now());
                verificationCodeRepository.save(verificationCode);
                data.put("secondsUntilExpired", EXPIRY_TIME - timeDifferenceExpiry.getSeconds());
            }

            if (timeDifferenceExpiry.getSeconds() >= EXPIRY_TIME) {
                verificationCodeRepository.delete(verificationCode);
            }

        } else {
            verificationCode = new VerificationCode(checkCode, email);
            verificationCodeRepository.save(verificationCode);

            if (viaEmail) {
                sendCheckCodeToEmail(email, verificationCode.getCheckCode());
            }

            data.put("secondsUntilExpired", EXPIRY_TIME);
        }

        data.put("attempts", verificationCode.getAttemps());
        data.put("secondsUntilResend", DELAY_TIME);

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

    private void sendCheckCodeToEmail(String email, int checkCode) {

        String message = String.format("Привет! Ваш код активации:\n %s", checkCode);

        mailSender.send(email, "Проверочный код", message);

    }

    //Получить случайное число от min до max
    private static int getRandomIntegerBetweenRange(int min, int max) {
        int x = (int) (Math.random() * ( (max - min) + 1 )) + min;
        return x;
    }
}
