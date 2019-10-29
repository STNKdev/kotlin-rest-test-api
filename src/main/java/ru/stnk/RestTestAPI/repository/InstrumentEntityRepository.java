package ru.stnk.RestTestAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stnk.RestTestAPI.entity.coins.InstrumentEntity;

@Repository
public interface InstrumentEntityRepository extends JpaRepository<InstrumentEntity, Long> {
    InstrumentEntity findBySymbolName(String symbolName);
}
