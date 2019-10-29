package ru.stnk.RestTestAPI.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.stnk.RestTestAPI.entity.coins.QuoteBinFiveMinute;
import ru.stnk.RestTestAPI.entity.coins.QuoteBinOneMinute;
import ru.stnk.RestTestAPI.repository.QuoteBinFiveMinuteRepository;
import ru.stnk.RestTestAPI.repository.QuoteBinOneMinuteRepository;

import java.util.List;

@Component
public class SheduleDeleteRowsInCoinTables {
    @Autowired
    private QuoteBinOneMinuteRepository quoteBinOneMinuteRepository;

    @Autowired
    private QuoteBinFiveMinuteRepository quoteBinFiveMinuteRepository;

    @Scheduled(fixedDelay = 60000)
    public void deleteRows () {
        List<QuoteBinOneMinute> oneMinuteList = quoteBinOneMinuteRepository.findAll();
        List<QuoteBinFiveMinute> fiveMinuteList = quoteBinFiveMinuteRepository.findAll();

        if (oneMinuteList.size() > 500) {
            for (int i = 0; i < oneMinuteList.size() - 499; i++) {
                quoteBinOneMinuteRepository.delete(oneMinuteList.get(i));
            }
        }

        if (fiveMinuteList.size() > 500) {
            for (int i = 0; i < fiveMinuteList.size() - 499; i++) {
                quoteBinFiveMinuteRepository.delete(fiveMinuteList.get(i));
            }
        }
    }
}
