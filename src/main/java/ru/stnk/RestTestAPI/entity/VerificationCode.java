package ru.stnk.RestTestAPI.entity;

import javax.persistence.*;
import java.time.Instant;

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
    private Instant createDate;

    @Column(name = "delay_date")
    private Instant delayDate;

    public VerificationCode() {}

    public VerificationCode(int checkCode, String userEmail) {
        this.checkCode = checkCode;
        this.userEmail = userEmail;
        this.attemps = 3;
        this.createDate = Instant.now();
        this.delayDate = Instant.now();
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

    public Instant getCreateDate () {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getDelayDate() {
        return delayDate;
    }

    public void setDelayDate(Instant delayDate) {
        this.delayDate = delayDate;
    }
}
