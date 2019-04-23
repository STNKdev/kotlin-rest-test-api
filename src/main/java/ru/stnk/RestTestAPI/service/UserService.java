package ru.stnk.RestTestAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
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
public class UserService implements IUserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private MailSender mailSender;

    @Transactional
    @Override
    public User registerNewUserAccount (UserDTO userDTO) throws UserExistException {

        if (emailExists(userDTO.getEmail())) {
            throw new UserExistException();
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        user.setOs(userDTO.getOs());
        user.setEnableUser(false);
        user.setEmailConfirmed(false);
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

    public Map<String, Object> saveCheckCodeToEmail(String email) throws DelayException {
        int checkCode = getRandomIntegerBetweenRange(1000, 9999);

        Optional verificationCodeFromDB = verificationCodeRepository.findByUserEmail(email);
        VerificationCode verificationCode;
        Map<String, Object> data = new HashMap<>();

        if (verificationCodeFromDB.isPresent()) {
            Instant requestTime = Instant.now();
            verificationCode = (VerificationCode) verificationCodeFromDB.get();

            Duration timeDifferenceDelay = Duration.between(verificationCode.getDelayDate(), requestTime);
            Duration timeDifferenceExpiry = Duration.between(verificationCode.getCreateDate(), requestTime);

            if (timeDifferenceDelay.getSeconds() <= verificationCode.DELAY_TIME) {
                throw new DelayException();
            } else {
                sendCheckCodeToEmail(email, verificationCode.getCheckCode());
                verificationCode.setDelayDate(Instant.now());
                verificationCodeRepository.save(verificationCode);
                data.put("secondsUntilExpired", verificationCode.EXPIRY_TIME - timeDifferenceExpiry.getSeconds());
            }

            if (timeDifferenceExpiry.getSeconds() >= verificationCode.EXPIRY_TIME) {
                verificationCodeRepository.delete(verificationCode);
            }

        } else {
            verificationCode = new VerificationCode(checkCode, email);
            verificationCodeRepository.save(verificationCode);
            sendCheckCodeToEmail(email, verificationCode.getCheckCode());
            data.put("secondsUntilExpired", verificationCode.EXPIRY_TIME);
        }

        data.put("attempts", verificationCode.getAttemps());
        data.put("secondsUntilResend", verificationCode.DELAY_TIME);

        return data;
    }

    private void sendCheckCodeToEmail(String email, int checkCode) {

        String message = String.format("Hello! Your check code:\n %s", checkCode);

        mailSender.send(email, "Activation code", message);

    }

    //Получить случайное число от min до max
    private static int getRandomIntegerBetweenRange(int min, int max) {
        int x = (int) (Math.random() * ( (max - min) + 1 )) + min;
        return x;
    }
}
