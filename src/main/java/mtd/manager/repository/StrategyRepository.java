package mtd.manager.repository;


import mtd.manager.entity.Strategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface StrategyRepository extends JpaRepository<Strategy, Long>, JpaSpecificationExecutor<Strategy> {
    @Transactional
    @Modifying
    @Query("update Strategy s set s.enabled = ?1")
    void updateAllStrategies(@NonNull Boolean enabled);

    Optional<Strategy> findByName(String name);

}