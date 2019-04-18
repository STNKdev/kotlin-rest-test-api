package ru.stnk.RestTestAPI.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users_verification_code")
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "checkCode")
    private int checkCode;

    @Column(name = "userEmail")
    private String userEmail;

    @Column(name = "attemps")
    private int attemps;

    @Column(name = "create_date")
    private Long createDate;

    private int expiryTime;

    /*private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }*/

    public VerificationCode() {}

    public VerificationCode(int checkCode, String userEmail) {
        this.checkCode = checkCode;
        this.userEmail = userEmail;
        this.attemps = 3;
        this.createDate = new Date().getTime();
    }

    public Long getId() {
        return id;
    }

    public int getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(int checkCode) {
        this.checkCode = checkCode;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getAttemps() {
        return attemps;
    }

    public void setAttemps (int attemps) {
        this.attemps = attemps;
    }

    public Long getCreateDate () {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }
}
