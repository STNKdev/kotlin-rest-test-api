package ru.stnk.RestTestAPI.exception.registration;

public class DelayException extends Exception {

    private Long delay = null;

    public DelayException(){}

    public DelayException(Long delay){
        this.delay = delay;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
