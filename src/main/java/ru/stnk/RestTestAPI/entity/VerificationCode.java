package ru.stnk.RestTestAPI.entity;

import ru.stnk.RestTestAPI.dto.UserDTO;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/*@Entity
@Table(name = "verification_code")*/
public class VerificationCode {
    private static final int EXPIRATION = 60 * 24;

   /* @Id
    @GeneratedValue(strategy = GenerationType.AUTO)*/
    private Long id;

    private int code;

    /*@OneToOne(targetEntity = UserDTO.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_email")*/
    private UserDTO user;

    private Date expiryDate;

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public VerificationCode() {}

    public VerificationCode(int code) {
        this.code = code;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public VerificationCode(int code, UserDTO user) {
        this.code = code;
        this.user = user;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public VerificationCode(int code, UserDTO user, int expiryTimeInMinutes) {
        this.code = code;
        this.user = user;
        this.expiryDate = calculateExpiryDate(expiryTimeInMinutes);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
