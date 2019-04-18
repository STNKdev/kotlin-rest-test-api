package ru.stnk.RestTestAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stnk.RestTestAPI.dto.UserDTO;
import ru.stnk.RestTestAPI.entity.User;
import ru.stnk.RestTestAPI.entity.VerificationCode;
import ru.stnk.RestTestAPI.exception.registration.UserExistException;
import ru.stnk.RestTestAPI.repository.RolesRepository;
import ru.stnk.RestTestAPI.repository.UserRepository;
import ru.stnk.RestTestAPI.repository.VerificationCodeRepository;

import java.util.ArrayList;
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

    private boolean emailExists(String email) {
        Optional<User> user = repository.findByEmail(email);
        if (user.isPresent()) {
            return true;
        }
        return false;
    }

    public int saveCheckCodeToEmail(String email) {
        int checkCode = getRandomIntegerBetweenRange(1000, 9999);

        Optional verificationCodeFromDB = verificationCodeRepository.findByUserEmail(email);

        if (verificationCodeFromDB.isPresent()) {
            VerificationCode verificationCode = (VerificationCode) verificationCodeFromDB.get();
            verificationCode.setCheckCode(checkCode);
            verificationCodeRepository.save(verificationCode);
        } else {
            VerificationCode verificationCode = new VerificationCode(checkCode, email);
            verificationCodeRepository.save(verificationCode);
        }

        return checkCode;
    }

    public void sendCheckCodeToEmail(String email) {

        Optional verificationCodeFromDB = verificationCodeRepository.findByUserEmail(email);

        if (verificationCodeFromDB.isPresent()) {

            VerificationCode verificationCode = (VerificationCode) verificationCodeFromDB.get();

            String message = String.format("Hello! Your check code:\n %s", verificationCode.getCheckCode());

            mailSender.send(email, "Activation code", message);
        }

    }

    //Получить случайное число от min до max
    private static int getRandomIntegerBetweenRange(int min, int max) {
        int x = (int) (Math.random() * ( (max - min) + 1 )) + min;
        return x;
    }
}
