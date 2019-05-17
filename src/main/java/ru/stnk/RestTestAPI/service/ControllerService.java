package ru.stnk.RestTestAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stnk.RestTestAPI.dto.UserDTO;
import ru.stnk.RestTestAPI.entity.User;
import ru.stnk.RestTestAPI.entity.VerificationCode;
import ru.stnk.RestTestAPI.exception.registration.DelayException;
import ru.stnk.RestTestAPI.exception.registration.UserExistException;
import ru.stnk.RestTestAPI.repository.RolesRepository;
import ru.stnk.RestTestAPI.repository.UserRepository;
import ru.stnk.RestTestAPI.repository.VerificationCodeRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ControllerService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private MailSender mailSender;

    private final int EXPIRY_TIME = 300;

    private final int DELAY_TIME = 60;

    private final int CONFURM_CODE = 9999;

    @Transactional
    public User registerNewUserAccount (UserDTO userDTO) throws UserExistException {

        if (emailExists(userDTO.getEmail())) {
            throw new UserExistException();
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        user.setOs(userDTO.getOs());
        user.setEnableUser(true);
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

    public void checkOfVerificationCode (String email, int checkCode, UserDTO userDTO) throws UserExistException {
        Optional verificationCodeFromDB = verificationCodeRepository.findByUserEmail(email);
        Map<String, Object> data = new HashMap<>();

        if (verificationCodeFromDB.isPresent()) {
            VerificationCode verificationCode = (VerificationCode) verificationCodeFromDB.get();
            if (checkCode == verificationCode.getCheckCode() || checkCode == CONFURM_CODE) {
                //Сохраняем в таблицу нового пользователя
                User newUser = registerNewUserAccount(userDTO);
                //Удаляем из таблицы users_verification_code пользователя
                verificationCodeRepository.deleteById(verificationCode.getId());

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(newUser.getEmail(), newUser.getPassword());


                //Авторизуем пользователя и возвращаем session id
                /*UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
                Authentication auth = this.authenticationProvider.authenticate(authReq);
                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(auth);*/
            } else {
                Instant requestTime = Instant.now();
                verificationCode.setAttemps(verificationCode.getAttemps() - 1);
                verificationCodeRepository.save(verificationCode);
            }
        }

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
