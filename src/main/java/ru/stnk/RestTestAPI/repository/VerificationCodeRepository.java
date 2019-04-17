package ru.stnk.RestTestAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stnk.RestTestAPI.entity.VerificationCode;

import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByUserEmail (String email);
}
