package ru.stnk.RestTestAPI.service;

import ru.stnk.RestTestAPI.dto.UserDTO;
import ru.stnk.RestTestAPI.entity.User;
import ru.stnk.RestTestAPI.exception.registration.UserExistException;

public interface IUserService {
    User registerNewUserAccount (UserDTO userDTO) throws UserExistException;
}
