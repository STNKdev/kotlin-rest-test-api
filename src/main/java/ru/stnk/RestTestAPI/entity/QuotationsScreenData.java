package ru.stnk.RestTestAPI.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotationsScreenData {
    private QuotationsPairsData[] pairs_data;

    public QuotationsScreenData() {
    }

    public QuotationsPairsData[] getPairs_data() {
        return pairs_data;
    }

    public void setPairs_data(QuotationsPairsData[] pairs_data) {
        this.pairs_data = pairs_data;
    }

    @Override
    public String toString() {
        return "QuotationsScreenData{" +
                "pairs_data=" + Arrays.toString(pairs_data) +
                '}';
    }
}
