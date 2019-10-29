package ru.stnk.RestTestAPI.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationsData {
    private QuotationsScreenData screen_data;

    public QuotationsData() {
    }

    public QuotationsScreenData getScreen_data() {
        return screen_data;
    }

    public void setScreen_data(QuotationsScreenData screen_data) {
        this.screen_data = screen_data;
    }

    @Override
    public String toString() {
        return "QuotationsData{" +
                "screen_data=" + screen_data +
                '}';
    }
}
