package ru.stnk.RestTestAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stnk.RestTestAPI.entity.coins.QuoteBinOneMinute;

import java.util.List;

@Repository
public interface QuoteBinOneMinuteRepository extends JpaRepository<QuoteBinOneMinute, Long> {
    QuoteBinOneMinute findBySymbolName(String symbolName);
    List<QuoteBinOneMinute> findBySymbolNameOrderByTimestampDesc(String symbolName);
}
