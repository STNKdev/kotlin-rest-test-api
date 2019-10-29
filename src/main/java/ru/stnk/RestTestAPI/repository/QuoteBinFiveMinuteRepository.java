package ru.stnk.RestTestAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stnk.RestTestAPI.entity.coins.QuoteBinFiveMinute;

import java.util.List;

@Repository
public interface QuoteBinFiveMinuteRepository extends JpaRepository<QuoteBinFiveMinute, Long> {
    QuoteBinFiveMinute findBySymbolName(String symbolName);
    List<QuoteBinFiveMinute> findBySymbolNameOrderByTimestampDesc(String symbolName);
}
